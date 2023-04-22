package com.example.project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface watchmodeAPISearch {


    //https://api.watchmode.com/v1/search/?apiKey=HCf7O95g6k5mNTilcYoMoGcNzCrcNa6K0swoBKo6&source_ids=203
    //BASE = //https://api.watchmode.com/v1/search/

    //See Lect 11 slide 40 for formats, and 4-12-21 recorded lecture ~20:00 mark
    @GET(".")
    fun search(  @Query("apiKey") keyString: String,
                 //Choose From Options (imdb_id, tmdb_person_id, tmdb_movie_id, tmdb_tv_id, name) by defualt probably name
                 @Query("search_field") searchType: String,
                 @Query("search_value") searchVal: String): Call<SearchData>

}