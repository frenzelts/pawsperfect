package com.frenzelts.pawsperfect.domain.model

import com.frenzelts.pawsperfect.util.TextUtil.capitalize

data class Breed(
    val breed: String,
    val subBreed: String? = null
) {
    val displayName: String
        get() = listOfNotNull(
            subBreed?.capitalize(),
            breed.capitalize()
        ).joinToString(" ")

    val apiPath: String
        get() = if (subBreed != null) "$breed/$subBreed" else breed

    override fun toString(): String = displayName
}