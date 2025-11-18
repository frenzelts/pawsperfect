package com.frenzelts.pawsperfect

import com.frenzelts.pawsperfect.data.local.ScoreDataStore
import com.frenzelts.pawsperfect.domain.model.Breed
import com.frenzelts.pawsperfect.domain.model.QuizQuestion
import com.frenzelts.pawsperfect.domain.usecase.GetQuizQuestion
import com.frenzelts.pawsperfect.presentation.quiz.QuizUiState
import com.frenzelts.pawsperfect.presentation.quiz.QuizViewModel
import com.frenzelts.pawsperfect.presentation.quiz.ui.OptionLayoutMode
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val getQuizQuestion = mockk<GetQuizQuestion>()
    private val scoreStore = mockk<ScoreDataStore>(relaxed = true)

    private lateinit var viewModel: QuizViewModel

    private val sampleQuestion = QuizQuestion(
        imageUrl = "url",
        options = listOf(
            QuizQuestion.Option.correctOption(
                Breed(breed = "retriever", subBreed = "golden")
            ),
            QuizQuestion.Option.incorrectOption(
                Breed(breed = "poodle")
            )
        )
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        coEvery { getQuizQuestion.invoke() } returns sampleQuestion
        viewModel = QuizViewModel(getQuizQuestion, scoreStore)
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
    }

    // ------------------------------------------------------
    // Load question
    // ------------------------------------------------------

    @Test
    fun `Given quiz loads successfully, When loadQuestion is called, Then quizState becomes Ready`() =
        runTest(dispatcher) {
            // Given
            coEvery { getQuizQuestion.invoke() } returns sampleQuestion

            // When
            viewModel.loadQuestion()
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.quizState is QuizUiState.Ready)
            val state = viewModel.quizState as QuizUiState.Ready
            assertEquals(sampleQuestion, state.question)
        }

    @Test
    fun `Given quiz fails to load, When loadQuestion is called, Then quizState becomes Error`() =
        runTest(dispatcher) {
            // Given
            coEvery { getQuizQuestion.invoke() } throws RuntimeException("error message")

            // When
            viewModel.loadQuestion()
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.quizState is QuizUiState.Error)
            assertEquals("error message", (viewModel.quizState as QuizUiState.Error).message)
        }

    // ------------------------------------------------------
    // Submit & validate answer
    // ------------------------------------------------------

    @Test
    fun `Given Ready state, When submitting a correct answer, Then streak increases and score updated`() =
        runTest(dispatcher) {
            // Given
            viewModel.loadQuestion()
            advanceUntilIdle()
            val correctOption = sampleQuestion.options.first() // isCorrect = true

            // When
            val result = viewModel.submitAnswer(correctOption)

            // Then
            assertTrue(result)
            assertEquals(1, viewModel.streak)
            assertEquals(10, viewModel.score)
        }

    @Test
    fun `Given Ready state with streak, When submitting a correct answer, Then streak increases and score updated`() =
        runTest(dispatcher) {
            // Given
            viewModel.loadQuestion()
            advanceUntilIdle()
            viewModel.streak = 1
            viewModel.score = 10
            val correctOption = sampleQuestion.options.first { it.isCorrect }

            // When
            val result = viewModel.submitAnswer(correctOption)

            // Then
            assertTrue(result)
            assertEquals(2, viewModel.streak)
            assertEquals(22, viewModel.score) // 10 + 10 + 1*2 = 22
        }

    @Test
    fun `Given Ready state with streak, When submitting a wrong answer, Then lives decrease and streak resets`() =
        runTest(dispatcher) {
            // Given
            viewModel.loadQuestion()
            advanceUntilIdle()
            viewModel.streak = 4
            val wrongOption = sampleQuestion.options.last() // isCorrect = false

            // When
            val result = viewModel.submitAnswer(wrongOption)

            // Then
            assertFalse(result)
            assertEquals(2, viewModel.lives)
            assertEquals(0, viewModel.streak)
        }

    @Test
    fun `Given Ready state with highest streak, When reaching more streaks, Then highest streak increases`() =
        runTest(dispatcher) {
            // Given
            viewModel.loadQuestion()
            advanceUntilIdle()
            viewModel.highestStreak = 5
            val correctOption = sampleQuestion.options.first { it.isCorrect }

            // When
            repeat(6) {
                viewModel.submitAnswer(correctOption)
            }

            // Then
            assertEquals(6, viewModel.highestStreak)
            assertEquals(6, viewModel.streak)
        }

    @Test
    fun `Given Ready state with highest streak, When current streak still lower, Then highest streak remains the same`() =
        runTest(dispatcher) {
            // Given
            viewModel.loadQuestion()
            advanceUntilIdle()
            viewModel.highestStreak = 10
            val correctOption = sampleQuestion.options.first { it.isCorrect }

            // When
            repeat(4) {
                viewModel.submitAnswer(correctOption)
            }

            // Then
            assertEquals(10, viewModel.highestStreak)
            assertEquals(4, viewModel.streak)
        }

    @Test
    fun `Given 1 life left, When submitting a wrong answer, Then game ends and highscore is saved`() =
        runTest(dispatcher) {
            // Given
            viewModel.loadQuestion()
            advanceUntilIdle()
            viewModel.lives = 1
            viewModel.score = 40
            viewModel.streak = 1
            viewModel.highestStreak = 3
            val wrongOption = sampleQuestion.options.last()

            // When
            viewModel.submitAnswer(wrongOption)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.isGameOver)
            coVerify { scoreStore.saveHighestScore(40) }
            coVerify { scoreStore.saveHighestStreak(3) }
        }

    // ------------------------------------------------------
    // Reset game
    // ------------------------------------------------------

    @Test
    fun `Given game over, When resetGame is called, Then score streak lives reset and new question loaded`() =
        runTest(dispatcher) {
            // Given
            viewModel.score = 100
            viewModel.streak = 7
            viewModel.lives = 0
            viewModel.isGameOver = true

            // When
            viewModel.resetGame()
            advanceUntilIdle()

            // Then
            assertEquals(0, viewModel.score)
            assertEquals(0, viewModel.streak)
            assertEquals(3, viewModel.lives)
            assertFalse(viewModel.isGameOver)
            assertTrue(viewModel.quizState is QuizUiState.Ready)
        }

    // ------------------------------------------------------
    // Change layout
    // ------------------------------------------------------

    @Test
    fun `Given current layoutMode is GRID, When toggleLayoutMode is called, Then mode switches to LIST`() {
        // Given
        assertEquals(OptionLayoutMode.GRID, viewModel.layoutMode)

        // When
        viewModel.toggleLayoutMode()

        // Then
        assertEquals(OptionLayoutMode.LIST, viewModel.layoutMode)
    }

    @Test
    fun `Given current layoutMode is LIST, When toggleLayoutMode is called, Then mode switches to GRID`() {
        // Given
        viewModel.layoutMode = OptionLayoutMode.LIST

        // When
        viewModel.toggleLayoutMode()

        // Then
        assertEquals(OptionLayoutMode.GRID, viewModel.layoutMode)
    }

    // ------------------------------------------------------
    // Save score & streak
    // ------------------------------------------------------

    @Test
    fun `Given score and streak, When saveHighestScore is called, Then datastore saves values`() =
        runTest(dispatcher) {
            // Given
            viewModel.score = 30
            viewModel.streak = 2
            viewModel.highestStreak = 3

            // When
            viewModel.saveHighestScore()
            advanceUntilIdle()

            // Then
            coVerify { scoreStore.saveHighestScore(30) }
            coVerify { scoreStore.saveHighestStreak(3) }
        }
}
