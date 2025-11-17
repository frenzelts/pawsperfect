package com.frenzelts.dogguesser.domain.model

data class Breed(
    val breed: String,
    val subBreed: String? = null
) {
    val displayName: String
        get() = listOfNotNull(subBreed, breed).joinToString(" ")

    val apiPath: String
        get() = if (subBreed != null) "$breed/$subBreed" else breed

    override fun toString(): String = displayName
}