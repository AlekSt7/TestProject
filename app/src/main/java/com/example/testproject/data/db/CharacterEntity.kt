package com.example.testproject.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Сущность базы данных, описывает хранящийся в таблице объект
 */

@Entity(tableName = "characters")
class CharacterEntity {

    @PrimaryKey
    var id: Int = 0

    var name: String? = null

}