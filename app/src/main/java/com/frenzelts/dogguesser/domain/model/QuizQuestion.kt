package com.frenzelts.dogguesser.domain.model

data class QuizQuestion(
    val imageUrl: String,
    val options: List<Option>,
) {
    data class Option(
        val breed: Breed,
        val isCorrect: Boolean
    ) {
        companion object {
            fun correctOption(breed: Breed): Option {
                return Option(breed, true)
            }

            fun incorrectOption(breed: Breed): Option {
                return Option(breed, false)
            }
        }
    }
}
