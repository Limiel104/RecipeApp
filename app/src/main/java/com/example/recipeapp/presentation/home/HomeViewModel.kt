package com.example.recipeapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
): ViewModel() {

    init {
        getIngredient("1")
    }

    private fun getIngredient(ingredientId: String) {
        viewModelScope.launch {
            ingredientRepository.getIngredient(ingredientId).collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG1",response.data.toString())
                    }
                }
            }
        }
    }
}