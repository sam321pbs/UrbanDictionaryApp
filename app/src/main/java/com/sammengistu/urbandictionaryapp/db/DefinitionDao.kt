package com.sammengistu.urbandictionaryapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sammengistu.urbandictionaryapp.models.Definition

@Dao
interface DefinitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(definition: Definition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(definition: List<Definition>)

    @Query("SELECT * FROM definitions WHERE word = :word")
    fun findByTerm(word: String): List<Definition>

    @Query("SELECT * FROM definitions")
    fun getAllDefinitions(): List<Definition>
}