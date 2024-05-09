package com.example.recipeapp.presentation.navigation.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.BottomNavigationItem
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RecipeAppNavigation() {

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val items = listOf(
        BottomNavigationItem(
            label = stringResource(id = R.string.home),
            route = Screen.HomeScreen.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),

        BottomNavigationItem(
            label = stringResource(id = R.string.saved),
            route = Screen.SavedRecipesScreen.route,
            selectedIcon = Icons.Filled.Bookmark,
            unselectedIcon = Icons.Outlined.Bookmark
        ),

        BottomNavigationItem(
            label = stringResource(id = R.string.list),
            route = Screen.ShoppingListScreen.route,
            selectedIcon = Icons.AutoMirrored.Rounded.List,
            unselectedIcon = Icons.AutoMirrored.Rounded.List
        ),

        BottomNavigationItem(
            label = stringResource(id = R.string.account),
            route = Screen.AccountScreen.route,
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, bottomNavigationItem ->

                    val selected = bottomNavigationItem.route == navBackStackEntry.value?.destination?.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = { navController.navigate(bottomNavigationItem.route) },
                        label = {
                                Text(
                                    text = bottomNavigationItem.label,
                                    softWrap = false
                                )
                        },
                        icon = {
                            Icon(
                                imageVector = if(selected) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon,
                                contentDescription = "Bottom Nav Item ${bottomNavigationItem.label}"
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(
                navController = navController
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RecipeAppNavigationPreview() {
    RecipeAppTheme {
        RecipeAppNavigation()
    }
}