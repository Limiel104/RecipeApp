package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.entity.SearchSuggestionEntity
import com.example.recipeapp.di.AppModule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SearchSuggestionDaoTest {

    private lateinit var searchSuggestion: SearchSuggestionEntity
    private lateinit var searchSuggestion2: SearchSuggestionEntity
    private lateinit var searchSuggestion3: SearchSuggestionEntity

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RecipeDatabase
    lateinit var dao: SearchSuggestionDao

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = db.searchSuggestionDao

        searchSuggestion = SearchSuggestionEntity(
            searchSuggestionId = 1,
            text = "Search Suggestion Text"
        )

        searchSuggestion2 = SearchSuggestionEntity(
            searchSuggestionId = 2,
            text = "Search Suggestion 2 Text"
        )

        searchSuggestion3 = SearchSuggestionEntity(
            searchSuggestionId = 3,
            text = "Search Suggestion 3 Text"
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun searchSuggestionDao_insertSearchSuggestion_insertOne() {
        runBlocking {
            dao.insertSearchSuggestion(searchSuggestion)
            val result = dao.getSearchSuggestions()

            assertThat(result).hasSize(1)
            assertThat(result[0]).isEqualTo(searchSuggestion)
            assertThat(result[0]).isInstanceOf(SearchSuggestionEntity::class.java)
        }
    }

    @Test
    fun searchSuggestionDao_insertSearchSuggestion_insertThree() {
        runBlocking {
            dao.insertSearchSuggestion(searchSuggestion)
            dao.insertSearchSuggestion(searchSuggestion2)
            dao.insertSearchSuggestion(searchSuggestion3)
            val result = dao.getSearchSuggestions()

            assertThat(result).hasSize(3)
        }
    }

    @Test
    fun searchSuggestionDao_getSearchSuggestions_dbEmpty() {
        runBlocking {
            val result = dao.getSearchSuggestions()

            assertThat(result).isEmpty()
        }
    }
}