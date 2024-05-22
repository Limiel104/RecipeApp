package com.example.recipeapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateConfirmPasswordUseCaseTest {

    private lateinit var validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase

    @Before
    fun setUp() {
        validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase()
    }

    @Test
    fun `validate password and confirmPassword match`() {
        val password = "Qwerty1+"

        val result = validateConfirmPasswordUseCase(password, password)

        assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun `validate password and confirmPassword are not the same and correct errors are returned - confirm password is not empty`() {
        val password = "Qwerty1+"
        val confirmPassword = "Qwerty"

        val result = validateConfirmPasswordUseCase(password, confirmPassword)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Passwords don't mach")

    }

    @Test
    fun `validate password and confirmPassword are not the same and correct errors are returned - confirm password is empty`() {
        val password = "Qwerty1+"
        val confirmPassword = ""

        val result = validateConfirmPasswordUseCase(password, confirmPassword)

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.errorMessage).isEqualTo("Passwords don't mach")
    }
}