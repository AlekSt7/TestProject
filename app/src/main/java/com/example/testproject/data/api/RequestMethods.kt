package com.example.testproject.data.api

import com.example.testproject.data.model.ApiModel
import com.example.testproject.data.model.CharacterModel
import com.example.testproject.data.model.FullLocationModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * Описание методов для запросов к серверу
 *
 */
interface RequestMethods {
    @GET("character")
    fun getCharatersList(@Query("page") page: Int): Single<ApiModel?>?

    @GET("character/{id}")
    fun getCharaterById(@Path("id") id: Int): Single<CharacterModel?>?

    @GET("location/{id}")
    fun getCharaterLocation(@Path("id") location_id: Int): Single<FullLocationModel?>?

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}