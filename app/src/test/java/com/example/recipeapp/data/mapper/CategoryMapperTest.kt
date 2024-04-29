package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.CategoryEntity
import com.example.recipeapp.domain.model.Category
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CategoryMapperTest {

    private lateinit var category: Category
    private lateinit var categoryEntity: CategoryEntity

    @Before
    fun setUp() {
        category = Category(
            categoryId = "categoryId",
            imageUrl = "imageUrl"
        )

        categoryEntity = CategoryEntity(
            categoryId = "categoryId",
            imageUrl = "imageUrl"
        )
    }

    @Test
    fun `CategoryEntity can be mapped to Category`() {
        val mappedCategory = categoryEntity.toCategory()

        assertThat(mappedCategory).isEqualTo(category)
    }
}