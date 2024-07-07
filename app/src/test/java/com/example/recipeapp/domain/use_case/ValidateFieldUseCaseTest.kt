package com.example.recipeapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateFieldUseCaseTest {

    private lateinit var validateFieldUseCase: ValidateFieldUseCase

    @Before
    fun setUp() {
        validateFieldUseCase = ValidateFieldUseCase()
    }

    @Test
    fun `validate field is correct`() {
        val field = "field text"

        val result = validateFieldUseCase(field)

        assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun `validate field is blank and correct error is returned`() {
        val field = ""

        val result = validateFieldUseCase(field)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Field can't be empty")
    }

    @Test
    fun `validate field is too short and correct error is returned`() {
        val field = "fie"

        val result = validateFieldUseCase(field)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Field is too short")
    }

    @Test
    fun `validate field does not have at least one special char and correct error is returned`() {
        val field = "field_text"

        val result = validateFieldUseCase(field)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("At least one character is not allowed")
    }
}