package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient

sealed class ShoppingListEvent {
    data class EnteredIngredient(val ingredient: String): ShoppingListEvent()
    data class EnteredName(val name: String): ShoppingListEvent()
    data class SelectedIngredient(val selectedIngredient: Ingredient): ShoppingListEvent()
    data class OnIngredientClicked(val ingredientId: String): ShoppingListEvent()
    data class SelectedWholeQuantity(val whole: String): ShoppingListEvent()
    data class SelectedDecimalQuantity(val decimal: String): ShoppingListEvent()
    data class SelectedTypeQuantity(val type: String): ShoppingListEvent()
    data class OnCheckBoxToggled(val ingredient: Ingredient): ShoppingListEvent()
    data class SelectedShoppingList(val shoppingListId: String): ShoppingListEvent()
    object OnAddButtonClicked: ShoppingListEvent()
    object OnDropDownMenuExpandChange: ShoppingListEvent()
    object OnAddIngredientsDialogDismiss: ShoppingListEvent()
    object OnAddIngredientsDialogSave: ShoppingListEvent()
    object OnQuantityPickerDismissed: ShoppingListEvent()
    object OnQuantityPickerSaved: ShoppingListEvent()
    object OnAddShoppingList: ShoppingListEvent()
    object OnMenuButtonClicked: ShoppingListEvent()
    object OnMenuDismissed: ShoppingListEvent()
    object OnOpenRenameShoppingListDialog: ShoppingListEvent()
    object OnRenameShoppingListDialogSaved: ShoppingListEvent()
    object OnRenameShoppingListDialogDismissed: ShoppingListEvent()
    object OnDeleteAllIngredients: ShoppingListEvent()
    object OnDeleteShoppingList: ShoppingListEvent()
    object OnOpenOtherShoppingListsMenu: ShoppingListEvent()
    object OnOtherShoppingListsMenuDismissed: ShoppingListEvent()
    object OnLogin: ShoppingListEvent()
    object OnSignup: ShoppingListEvent()
}