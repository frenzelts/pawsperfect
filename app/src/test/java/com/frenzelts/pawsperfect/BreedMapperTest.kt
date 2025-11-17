package com.frenzelts.pawsperfect

import com.frenzelts.pawsperfect.domain.mapper.BreedMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BreedMapperTest {

    // ------------------------------------------------------------
    // Case 1: breed + sub-breed via hyphen
    // ------------------------------------------------------------
    @Test
    fun `Given an image URL with breed and subbreed, When mapped, Then correct Breed is returned`() {
        // Given
        val url = "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11693.jpg"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNotNull(result)
        assertEquals("bulldog", result?.breed)
        assertEquals("boston", result?.subBreed)
    }

    // ------------------------------------------------------------
    // Case 2: simple breed (no sub-breed)
    // ------------------------------------------------------------
    @Test
    fun `Given an image URL with only breed, When mapped, Then Breed with null subbreed is returned`() {
        // Given
        val url = "https://images.dog.ceo/breeds/poodle/n02113799_6022.jpg"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNotNull(result)
        assertEquals("poodle", result?.breed)
        assertEquals(null, result?.subBreed)
    }

    // ------------------------------------------------------------
    // Case 3: breed path uses slash formatting (edge cases)
    // Example: /breeds/hound/walker/
    // ------------------------------------------------------------
    @Test
    fun `Given breed and subbreed with slash separator, When mapped, Then correct Breed is returned`() {
        // Given
        val url = "https://images.dog.ceo/breeds/hound/walker/n02117401_1002.jpg"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNotNull(result)
        assertEquals("hound", result?.breed)
        assertEquals("walker", result?.subBreed)
    }

    // ------------------------------------------------------------
    // Case 4: no breeds segment → return null
    // ------------------------------------------------------------
    @Test
    fun `Given URL without breeds segment, When mapped, Then returns null`() {
        // Given
        val url = "https://example.com/random/path/image.jpg"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNull(result)
    }

    // ------------------------------------------------------------
    // Case 5: breeds segment is last → no next segment
    // ------------------------------------------------------------
    @Test
    fun `Given URL where breeds is last segment, When mapped, Then returns null`() {
        // Given
        val url = "https://images.dog.ceo/breeds/"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNull(result)
    }

    // ------------------------------------------------------------
    // Case 6: malformed URL → safely returns null
    // ------------------------------------------------------------
    @Test
    fun `Given malformed URL, When mapped, Then returns null`() {
        // Given
        val url = "not-a-valid-url"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNull(result)
    }

    // ------------------------------------------------------------
    // Case 7: breed path includes more parts than expected
    // e.g. bulldog-boston-something → extra ignored
    // ------------------------------------------------------------
    @Test
    fun `Given breed path with extra parts, When mapped, Then only first two are used`() {
        // Given
        val url = "https://images.dog.ceo/breeds/bulldog-boston-extra/n02108422_234.jpg"

        // When
        val result = BreedMapper.mapImageUrlToBreed(url)

        // Then
        assertNotNull(result)
        assertEquals("bulldog", result?.breed)
        assertEquals("boston", result?.subBreed)
    }
}