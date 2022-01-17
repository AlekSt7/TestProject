package com.example.testproject.data.model

import com.google.gson.annotations.SerializedName

/**
 *
 * Модель Data-объекта инофрмации о ответе сервера, в который будет парсится и конвертироваться информация, получаемая в Json
 *
 */
data class InfoModel(

    @SerializedName("count")
    private var count: Int = 0, //Размер массива

    @SerializedName("pages")
    private val pages: Int = 0, //Всего страниц

    @SerializedName("next")
    private val next: String? = null, //Ссылка на сл. стр

    @SerializedName("prev")
    private val prev: String? = null, //Ссылка на пред. стр

){

    fun getCount(): Int {
        return count
    }

    fun getPages(): Int {
        return pages
    }

    fun getNext(): String? {
        return next
    }

    fun getPrev(): String? {
        return prev
    }

}