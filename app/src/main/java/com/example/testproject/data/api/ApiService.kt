package com.example.testproject.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Инициализация необходимых библиотек для работы с сетью и реализация логики запросов к серверу посредством этих бибилотек
 * Библиотеки (Retrofit2, OKHttp3, RxJava2, Gson, Converter-Gson)
 */
class ApiService() {

    val api: RequestMethods

    init {
        val retrofit = createRetrofitt()
        api = retrofit.create(RequestMethods::class.java)
    }

    private fun createRetrofitt(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RequestMethods.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}

