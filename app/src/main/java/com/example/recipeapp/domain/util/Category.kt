package com.example.recipeapp.domain.util

enum class Category(
    val lowerCase: String
) {
    APPETIZER("Appetizer"),
    BREAKFAST("Breakfast"),
    BEEF("Beef"),
    CHICKEN("Chicken"),
    CURRY("Curry"),
    DESSERT("Dessert"),
    DINNER("Dinner"),
    FISH("Fish"),
    LUNCH("Lunch"),
    PASTA("Pasta"),
    PIZZA("Pizza"),
    PORK("Pork"),
    SALAD("Salad"),
    SALMON("Salmon"),
    SNACK("Snack"),
    SOUP("Soup"),
    STEW("Stew")
}

fun getCategoryNames(): List<String> {
    return listOf(
        Category.APPETIZER.lowerCase,
        Category.BREAKFAST.lowerCase,
        Category.BEEF.lowerCase,
        Category.CHICKEN.lowerCase,
        Category.CURRY.lowerCase,
        Category.DESSERT.lowerCase,
        Category.DINNER.lowerCase,
        Category.FISH.lowerCase,
        Category.LUNCH.lowerCase,
        Category.PASTA.lowerCase,
        Category.PIZZA.lowerCase,
        Category.PORK.lowerCase,
        Category.SALAD.lowerCase,
        Category.SALMON.lowerCase,
        Category.SNACK.lowerCase,
        Category.SOUP.lowerCase,
        Category.STEW.lowerCase
    )
}