package com.frenzelts.dogguesser.domain.repository

import com.frenzelts.dogguesser.domain.model.Breed

interface DogRepository {
    suspend fun getAllBreeds(): List<Breed>
    suspend fun getRandomImageUrl(): String
    suspend fun getRandomImageByBreed(breed: Breed): String
}
