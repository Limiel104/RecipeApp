package com.example.recipeapp.presentation.home.composable

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipeapp.presentation.home.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeContent()
}