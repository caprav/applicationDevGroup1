package com.example.project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface watchmodeAPI {


    //https://api.watchmode.com/v1/list-titles/?apiKey=HCf7O95g6k5mNTilcYoMoGcNzCrcNa6K0swoBKo6&source_ids=203
    //BASE = //https://api.watchmode.com/v1/list-titles/

    //See Lect 11 slide 40 for formats, and 4-12-21 recorded lecture ~20:00 mark
    @GET(".")
    fun getContent(  @Query("apiKey") keyString: String,
                     @Query("types") title_type: String, // comment line out for original code
                     @Query("page") page_num: Int,
                     @Query("source_ids") sourceIds: Int): Call<contentData> // Netflix source = 203, hulu source = 157
                           // @Query("limit") titlesLimit: Int): Call<Any> //I think this is just for viewing raw payload

}