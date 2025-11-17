package com.frenzelts.dogguesser.data.repository

import com.frenzelts.dogguesser.data.remote.api.DogApi
import com.frenzelts.dogguesser.domain.model.Breed
import com.frenzelts.dogguesser.domain.repository.DogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor (
    private val api: DogApi
) : DogRepository {

    override suspend fun getAllBreeds(): List<Breed> = withContext(Dispatchers.IO) {
        val resp = api.getAllBreeds()
        val map = resp.message

        val result = mutableListOf<Breed>()
        for ((breedName, subBreeds) in map) {
            if (subBreeds.isEmpty()) {
                result.add(Breed(breed = breedName))
            } else {
                subBreeds.forEach { sub ->
                    result.add(Breed(breed = breedName, subBreed = sub))
                }
            }
        }
        result
    }

    override suspend fun getRandomImageUrl(): String = withContext(Dispatchers.IO) {
        api.getRandomImage().message
    }

    override suspend fun getRandomImageByBreed(breed: Breed): String = withContext(Dispatchers.IO) {
        val path = breed.apiPath
        api.getRandomImageByBreed(path).message
    }
}
