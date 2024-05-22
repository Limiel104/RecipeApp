package com.example.recipeapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateLoginPasswordUseCaseTest {

    private lateinit var validateLoginPasswordUseCase: ValidateLoginPasswordUseCase

    @Before
    fun setUp() {
        validateLoginPasswordUseCase = ValidateLoginPasswordUseCase()
    }

    @Test
    fun `validate password is not blank`() {
        val password = "Qwerty1+"

        val result = validateLoginPasswordUseCase(password)

        assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun `validate password is blank and correct error is returned`() {
        val password = ""

        val result = validateLoginPasswordUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Password can't be empty")
    }
}