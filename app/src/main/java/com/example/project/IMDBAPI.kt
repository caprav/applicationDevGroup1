package com.example.project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IMDBAPI {
    //

    //BASE = //https://imdb-api.com/en/API/Name/

    //See Lect 11 slide 40 for formats, and 4-12-21 recorded lecture ~20:00 mark
    @GET("Name/{apiKey}/{id}")
    fun search(@Path("apiKey") apiKey: String,
               @Path("id") id: String): Call<SearchData>

}
