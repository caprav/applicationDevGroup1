// Project is to create an app that compares available titles across streaming services
// Authors: Ramya Satyavarapu, Marek Grabowski, Vincent Capra
// For CS-414/507

package com.example.project

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var BASE_URL = "https://api.watchmode.com/v1/list-titles/"
    private val TAG = "MAINActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //VPC - creating an array for available titles
        val availableList = ArrayList<content_title>()
        val availableResultsAdapter = mainRecyclerAdapter(availableList)

        val availableResultsRecyclerView = findViewById<RecyclerView>(R.id.recycler_main_results)
        availableResultsRecyclerView.adapter = availableResultsAdapter
        availableResultsRecyclerView.layoutManager = LinearLayoutManager(this)

        //VPC - creating the retrofit for the main movie list endpoint
        val titlesRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getmoviesAPI = titlesRetrofit.create(watchmodeAPI::class.java)

        getmoviesAPI.getContent(resources.getString(R.string.watchmodeAPIkey),203).enqueue(object:
            Callback<contentData> {
            override fun onResponse(call: Call<contentData>, mainResponse: Response<contentData>) {
                Log.d(TAG, "onResponse: $mainResponse")
                val contentBody = mainResponse.body()

                if (mainResponse == null){
                    Log.w(TAG, "Valid response was not received")
                    return
                }
                // The following log messages are just for testing purpose
                Log.d(TAG, "Movie ID: ${contentBody?.titles?.get(0)?.id}")
                Log.d(TAG, "Title: ${contentBody?.titles?.get(0)?.title}")
                Log.d(TAG, "Year: ${contentBody?.titles?.get(0)?.year}")
                Log.d(TAG, "IMDB_ID: ${contentBody?.titles?.get(0)?.imdb_id}")
                Log.d(TAG, "Type: ${contentBody?.titles?.get(0)?.type}")
                // Update the adapter with the new data

                contentBody?.titles?.let { availableList.addAll(it) }
                availableResultsAdapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<contentData>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }
        })
            //VPC - creating user activity launcher
        val userActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // can use this section to get back info passed from the user Intent
            }
        }

        //VPC - creating search activity launcher
        val searchActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // can use this section to get back info passed from the user Intent
            }
        }

        //VPC - launch UserActivity with click listener
        findViewById<Button>(R.id.button_user).setOnClickListener(){
            val userIntent = Intent(this, UserActivity::class.java)
            userActivityLauncher.launch(userIntent)
        }

        //VPC - launch SearchActivity with click listener
        findViewById<ImageButton>(R.id.button_search).setOnClickListener(){
            val searchIntent = Intent(this, SearchActivity::class.java)
            searchActivityLauncher.launch(searchIntent)
        }
    }//end OnCreate

}