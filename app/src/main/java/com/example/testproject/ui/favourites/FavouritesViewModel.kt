package com.example.testproject.ui.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testproject.App
import com.example.testproject.data.db.CharacterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavouritesViewModel() : ViewModel() {

    var db = App.database
    var dbProvider = db.getProvider()

    private var characters = MutableLiveData<List<CharacterEntity?>>()

    fun getAll(): MutableLiveData<List<CharacterEntity?>> {
        GlobalScope.launch(Dispatchers.IO) {
            characters.postValue(dbProvider?.getAll())
        }
        return characters
    }

    fun delete(id: Int){
        GlobalScope.launch(Dispatchers.IO) {
            dbProvider?.delete(
                dbProvider?.getById(id)
            )
        }
    }

}