package com.example.recipeapp.di

import android.app.Application
import androidx.room.Room
import com.example.recipeapp.data.local.RecipeDatabase
import com.example.recipeapp.data.repository.AuthRepositoryImpl
import com.example.recipeapp.data.repository.CategoryRepositoryImpl
import com.example.recipeapp.data.repository.ImageStorageRepositoryImpl
import com.example.recipeapp.data.repository.IngredientRepositoryImpl
import com.example.recipeapp.data.repository.RecipeRepositoryImpl
import com.example.recipeapp.data.repository.SavedRecipeRepositoryImpl
import com.example.recipeapp.data.repository.SearchSuggestionRepositoryImpl
import com.example.recipeapp.data.repository.ShoppingListRepositoryImpl
import com.example.recipeapp.data.repository.UserRepositoryImpl
import com.example.recipeapp.domain.repository.AuthRepository
import com.example.recipeapp.domain.repository.CategoryRepository
import com.example.recipeapp.domain.repository.ImageStorageRepository
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.repository.UserRepository
import com.example.recipeapp.domain.use_case.AddImageUseCase
import com.example.recipeapp.domain.use_case.AddRecipeUseCase
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.AddShoppingListUseCase
import com.example.recipeapp.domain.use_case.AddUserUseCase
import com.example.recipeapp.domain.use_case.DeleteShoppingListUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetShoppingListUseCase
import com.example.recipeapp.domain.use_case.GetUserRecipesUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.domain.use_case.GetUserUseCase
import com.example.recipeapp.domain.use_case.LoginUseCase
import com.example.recipeapp.domain.use_case.LogoutUseCase
import com.example.recipeapp.domain.use_case.SignupUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.domain.use_case.UpdateUserPasswordUseCase
import com.example.recipeapp.domain.use_case.UpdateUserUseCase
import com.example.recipeapp.domain.use_case.ValidateConfirmPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateFieldUseCase
import com.example.recipeapp.domain.use_case.ValidateLoginPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateNameUseCase
import com.example.recipeapp.domain.use_case.ValidateSignupPasswordUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    fun provideRecipeDatabase(application: Application): RecipeDatabase {
        return Room.inMemoryDatabaseBuilder(
            application,
            RecipeDatabase::class.java
        ).build()
    }

    @Provides
    fun provideRecipeRepository(db: RecipeDatabase): RecipeRepository {
        val recipesRef = Firebase.firestore.collection("recipes")
        return RecipeRepositoryImpl(recipesRef, db.recipeDao)
    }

    @Provides
    fun provideIngredientRepository(db: RecipeDatabase): IngredientRepository {
        val ingredientsRef = Firebase.firestore.collection("ingredients")
        return IngredientRepositoryImpl(ingredientsRef, db.ingredientDao)
    }

    @Provides
    fun provideShoppingListRepository(db: RecipeDatabase): ShoppingListRepository {
        val shoppingListsRef = Firebase.firestore.collection("shoppingLists")
        return ShoppingListRepositoryImpl(shoppingListsRef, db.shoppingListDao)
    }

    @Provides
    fun provideSavedRecipeRepository(db: RecipeDatabase): SavedRecipeRepository {
        val savedRecipesRef = Firebase.firestore.collection("savedRecipes")
        return SavedRecipeRepositoryImpl(savedRecipesRef, db.savedRecipeDao)
    }

    @Provides
    @Singleton
    fun provideSearchSuggestionRepository(db: RecipeDatabase): SearchSuggestionRepository {
        return SearchSuggestionRepositoryImpl(db.searchSuggestionDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: RecipeDatabase): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        val usersRef = Firebase.firestore.collection("users")
        return UserRepositoryImpl(usersRef)
    }

    @Provides
    @Singleton
    fun provideImageStorageRepository(): ImageStorageRepository {
        val storage = Firebase.storage.reference
        return ImageStorageRepositoryImpl(storage)
    }

    @Provides
    @Singleton
    fun provideGetIngredientsUseCase(ingredientRepository: IngredientRepository): GetIngredientsUseCase {
        return GetIngredientsUseCase(ingredientRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipesUseCase(recipeRepository: RecipeRepository): GetRecipesUseCase {
        return GetRecipesUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserShoppingListsUseCase(shoppingListRepository: ShoppingListRepository): GetUserShoppingListsUseCase {
        return GetUserShoppingListsUseCase(shoppingListRepository)
    }

    @Provides
    @Singleton
    fun provideAddSearchSuggestionUseCase(searchSuggestionRepository: SearchSuggestionRepository): AddSearchSuggestionUseCase {
        return AddSearchSuggestionUseCase(searchSuggestionRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchSuggestionsUseCase(searchSuggestionRepository: SearchSuggestionRepository): GetSearchSuggestionsUseCase {
        return GetSearchSuggestionsUseCase(searchSuggestionRepository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(categoryRepository: CategoryRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(categoryRepository)
    }

    @Provides
    @Singleton
    fun provideAddUserUseCase(userRepository: UserRepository): AddUserUseCase {
        return AddUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(userRepository: UserRepository): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAddImageUseCase(imageStorageRepository: ImageStorageRepository): AddImageUseCase {
        return AddImageUseCase(imageStorageRepository)
    }

    @Provides
    @Singleton
    fun provideAddRecipeUseCase(recipeRepository: RecipeRepository): AddRecipeUseCase {
        return AddRecipeUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserRecipesUseCase(recipeRepository: RecipeRepository): GetUserRecipesUseCase {
        return GetUserRecipesUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideSortRecipesUseCase(): SortRecipesUseCase {
        return SortRecipesUseCase()
    }

    @Provides
    @Singleton
    fun provideUpdateUserPasswordUseCase(authRepository: AuthRepository): UpdateUserPasswordUseCase {
        return UpdateUserPasswordUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAddShoppingListUseCase(shoppingListRepository: ShoppingListRepository): AddShoppingListUseCase {
        return AddShoppingListUseCase(shoppingListRepository)
    }

    @Provides
    @Singleton
    fun provideGetShoppingListUseCase(shoppingListRepository: ShoppingListRepository): GetShoppingListUseCase {
        return GetShoppingListUseCase(shoppingListRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteShoppingListUseCase(shoppingListRepository: ShoppingListRepository): DeleteShoppingListUseCase {
        return DeleteShoppingListUseCase(shoppingListRepository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignupUseCase(authRepository: AuthRepository): SignupUseCase {
        return SignupUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateLoginPasswordUseCase(): ValidateLoginPasswordUseCase {
        return ValidateLoginPasswordUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateSignupPasswordUseCase(): ValidateSignupPasswordUseCase {
        return ValidateSignupPasswordUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateConfirmPasswordUseCase(): ValidateConfirmPasswordUseCase {
        return ValidateConfirmPasswordUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateNameUseCase(): ValidateNameUseCase {
        return ValidateNameUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateFieldUseCase(): ValidateFieldUseCase {
        return ValidateFieldUseCase()
    }
}