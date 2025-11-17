package com.frenzelts.pawsperfect.domain.repository

import com.frenzelts.pawsperfect.domain.model.Breed

interface DogRepository {
    suspend fun getAllBreeds(): List<Breed>
    suspend fun getRandomImageUrl(): String
    suspend fun getRandomImageByBreed(breed: Breed): String
}
