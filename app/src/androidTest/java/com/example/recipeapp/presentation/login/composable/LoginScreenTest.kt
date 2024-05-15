package com.example.recipeapp.presentation.login.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.presentation.MainActivity
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
class LoginScreenTest {

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
            RecipeAppTheme() { LoginScreen(navController = navController) }
        }
    }

    private fun setScreenState(
        email: String = "",
        emailError: String? = null,
        password: String = "",
        passwordError: String? = null,
        isLoading: Boolean = false
    ) {
        composeRule.activity.setContent {
            RecipeAppTheme() {
                LoginContent(
                    email = email,
                    emailError = emailError,
                    password = password,
                    passwordError = passwordError,
                    isLoading = isLoading,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onLogin = {},
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

        composeRule.onNodeWithTag("Login email TF").isDisplayed()
        composeRule.onNodeWithTag("Password email TF").isDisplayed()

        composeRule.onNodeWithTag("Login button").isDisplayed()
        composeRule.onNodeWithTag("Login button").assertHasClickAction()

        composeRule.onNode(hasText("Don't have an account?")).isDisplayed()
        composeRule.onNode(hasText("Signup")).isDisplayed()
        composeRule.onNode(hasText("Signup")).assertHasClickAction()
    }

    @Test
    fun emailTextField_inputTextIsDisplayedCorrectly() {
        val email = "email@email.com"
        setScreenState(
            email = email
        )

        composeRule.onNodeWithTag("Login email TF").performTextInput(email)
        val emailNode = composeRule.onNodeWithTag("Login email TF").fetchSemanticsNode()
        val textInput = emailNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(email)
    }

    @Test
    fun passwordTextField_inputTextIsDisplayedCorrectly() {
        val password = "Qwerty1+"
        setScreenState(
            password = password
        )

        composeRule.onNodeWithTag("Login password TF").performTextInput(password)
        val passwordNode = composeRule.onNodeWithTag("Login password TF").fetchSemanticsNode()
        val textInput = passwordNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo("••••••••")
    }

    @Test
    fun emailErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(
            emailError = "Email can't be empty"
        )

        val emailNode = composeRule.onNodeWithTag("Login email TF").fetchSemanticsNode()
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

        val passwordNode = composeRule.onNodeWithTag("Login password TF").fetchSemanticsNode()
        val errorLabel = passwordNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = passwordNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Password")
        assertThat(errorValue).isEqualTo("Password can't be empty")
    }
}