package com.frenzelts.pawsperfect

import com.frenzelts.pawsperfect.data.remote.api.DogApi
import com.frenzelts.pawsperfect.data.remote.model.BaseResponse
import com.frenzelts.pawsperfect.data.repository.DogRepositoryImpl
import com.frenzelts.pawsperfect.domain.model.Breed
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DogRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val api = mockk<DogApi>()
    private lateinit var repo: DogRepositoryImpl

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = DogRepositoryImpl(api)
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
    }

    // ----------------------------------------------------------------------
    // getAllBreeds()
    // ----------------------------------------------------------------------

    @Test
    fun `Given API returns breed map with subbreeds, When getAllBreeds is called, Then all Breeds are mapped correctly`() =
        runTest(dispatcher) {
            // Given
            val apiResponse = BaseResponse(
                message = mapOf(
                    "retriever" to listOf("golden", "chesapeake"),
                    "poodle" to emptyList()
                ),
                status = "success"
            )

            coEvery { api.getAllBreeds() } returns apiResponse

            // When
            val result = repo.getAllBreeds()

            // Then
            assertEquals(
                listOf(
                    Breed("retriever", "golden"),
                    Breed("retriever", "chesapeake"),
                    Breed("poodle", null)
                ),
                result
            )
        }

    @Test
    fun `Given API returns empty map, When getAllBreeds is called, Then empty list returned`() =
        runTest(dispatcher) {
            // Given
            coEvery { api.getAllBreeds() } returns BaseResponse(
                message = emptyMap(),
                status = "success"
            )

            // When
            val result = repo.getAllBreeds()

            // Then
            assertTrue(result.isEmpty())
        }

    // ----------------------------------------------------------------------
    // getRandomImageUrl()
    // ----------------------------------------------------------------------

    @Test
    fun `Given API returns random image, When getRandomImageUrl is called, Then the URL is returned`() =
        runTest(dispatcher) {
            // Given
            val url = "https://images.dog.ceo/breeds/poodle/n02113799_6022.jpg"
            coEvery { api.getRandomImage() } returns BaseResponse(
                message = url,
                status = "success"
            )

            // When
            val result = repo.getRandomImageUrl()

            // Then
            assertEquals(url, result)
        }

    // ----------------------------------------------------------------------
    // getRandomImageByBreed()
    // ----------------------------------------------------------------------

    @Test
    fun `Given a breed, When getRandomImageByBreed is called, Then api is called with correct path and URL returned`() =
        runTest(dispatcher) {
            // Given
            val breed = Breed("retriever", "golden")
            val expectedPath = "retriever/golden"
            val expectedUrl = "https://images.dog.ceo/breeds/retriever-golden/n02099601_1000.jpg"

            coEvery { api.getRandomImageByBreed(expectedPath) } returns BaseResponse(
                message = expectedUrl,
                status = "success"
            )

            // When
            val result = repo.getRandomImageByBreed(breed)

            // Then
            assertEquals(expectedUrl, result)
        }
}
