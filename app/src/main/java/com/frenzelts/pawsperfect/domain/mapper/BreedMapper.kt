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
        return try {
            val path = URI(url).path ?: return null
            val segments = path.split("/").filter { it.isNotBlank() }

            val breedsIndex = segments.indexOf(PATH_SEGMENT_BREED)
            if (breedsIndex < 0) return null

            // segment after "breeds"
            val breedSegment = segments.getOrNull(breedsIndex + 1) ?: return null
            val nextSegment = segments.getOrNull(breedsIndex + 2) // may or may not exist

            // Case 1: Hyphen-based ("breeds/bulldog-boston")
            if (breedSegment.contains("-")) {
                val parts = breedSegment.split('-').filter { it.isNotBlank() }
                return Breed(
                    breed = parts.firstOrNull().orEmpty(),
                    subBreed = parts.getOrNull(1)
                )
            }

            // Case 2: Slash-based ("breeds/bulldog/boston")
            // Use next segment as subbreed if it's not an image file
            if (nextSegment != null && !nextSegment.contains(".")) {
                return Breed(
                    breed = breedSegment,
                    subBreed = nextSegment
                )
            }

            // Case 3: simple breed ("breeds/poodle")
            Breed(breed = breedSegment)

        } catch (ignored: Throwable) {
            null
        }
    }
}