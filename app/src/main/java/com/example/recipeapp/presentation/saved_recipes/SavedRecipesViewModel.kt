package com.example.recipeapp.presentation.saved_recipes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {

    private val _savedRecipesState = mutableStateOf(SavedRecipesState())
    val savedRecipesState: State<SavedRecipesState> = _savedRecipesState

    private val _savedRecipesUiEventChannel = Channel<SavedRecipesUiEvent>()
    val savedRecipesUiEventChannelFlow = _savedRecipesUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "Saved Recipes ViewModel")
        checkIfUserLoggedIn()
    }

    fun onEvent(event: SavedRecipesEvent) {
        when(event) {
            SavedRecipesEvent.OnLogin -> {
                viewModelScope.launch {
                    _savedRecipesUiEventChannel.send(SavedRecipesUiEvent.NavigateToLogin)
                }
            }
            SavedRecipesEvent.OnSignup -> {
                viewModelScope.launch {
                    _savedRecipesUiEventChannel.send(SavedRecipesUiEvent.NavigateToSignup)
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _savedRecipesState.value = savedRecipesState.value.copy(
                isUserLoggedIn = currentUser != null
            )
        }
    }
}