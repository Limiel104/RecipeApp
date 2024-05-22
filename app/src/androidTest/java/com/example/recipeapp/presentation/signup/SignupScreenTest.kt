package com.example.recipeapp.presentation.signup

import androidx.activity.compose.setContent
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.signup.composable.SignupContent
import com.example.recipeapp.presentation.signup.composable.SignupScreen
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SignupScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun setScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { SignupScreen(navController = navController) }
        }
    }

    private fun setScreenState(
        email: String = "",
        emailError: String? = null,
        password: String = "",
        passwordError: String? = null,
        confirmPassword: String = "",
        confirmPasswordError: String? = null,
        name: String = "",
        nameError: String? = null,
        isLoading: Boolean = false
    ) {
        composeRule.activity.setContent {
            RecipeAppTheme() {
                SignupContent(
                    email = email,
                    emailError = emailError,
                    password = password,
                    passwordError = passwordError,
                    confirmPassword = confirmPassword,
                    confirmPasswordError = confirmPasswordError,
                    name = name,
                    nameError = nameError,
                    isLoading = isLoading,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onConfirmPasswordChange = {},
                    onNameChange = {},
                    onSignup = {},
                    onGoBack = {}
                )
            }
        }
    }

    @Test
    fun allComponentsAreDisplayed() {
        setScreen()

        composeRule.onNodeWithContentDescription("Back button").isDisplayed()

        composeRule.onNodeWithTag("Signup email TF").isDisplayed()
        composeRule.onNodeWithTag("Signup password TF").isDisplayed()
        composeRule.onNodeWithTag("Signup confirm password TF").isDisplayed()
        composeRule.onNodeWithTag("Signup name TF").isDisplayed()

        composeRule.onNodeWithTag("Signup button").isDisplayed()
        composeRule.onNodeWithTag("Signup button").assertHasClickAction()
    }

    @Test
    fun emailTextField_inputTextIsDisplayedCorrectly() {
        val email = "email@email.com"
        setScreenState(
            email = email
        )

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val textInput = emailNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(email)
    }

    @Test
    fun passwordTextField_inputTextIsDisplayedCorrectly() {
        val password = "Qwerty1+"
        setScreenState(
            password = password
        )

        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(password)
    }

    @Test
    fun confirmPasswordTextField_inputTextIsDisplayedCorrectly() {
        val confirmPassword = "Qwerty1+"
        setScreenState(
            confirmPassword = confirmPassword
        )

        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(confirmPassword)
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val textInput = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(confirmPassword)
    }

    @Test
    fun nameTextField_inputTextIsDisplayedCorrectly() {
        val name = "John Smith"
        setScreenState(
            name = name
        )

        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val textInput = confirmPasswordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(name)
    }

    @Test
    fun emailErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            emailError = "Email can't be empty"
        )

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val errorLabel = emailNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = emailNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Email")
        assertThat(errorValue).isEqualTo("Email can't be empty")
    }

    @Test
    fun passwordErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            passwordError = "Password can't be empty"
        )

        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorLabel = passwordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = passwordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Password")
        assertThat(errorValue).isEqualTo("Password can't be empty")
    }

    @Test
    fun confirmPasswordErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            confirmPasswordError = "Passwords don't mach"
        )

        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val errorLabel = confirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Confirm Password")
        assertThat(errorValue).isEqualTo("Passwords don't mach")
    }

    @Test
    fun nameErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            nameError = "Name can't be empty"
        )

        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val errorLabel = nameNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = nameNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Name")
        assertThat(errorValue).isEqualTo("Name can't be empty")
    }

    @Test
    fun emailErrorTextField_performClickOnButtonWhileEmailTextFieldIsEmpty_errorDisplayedCorrectly() {
        val password = "Qwerty1+"
        val name = "John Smith"
        setScreen()

        val initialEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val initialErrorValue = initialEmailNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()
        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val errorLabel = resultEmailNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Email")
        assertThat(resultErrorValue).isEqualTo("Email can't be empty")
    }

    @Test
    fun passwordErrorTextField_performClickOnButtonWilePasswordTextFieldIsEmpty_errorDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val name = "John Smith"
        setScreen()

        val initialPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val initialErrorValue = initialPasswordNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()
        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorLabel = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Password")
        assertThat(resultErrorValue).isEqualTo("Password can't be empty")
    }

    @Test
    fun confirmPasswordErrorTextField_performClickOnButtonWhileConfirmPasswordTextFieldIsEmpty_errorDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val name = "John Smith"
        setScreen()

        val initialConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val initialErrorValue = initialConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val errorLabel = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Confirm Password")
        assertThat(resultErrorValue).isEqualTo("Passwords don't mach")
    }

    @Test
    fun nameErrorTextField_performClickOnButtonWhileNameTextFieldIsEmpty_errorDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        setScreen()

        val initialNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialErrorValue = initialNameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)

        checkSignupButton()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val errorLabel = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Name")
        assertThat(resultErrorValue).isEqualTo("Name can't be empty")
    }

    @Test
    fun emailErrorTextFields_performClickOnButtonWhileEmailTextFieldHasSpecialCharacters_errorsDisplayedCorrectly() {
        val email = "e!mail@email.com"
        val password = "Qwerty1+"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode =
            composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue =
            confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val errorEmailLabel = resultEmailNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorEmailLabel).isEqualTo("Email")
        assertThat(resultEmailErrorValue).isEqualTo("Email in wrong format")
        assertThat(resultPasswordErrorValue).isNull()
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun passwordErrorTextFields_performClickOnButtonWhilePasswordTextFieldIsTooShort_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwer"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorPasswordLabel = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorPasswordLabel).isEqualTo("Password")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isEqualTo("Password is too short")
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun passwordErrorTextFields_performClickOnButtonWhilePasswordTextFieldDoesNotHaveAtLeastOneDigit_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty++"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorPasswordLabel = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorPasswordLabel).isEqualTo("Password")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isEqualTo("Password should have at least one digit")
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun passwordErrorTextFields_performClickOnButtonWhilePasswordTextFieldDoesNotHaveAtLeastOneCapitalLetter_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "qwerty1+"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorPasswordLabel = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorPasswordLabel).isEqualTo("Password")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isEqualTo("Password should have at least one capital letter")
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun passwordErrorTextFields_performClickOnButtonWhilePasswordTextFieldDoesNotHaveAtLeastOneSpecialCharacter_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty11"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorPasswordLabel = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorPasswordLabel).isEqualTo("Password")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isEqualTo("Password should have at least one special character")
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun confirmPasswordErrorTextFields_performClickOnButtonWhileConfirmPasswordDoesNotMach_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val confirmPassword = "Qwerty11"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(confirmPassword)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val errorConfirmPasswordLabel = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorConfirmPasswordLabel).isEqualTo("Confirm Password")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isNull()
        assertThat(resultConfirmPasswordErrorValue).isEqualTo("Passwords don't mach")
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun nameErrorTextFields_performClickOnButtonWhileNameIsTooShort_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val name = "John"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val errorNameLabel = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorNameLabel).isEqualTo("Name")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isNull()
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isEqualTo("Name is too short")
    }

    @Test
    fun nameErrorTextFields_performClickOnButtonWhileNameHasAtLeastOneSpecialCharacter_errorsDisplayedCorrectly() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val name = "Jo#n Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()

        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val errorNameLabel = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorNameLabel).isEqualTo("Name")
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isNull()
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isEqualTo("At least one character in name is not allowed")
    }

    @Test
    fun errorTextFields_performClickOnButtonWhileAllTextFieldsAreEmpty_errorsDisplayedCorrectly() {
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        checkSignupButton()
        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val errorEmailLabel = resultEmailNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val errorPasswordLabel = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)

        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val errorNameLabel = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(errorEmailLabel).isEqualTo("Email")
        assertThat(errorPasswordLabel).isEqualTo("Password")
        assertThat(errorNameLabel).isEqualTo("Name")
        assertThat(resultEmailErrorValue).isEqualTo("Email can't be empty")
        assertThat(resultPasswordErrorValue).isEqualTo("Password can't be empty")
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isEqualTo("Name can't be empty")
    }

    @Test
    fun errorTextFields_noErrorsAfterClickingOnTheButton() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val name = "John Smith"
        setScreen()

        val emailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val passwordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val confirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val nameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val initialEmailErrorValue = emailNode.config.getOrNull(SemanticsProperties.Error)
        val initialPasswordErrorValue = passwordNode.config.getOrNull(SemanticsProperties.Error)
        val initialConfirmPasswordErrorValue = confirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val initialNameErrorValue = nameNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)

        checkSignupButton()
        val resultEmailNode = composeRule.onNodeWithTag("Signup email TF").fetchSemanticsNode()
        val resultPasswordNode = composeRule.onNodeWithTag("Signup password TF").fetchSemanticsNode()
        val resultConfirmPasswordNode = composeRule.onNodeWithTag("Signup confirm password TF").fetchSemanticsNode()
        val resultNameNode = composeRule.onNodeWithTag("Signup name TF").fetchSemanticsNode()
        val resultEmailErrorValue = resultEmailNode.config.getOrNull(SemanticsProperties.Error)
        val resultPasswordErrorValue = resultPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultConfirmPasswordErrorValue = resultConfirmPasswordNode.config.getOrNull(SemanticsProperties.Error)
        val resultNameErrorValue = resultNameNode.config.getOrNull(SemanticsProperties.Error)

        assertThat(initialEmailErrorValue).isNull()
        assertThat(initialPasswordErrorValue).isNull()
        assertThat(initialConfirmPasswordErrorValue).isNull()
        assertThat(initialNameErrorValue).isNull()
        assertThat(resultEmailErrorValue).isNull()
        assertThat(resultPasswordErrorValue).isNull()
        assertThat(resultConfirmPasswordErrorValue).isNull()
        assertThat(resultNameErrorValue).isNull()
    }

    @Test
    fun circularProgressIndicator_isDisplayedAfterClickingOnTheButtonWhenThereWasNoErrors() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val name = "John Smith"
        setScreen()

        composeRule.onNodeWithTag("Signup CPI").assertDoesNotExist()

        composeRule.onNodeWithTag("Signup email TF").performTextInput(email)
        composeRule.onNodeWithTag("Signup password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup confirm password TF").performTextInput(password)
        composeRule.onNodeWithTag("Signup name TF").performTextInput(name)
        composeRule.onNodeWithTag("Signup button").performClick()

        composeRule.onNodeWithTag("Signup CPI").assertExists()
    }

    private fun checkSignupButton() {
        composeRule.onNodeWithTag("Signup button").assertIsDisplayed()
        composeRule.onNodeWithTag("Signup button").assertIsEnabled()
        composeRule.onNodeWithTag("Signup button").performClick()
    }
}