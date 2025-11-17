package com.frenzelts.dogguesser.data.remote.model

data class BaseResponse<T>(val status: String, val message: T)