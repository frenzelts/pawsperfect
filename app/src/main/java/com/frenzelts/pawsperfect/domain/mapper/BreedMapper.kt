package com.frenzelts.pawsperfect.domain.mapper

import com.frenzelts.pawsperfect.domain.model.Breed
import java.net.URI

object BreedMapper {

    private const val PATH_SEGMENT_BREED = "breeds"

    /**
     * Mapper from image url path to strong type Breed model.
     * Example: /breeds/bulldog-boston/ -> breed: Bulldog, subbreed: Boston
     */
    fun mapImageUrlToBreed(url: String): Breed? {
        try {
            val path = URI(url).path ?: return null
            val pathSegments = path.split("/").filter { it.isNotBlank() }
            val breedsIndex = pathSegments.indexOf(PATH_SEGMENT_BREED)
            if (breedsIndex >= 0 && breedsIndex + 1 < pathSegments.size) {
                val breedSegment = pathSegments[breedsIndex + 1]
                val parts = breedSegment.split('-', '/').filter { it.isNotBlank() }
                return Breed(
                    breed = parts.firstOrNull().orEmpty(),
                    subBreed = parts.getOrNull(1)
                )
            }
        } catch (ignored: Throwable) {
            return null
        }
        return null
    }
}