package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Query
import com.example.recipeapp.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categoryentity")
    fun getCategories(): List<CategoryEntity>
}