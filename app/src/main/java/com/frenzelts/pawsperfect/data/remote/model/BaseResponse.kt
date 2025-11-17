package com.frenzelts.pawsperfect.data.remote.model

data class BaseResponse<T>(val status: String, val message: T)