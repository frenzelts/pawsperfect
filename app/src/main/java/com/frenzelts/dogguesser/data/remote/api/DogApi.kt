package com.frenzelts.dogguesser.data.remote.api

import com.frenzelts.dogguesser.data.remote.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): BaseResponse<Map<String, List<String>>>

    @GET("breeds/image/random")
    suspend fun getRandomImage(): BaseResponse<String>

    // Use encoded=true so we can pass "hound/afghan" as path
    @GET("breed/{path}/images/random")
    suspend fun getRandomImageByBreed(@Path(value = "path", encoded = true) path: String): BaseResponse<String>

    // Optional: get multiple images
    @GET("breed/{path}/images")
    suspend fun getImagesByBreed(@Path(value = "path", encoded = true) path: String): BaseResponse<List<String>>
}
