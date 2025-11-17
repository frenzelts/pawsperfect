package com.frenzelts.dogguesser.domain.mapper

import com.frenzelts.dogguesser.domain.model.Breed
import com.frenzelts.dogguesser.util.TextUtil.capitalize
import java.net.URI

object BreedMapper {
    /**
     * Mapper from image url path to strong type Breed model.
     * Example: /breeds/bulldog-boston/ -> breed: Bulldog, subbreed: Boston
     */
    fun mapImageUrlToBreed(url: String): Breed? {
        try {
            val path = URI(url).path ?: return null
            val pathSegments = path.split("/").filter { it.isNotBlank() }
            val breedsIndex = pathSegments.indexOf("breeds")
            if (breedsIndex >= 0 && breedsIndex + 1 < pathSegments.size) {
                val breedSegment = pathSegments[breedsIndex + 1]
                val parts = breedSegment.split('-', '/').filter { it.isNotBlank() }
                return Breed(
                    breed = parts.firstOrNull()?.capitalize().orEmpty(),
                    subBreed = parts.getOrNull(1)?.capitalize()
                )
            }
        } catch (ignored: Throwable) {
            return null
        }
        return null
    }
}