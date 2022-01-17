package com.example.testproject.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.testproject.App
import com.example.testproject.data.api.ApiService
import com.example.testproject.data.db.CharacterEntity
import com.example.testproject.data.model.ApiModel
import com.example.testproject.data.model.CharacterModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    /**
     * Страницы для пагинации списка
     */
    private var nextPage: Int? = 1
    fun getNextPage(): Int? { return nextPage }

    private var apiModel: MutableLiveData<ApiModel?> = MutableLiveData()
    private var apiService: ApiService = ApiService()
    private var throwable = MediatorLiveData<Throwable>()

    var characters: MutableList<CharacterModel?>? = null //Для хранения загруженных данных в RV

    private val compositeDisposable = CompositeDisposable()

    var db = App.database
    var dbProvider = db.getProvider()

    fun addToFavourite(name: String?, id: Int){
        val character = CharacterEntity()
        character.id = id
        character.name = name
        GlobalScope.launch(Dispatchers.IO) {
            if(dbProvider.getById(id) == null){
                dbProvider.insert(character)
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun fetchCharactersData(page: Int) {

            compositeDisposable.add(apiService.api.getCharatersList(page)
            !!.subscribeOn(Schedulers.io())
            !!.observeOn(AndroidSchedulers.mainThread())
            !!.subscribe({
                apiModel.postValue(it)
                nextPage = getNextPageInt(
                        it?.getInfo()?.getNext()
                    )
            }, {
                throwable.postValue(it)
            })
            )
    }

    private fun getNextPageInt(s: String?): Int{
        if (s != null) {
            return s?.substring(s?.lastIndexOf('=') + 1)!!.toInt()
        } else{
          return -1
        }
    }

    fun getApiModel(): MutableLiveData<ApiModel?> {return apiModel}
    fun getThrowable(): LiveData<Throwable> {return throwable}

    fun isFirstPage(): Boolean{
        return (getNextPage()!! - 1) == 1
    }

    fun isLastPage(): Boolean{
        return !(getNextPage() == -1)
    }

    fun clearThrowable(){ throwable.value = null }

}