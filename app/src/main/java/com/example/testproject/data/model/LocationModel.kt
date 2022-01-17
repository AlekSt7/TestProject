package com.example.testproject.data.model

import com.google.gson.annotations.SerializedName

/**
 *
 * Модель Data-объекта локации персонажа, в который будет парсится и конвертироваться информация, получаемая в Json
 *
 */
data class LocationModel(

    @SerializedName("name")
    private var name: String? = null, //Название локации

    @SerializedName("url")
    private var url: String? = null //Ссылка на полную локацию

){

    fun getName(): String? {
        return name
    }

    fun getUrl(): String? {
        return url
    }

}