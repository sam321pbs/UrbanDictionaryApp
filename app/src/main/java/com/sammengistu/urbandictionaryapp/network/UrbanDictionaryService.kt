package com.sammengistu.urbandictionaryapp.network

import com.sammengistu.urbandictionaryapp.models.DefinitionList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UrbanDictionaryService {

    @Headers("x-rapidapi-host:mashape-community-urban-dictionary.p.rapidapi.com",
        "x-rapidapi-key:49af1b6bc4mshf4dfdeaccaba136p12dd6fjsnee63973120c5")
    @GET("/define")
    fun getDefinitions(@Query("term") term: String): Call<DefinitionList>
}