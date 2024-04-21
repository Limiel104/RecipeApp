package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.data.local.entity.SearchSuggestionEntity

@Dao
interface SearchSuggestionDao {

    @Insert
    suspend fun insertSearchSuggestion(searchSuggestionEntity: SearchSuggestionEntity)

    @Query(
        """
            SELECT *
            FROM searchsuggestionentity
            GROUP BY text
            ORDER BY searchSuggestionId DESC
            LIMIT 15
        """
    )
    suspend fun getSearchSuggestions(): List<SearchSuggestionEntity>
}