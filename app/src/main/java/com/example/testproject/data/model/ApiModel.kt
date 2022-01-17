package com.example.testproject.data.model

import com.google.gson.annotations.SerializedName

/**
 *
 * Модель Data-объекта основоного ответа сервера, в который будет парсится и конвертироваться информация, получаемая в Json
 *
 */
data class ApiModel(

    /**Данные о запросе */
    @SerializedName("info")
    private var info: InfoModel? = null,

    /**Персонажи */
    @SerializedName("results")
    private var characterModelList: List<CharacterModel?>? = null

){

    fun getCharacterModelList(): List<CharacterModel?>? {
        return characterModelList
    }

    fun getInfo(): InfoModel? {
        return info
    }

}