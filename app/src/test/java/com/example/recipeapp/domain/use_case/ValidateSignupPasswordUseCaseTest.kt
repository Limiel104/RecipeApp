package com.example.recipeapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import io.mockk.impl.annotations.MockK

import org.junit.Before
import org.junit.Test

class ValidateSignupPasswordUseCaseTest {

    @MockK
    private lateinit var validateSignupPasswordUseCase: ValidateSignupPasswordUseCase

    @Before
    fun setUp() {
        validateSignupPasswordUseCase = ValidateSignupPasswordUseCase()
    }

    @Test
    fun `validate password is correct`() {
        val password = "Qwerty1+"

        val result = validateSignupPasswordUseCase(password)

        assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun `validate password is blank and correct error is returned`() {
        val password = ""

        val result = validateSignupPasswordUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Password can't be empty")
    }

    @Test
    fun `validate password is too short and correct error is returned`() {
        val password = "Qwert"

        val result = validateSignupPasswordUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Password is too short")
    }

    @Test
    fun `validate password does not have at least one digit and correct error is returned`() {
        val password = "Qwerty++"

        val result = validateSignupPasswordUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Password should have at least one digit")
    }

    @Test
    fun  `validate password does not have at least one capital letter and correct error is returned`() {
        val password = "qwerty1+"

        val result = validateSignupPasswordUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Password should have at least one capital letter")
    }

    @Test
    fun `validate password does not have at least one special char and correct error is returned`() {
        val password = "Qwerty11"

        val result = validateSignupPasswordUseCase(password)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Password should have at least one special character")
    }
}