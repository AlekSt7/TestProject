package com.example.testproject.ui.character_card

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testproject.data.api.ApiService
import com.example.testproject.data.model.CharacterModel
import com.example.testproject.data.model.FullLocationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterViewModel : ViewModel() {

    private var fullLocationModel: MutableLiveData<FullLocationModel?> = MutableLiveData()
    private var characterModel: MutableLiveData<CharacterModel?> = MutableLiveData()
    private var throwable = MediatorLiveData<Throwable>()

    private var apiService: ApiService = ApiService()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun fetchLocationData(characterModel: CharacterModel?): MutableLiveData<FullLocationModel?>{

        //Получаем полные данные о локации персонажа
        var loc_url: String? = characterModel?.getLocation()?.getUrl()

        if (loc_url != "" && loc_url != " ") {

            loc_url = loc_url?.substring(loc_url.lastIndexOf('/') + 1)

            compositeDisposable.add(apiService.api.getCharaterLocation(Integer.parseInt(loc_url))
            !!.subscribeOn(Schedulers.io())
            !!.observeOn(AndroidSchedulers.mainThread())
            !!.subscribe({
                fullLocationModel.postValue(it)
            }, {
                throwable.postValue(it)
            })
            )
       }

        return fullLocationModel

    }

    fun fetchCharacterData(character_id: Int): MutableLiveData<CharacterModel?>{

        compositeDisposable.add(apiService.api.getCharaterById(character_id)
        !!.subscribeOn(Schedulers.io())
        !!.observeOn(AndroidSchedulers.mainThread())
        !!.subscribe({
            characterModel.postValue(it)
        }, {
            throwable.postValue(it)
        })
        )

        return characterModel

    }

    fun getThrowable(): MediatorLiveData<Throwable>{
        return throwable
    }

}