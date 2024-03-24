package com.example.recipeapp.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)