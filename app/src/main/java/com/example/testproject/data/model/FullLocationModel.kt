package com.example.testproject.data.model

import com.google.gson.annotations.SerializedName

data class FullLocationModel(

    @SerializedName("id")
    private var id: Int = 0,

    @SerializedName("name")
    private var name: String? = null,

    @SerializedName("type")
    private var type: String? = null,

    @SerializedName("dimension")
    private var dimension: String? = null,

    @SerializedName("residents")
    private var residents: List<String>? = null,

    ){

    fun getId(): Int {
        return id
    }

    fun getName(): String? {
        return name
    }

    fun getType(): String? {
        return type
    }

    fun getDimension(): String? {
        return dimension
    }

    fun getResidents(): List<String?>? {
        return residents
    }

}