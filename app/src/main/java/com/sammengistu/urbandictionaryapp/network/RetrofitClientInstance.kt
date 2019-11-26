package com.sammengistu.urbandictionaryapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private const val BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com"
    val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}