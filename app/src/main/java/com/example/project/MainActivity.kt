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
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private var BASE_URL = "https://api.watchmode.com/v1/list-titles/"
    private val TAG = "MAINActivity"
    //var NetflixSourceID = 203
    //var HuluSourceID = 157
    private var sourceList = listOf(203, 157) //NetflixSourceID = 203; HuluSourceID = 157

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

        // Loop should execute a call for each of the sources to the API netflix(203) and hulu(157)
        for (callSourceId in sourceList){
            getmoviesAPI.getContent(resources.getString(R.string.watchmodeAPIkey), callSourceId) //sourceId previously hardcoded as NetflixSourceID
                .enqueue(object :
                    Callback<contentData> {
                    override fun onResponse(
                        call: Call<contentData>,
                        mainResponse: Response<contentData>
                    ) {
                        Log.d(TAG, "onResponse: $mainResponse")
                        var contentBody = mainResponse.body()

                        if (mainResponse == null) {
                            Log.w(TAG, "Valid response was not received")
                            return
                        }
                        // The following log messages are just for testing purpose
                        Log.d(TAG, "Movie ID: ${contentBody?.titles?.get(0)?.id}")
                        Log.d(TAG, "Title: ${contentBody?.titles?.get(0)?.title}")
                        Log.d(TAG, "Year: ${contentBody?.titles?.get(0)?.year}")
                        Log.d(TAG, "IMDB_ID: ${contentBody?.titles?.get(0)?.imdb_id}")
                        Log.d(TAG, "Type: ${contentBody?.titles?.get(0)?.type}")
                        // Update the adapter with the data from the API call
                        contentBody?.titles?.let { availableList.addAll(it) }

                        //Add the source ID to each record. This is why we cannot just call the API once with both source IDs
                        // we need to know for each record what service streams the title.
                        for (content_title in contentBody?.titles!!) {
                            content_title.sourceID = callSourceId
                        }
                        //following line randomized all info in the array before passing it to the recyclerView.
                        Collections.shuffle(contentBody.titles)
                        availableResultsAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<contentData>, t: Throwable) {
                        Log.d(TAG, "onFailure : $t")
                    }
                })
        }
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
    //VPC - this function will serve to take the input of the availableList and insert records into the DB
    private fun putIntoDB(){

    }
    //VPC - This function will delete all of the records in the DB and should be done on startup or if the user is changed
    private fun clearDatabase(){

    }
}