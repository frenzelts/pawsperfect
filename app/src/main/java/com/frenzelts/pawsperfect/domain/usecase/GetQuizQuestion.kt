package com.frenzelts.pawsperfect.domain.usecase

import com.frenzelts.pawsperfect.domain.mapper.BreedMapper
import com.frenzelts.pawsperfect.domain.model.Breed
import com.frenzelts.pawsperfect.domain.model.QuizQuestion
import com.frenzelts.pawsperfect.domain.repository.DogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetQuizQuestion @Inject constructor(
    private val repo: DogRepository
) {
    suspend operator fun invoke(numOptions: Int = 4): QuizQuestion = withContext(Dispatchers.IO) {
        val breeds = repo.getAllBreeds()

        // Fetch random image
        val imageUrl = repo.getRandomImageUrl()

        // Collect correct & incorrect breeds
        val correctBreed = BreedMapper.mapImageUrlToBreed(imageUrl)
            ?: Breed("Unknown Breed")
        val incorrectBreeds = breeds.shuffled().filter { it != correctBreed }.take(numOptions - 1)

        // Map breeds into options
        val correctOption = QuizQuestion.Option.correctOption(correctBreed)
        val incorrectOptions = incorrectBreeds.map { QuizQuestion.Option.incorrectOption(it) }
        val options = (incorrectOptions + correctOption).shuffled()
        
        QuizQuestion(imageUrl = imageUrl, options = options)
    }
}