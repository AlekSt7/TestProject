package com.example.testproject

import android.app.Application
import com.example.testproject.data.db.AppDatabase

/**
 * Реализуем singleton объект базы данных, т.к. объект тяжёлый и постоянно создавать его невыгодно
 */

class App : Application() {

    companion object {

        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
    }

}