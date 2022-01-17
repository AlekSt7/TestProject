package com.example.testproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Объект базы данных
 */

@Database(entities = [CharacterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getProvider(): DatabaseQueryProvider

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val instance = INSTANCE
            if(instance != null){
                return instance
            }
            synchronized(this){
                val sameInstance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "characters"
                ).build()
                INSTANCE = sameInstance
                return sameInstance
            }

        }

    }

}