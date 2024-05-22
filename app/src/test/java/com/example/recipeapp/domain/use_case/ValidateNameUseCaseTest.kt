package com.example.recipeapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateNameUseCaseTest {

    private lateinit var validateNameUseCase: ValidateNameUseCase

    @Before
    fun setUp() {
        validateNameUseCase = ValidateNameUseCase()
    }

    @Test
    fun `validate name is correct`() {
        val name = "John Smith"

        val result = validateNameUseCase(name)

        assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun `validate name is blank and correct error is returned`() {
        val name = ""

        val result = validateNameUseCase(name)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Name can't be empty")
    }

    @Test
    fun `validate name is too short and correct error is returned`() {
        val password = "John"

        val result = validateNameUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Name is too short")
    }

    @Test
    fun `validate name has special char and correct error is returned`() {
        val name = "John_Smith"

        val result = validateNameUseCase(name)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("At least one character in name is not allowed")
    }
}