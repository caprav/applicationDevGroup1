// Project is to create an app that compares available titles across streaming services
// Authors: Ramya Satyavarapu, Marek Grabowski, Vincent Capra
// For CS-414/507

package com.example.project

import android.annotation.SuppressLint
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
import androidx.room.Room
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private var BASE_URL = "https://api.watchmode.com/v1/list-titles/"
    private val TAG = "MAINActivity"
    //var NetflixSourceID = 203
    //var HuluSourceID = 157
    private var sourceList = listOf(203, 157) //NetflixSourceID = 203; HuluSourceID = 157
    lateinit var db: TitleRoomDB //global var db for type room db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //VPC - creating an array for available titles
        val availableList = ArrayList<content_title>()
        val availableResultsAdapter = mainRecyclerAdapter(availableList)

        //VPC - building the database to store all titles we pull down
        db = Room.databaseBuilder(
            applicationContext, TitleRoomDB::class.java, "titles.db"
        ).build()

        //VPC - This will wipe out persisted data in the db from the last run of the application. We always
        // want to get the latest data from the APIs
        clearDatabase()

        //VPC - creating the recycler layout
        val availableResultsRecyclerView = findViewById<RecyclerView>(R.id.recycler_main_results)
        availableResultsRecyclerView.adapter = availableResultsAdapter
        availableResultsRecyclerView.layoutManager = LinearLayoutManager(this)

        //VPC - creating the retrofit for the main movie list endpoint
        val titlesRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getmoviesAPI = titlesRetrofit.create(watchmodeAPI::class.java)

        // VPC - Loop should execute a call for each of the sources to the API netflix(203) and hulu(157)
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
                        // MG - The following log messages are just for testing purpose
                        Log.d(TAG, "Movie ID: ${contentBody?.titles?.get(0)?.id}")
                        Log.d(TAG, "Title: ${contentBody?.titles?.get(0)?.title}")
                        Log.d(TAG, "Year: ${contentBody?.titles?.get(0)?.year}")
                        Log.d(TAG, "IMDB_ID: ${contentBody?.titles?.get(0)?.imdb_id}")
                        Log.d(TAG, "Type: ${contentBody?.titles?.get(0)?.type}")
                        Log.d(TAG, "call source: $callSourceId    size of content body:$")
                        // Update the adapter with the data from the API call
                        contentBody?.titles?.let { availableList.addAll(it) }

                        //VPC - Add the source ID to each record. This is why we cannot just call the API once with both source IDs
                        // we need to know for each record what service streams the title.
                        for (content_title in contentBody?.titles!!) {
                            content_title.sourceID = callSourceId
                        }

                        //VPC - Here we need to call putIntoDB once it is implemented so we can cross reference
                        // these records in the search functionality.
                        putIntoDB(contentBody.titles)

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
    //VPC - this function will serve to take the input of the contentBody response from
    // the API and insert records into the DB AFTER we added the source ID
    @SuppressLint("SuspiciousIndentation")
    private fun putIntoDB(arrayListContent: List<content_title>){
        var temptitleDBEntity: TitleDBEntity
        for(content_title in arrayListContent){
            Thread {
                Log.d(TAG, "Populating records into the DB")

                for(content_title in arrayListContent){
                //putting the data from one data class into the other
                temptitleDBEntity = TitleDBEntity(
                    content_title.sourceID,
                    content_title.id,
                    content_title.title,
                    content_title.year,
                    content_title.imdb_id,
                    content_title.type
                )
                    db.titleDAO().insertTitle(temptitleDBEntity)
                }

                runOnUiThread {
                    // Do your UI operations
                }
            }.start()
        }
    }
    //VPC - This function will delete all of the records in the DB and should be done on startup or if the user is changed
    private fun clearDatabase(){

        Thread {
            Log.d(TAG, "Wiping the title database")
            //The implementation of this was thanks to:
            // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
            db.titleDAO().clearTitleTable()
            // We cannot call showDialog from a non-UI thread, instead we can call it from a runOnUiThread to access our views
            runOnUiThread {
                // Do your UI operations
            }
        }.start()
    }
}