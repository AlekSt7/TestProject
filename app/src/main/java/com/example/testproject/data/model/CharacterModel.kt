package com.example.testproject.data.model

import com.google.gson.annotations.SerializedName

/**
 *
 * Модель Data-объекта персонажа, в который будет парсится и конвертироваться информация, получаемая в Json
 *
 */

data class CharacterModel(

    @SerializedName("id")
    private var id: Int = 0, //Id персонажа

    @SerializedName("name")
    private val name: String? = null, //Имя

    @SerializedName("status")
    private val status: String? = null, //Статус ('Alive', 'Dead' or 'unknown')

    @SerializedName("species")
    private val species: String? = null, //Расса

    @SerializedName("type")
    private val type: String? = null, //Тип

    @SerializedName("gender")
    private val gender: String? = null, //пол ('Female', 'Male', 'Genderless' or 'unknown')

    @SerializedName("origin")
    private val origin: LocationModel? = null, //Название или ссылка на место первого появления

    @SerializedName("location")
    private val location: LocationModel? = null, //Название или ссылка на место последнего нахождения

    @SerializedName("image")
    private val image_url: String? = null, //Ссылка на изображение. Все изображения 300x300px

    @SerializedName("episode")
    private val episodes_url: List<String>? = null, //Список эпизодов в которых появляется персонаж

    @SerializedName("url")
    private val url: String? = null, //Ссылка на страничку персонажа

    @SerializedName("created")
    private val created: String? = null //Время, когда персонаж был создан в бд

){

    fun getId(): Int {
        return id
    }

    fun getName(): String? {
        return name
    }

    fun getStatus(): String? {
        return status
    }

    fun getSpecies(): String? {
        return species
    }

    fun getType(): String? {
        return type
    }

    fun getGender(): String? {
        return gender
    }

    fun getOrigin(): LocationModel? {
        return origin
    }

    fun getLocation(): LocationModel? {
        return location
    }

    fun getImage_url(): String? {
        return image_url
    }

    fun getEpisodes_urls(): List<String?>? {
        return episodes_url
    }

    fun getUrl(): String? {
        return url
    }

    fun getCreated(): String? {
        return created
    }

}