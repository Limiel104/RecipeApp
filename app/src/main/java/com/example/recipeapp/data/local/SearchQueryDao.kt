package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.data.local.entity.SearchQueryEntity

@Dao
interface SearchQueryDao {

    @Insert
    suspend fun insertSearchQuery(searchQueryEntity: SearchQueryEntity)

    @Query(
        """
            SELECT *
            FROM searchqueryentity
            GROUP BY `query`
            ORDER BY searchQueryId DESC
            LIMIT 15
        """
    )
    suspend fun getRecentSearchQueries(): List<SearchQueryEntity>
}