package com.example.testproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


/**
 * Реализация запросов к базе данных
 */

@Dao
interface DatabaseQueryProvider {

    @Query("SELECT * FROM characters ORDER BY id ASC")
    suspend fun getAll(): List<CharacterEntity?>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Int): CharacterEntity?

    @Insert
    suspend fun insert(character: CharacterEntity?)

    @Delete
    suspend fun delete(character: CharacterEntity?)

}