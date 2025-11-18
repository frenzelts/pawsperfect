package com.frenzelts.pawsperfect

import com.frenzelts.pawsperfect.util.StringUtil.capitalize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringUtilTest {

    // ------------------------------------------------------------
    // 1. Normal lowercase
    // ------------------------------------------------------------
    @Test
    fun `Given lowercase word, When capitalize is called, Then first letter becomes uppercase`() {
        // Given
        val input = "poodle"

        // When
        val result = input.capitalize()

        // Then
        assertEquals("Poodle", result)
    }

    // ------------------------------------------------------------
    // 2. Mixed-case input
    // ------------------------------------------------------------
    @Test
    fun `Given mixed-case word, When capitalize is called, Then only first char is uppercased`() {
        // Given
        val input = "gOlDeN"

        // When
        val result = input.capitalize()

        // Then
        assertEquals("GOlDeN", result)
    }

    // ------------------------------------------------------------
    // 3. Already capitalized
    // ------------------------------------------------------------
    @Test
    fun `Given a word already capitalized, When capitalize is called, Then string remains unchanged`() {
        // Given
        val input = "Beagle"

        // When
        val result = input.capitalize()

        // Then
        assertEquals("Beagle", result)
    }

    // ------------------------------------------------------------
    // 4. Empty string
    // ------------------------------------------------------------
    @Test
    fun `Given an empty string, When capitalize is called, Then result is empty`() {
        // Given
        val input = ""

        // When
        val result = input.capitalize()

        // Then
        assertEquals("", result)
    }

    // ------------------------------------------------------------
    // 5. Starts with non-letter
    // ------------------------------------------------------------
    @Test
    fun `Given a string starting with non-letter, When capitalize is called, Then string is unchanged`() {
        // Given
        val input = "123dog"

        // When
        val result = input.capitalize()

        // Then
        assertEquals("123dog", result)
    }

    // ------------------------------------------------------------
    // 6. Unicode characters (internationalization)
    // ------------------------------------------------------------
    @Test
    fun `Given a word with unicode first letter, When capitalize is called, Then first letter uppercased properly`() {
        // Given
        val input = "áfrica"

        // When
        val result = input.capitalize()

        // Then
        assertEquals("África", result)
    }

    @Test
    fun `Given a word starting with unicode lowercase, When capitalize is called, Then uppercase respects locale`() {
        // Given
        val input = "üser"

        // When
        val result = input.capitalize()

        // Then
        assertEquals("Üser", result)
    }
}
