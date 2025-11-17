package com.frenzelts.pawsperfect

import com.frenzelts.pawsperfect.domain.mapper.BreedMapper
import com.frenzelts.pawsperfect.domain.model.Breed
import com.frenzelts.pawsperfect.domain.repository.DogRepository
import com.frenzelts.pawsperfect.domain.usecase.GetQuizQuestion
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetQuizQuestionTest {

    private val dispatcher = StandardTestDispatcher()

    private val repo = mockk<DogRepository>()
    private lateinit var useCase: GetQuizQuestion

    private val imageUrl = "https://images.dog.ceo/breeds/retriever-golden/n02099601_1000.jpg"

    private val golden = Breed("retriever", "golden")
    private val poodle = Breed("poodle")
    private val beagle = Breed("beagle")
    private val yorkshire = Breed("terrier", "yorkshire")
    private val shihtzu = Breed("shihtzu")

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        useCase = GetQuizQuestion(repo)
        coEvery { repo.getAllBreeds() } returns listOf(golden, poodle, beagle, yorkshire, shihtzu)
        coEvery { repo.getRandomImageUrl() } returns imageUrl
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
    }

    // -------------------------------------------------------------------------
    // 1. Happy flow
    // -------------------------------------------------------------------------

    @Test
    fun `Given repo returns breeds and image, When usecase is invoked, Then quiz contains correct and incorrect options`() =
        runTest(dispatcher) {
            // Given
            mockkObject(BreedMapper)
            every { BreedMapper.mapImageUrlToBreed(imageUrl) } returns golden

            // When
            val result = useCase(numOptions = 3)

            // Then
            assertEquals(imageUrl, result.imageUrl)
            assertEquals(3, result.options.size)

            val correctOption = result.options.first { it.isCorrect }
            val incorrectOptions = result.options.filter { !it.isCorrect }

            assertEquals(golden, correctOption.breed)
            assertFalse(incorrectOptions.map { it.breed }.contains(golden))

            unmockkObject(BreedMapper)
        }

    // -------------------------------------------------------------------------
    // 2. Ensure correct breed is not included in incorrect list
    // -------------------------------------------------------------------------

    @Test
    fun `Given correct breed, When options generated, Then incorrect options must not contain correct breed`() =
        runTest(dispatcher) {
            // Given
            mockkObject(BreedMapper)
            every { BreedMapper.mapImageUrlToBreed(imageUrl) } returns golden

            // When
            val result = useCase(numOptions = 3)

            // Then
            assertTrue(result.options.none { !it.isCorrect && it.breed == golden })

            unmockkObject(BreedMapper)
        }

    // -------------------------------------------------------------------------
    // 3. When BreedMapper returns null → Unknown Breed
    // -------------------------------------------------------------------------

    @Test
    fun `Given BreedMapper returns null, When invoked, Then correct option uses Unknown Breed`() =
        runTest(dispatcher) {
            // Given
            val unknown = Breed("Unknown Breed")
            mockkObject(BreedMapper)
            every { BreedMapper.mapImageUrlToBreed(imageUrl) } returns null

            // When
            val result = useCase(numOptions = 3)

            // Then
            val correct = result.options.first { it.isCorrect }
            assertEquals(unknown.breed, correct.breed.breed)

            unmockkObject(BreedMapper)
        }

    // -------------------------------------------------------------------------
    // 4. Ensure correct # of options is returned
    // -------------------------------------------------------------------------

    @Test
    fun `Given numOptions, When invoked, Then exactly numOptions options returned`() =
        runTest(dispatcher) {
            // Given
            mockkObject(BreedMapper)
            every { BreedMapper.mapImageUrlToBreed(imageUrl) } returns golden

            // When
            val result = useCase(numOptions = 4)

            // Then
            assertEquals(4, result.options.size)

            unmockkObject(BreedMapper)
        }
}
