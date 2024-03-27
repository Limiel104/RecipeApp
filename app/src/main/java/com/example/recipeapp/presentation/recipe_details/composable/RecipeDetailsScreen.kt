package com.example.recipeapp.presentation.recipe_details.composable

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun RecipeDetailsScreen(
    navController: NavController
) {
    var secondaryTabState by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    val tabTitleList = listOf("Ingredients", "Description")

    RecipeDetailsContent(
        secondaryTabState = secondaryTabState,
        scrollState = scrollState,
        tabTitleList = tabTitleList,
        onTabChanged = { secondaryTabState = if(secondaryTabState == 0) 1 else 0 }
    )
}