package com.example.project

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    lateinit var userSearchInput: String
    //lateinit var viewModel: titleViewModel //maybe delete this
    private val TAG = "SearchActivity"
    private var BASE_URL_SEARCH = "https://api.watchmode.com/v1/search/"
    private var BASE_URL_IMDB = "https://imdb-api.com/en/API/"
    lateinit var db: TitleRoomDB
    lateinit var returnRec: TitleDBEntity
    private var displayTitles =  ArrayList<title_results>()
    lateinit var resultsAdapter: titleRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //VPC - on search click by user, inflate the fragment that will return a recycler
        // View with the search result items
        db = TitleRoomDB.getInstance(this)

        findViewById<Button>(R.id.button_titleSearch).setOnClickListener() {

            // Get user input search string
            val editTextsearchInput = findViewById<EditText>(R.id.editText_searchInput)
            userSearchInput = editTextsearchInput.text.toString()
            Log.d(TAG, "user searched for: $userSearchInput")
            editTextsearchInput.hideKeyboard()

            // creating an array of titles and adapter for the RecyclerView to use
            //MG - ArrayList Stores List of Movies From API
            val searchList = ArrayList<title_results>()
            val searchListPerson = ArrayList<people_results>()
            //val titlesData = ArrayList<title_results>() // redundantly created
            resultsAdapter = titleRecyclerAdapter(displayTitles) //(searchList)
            var TopPerson = "test"
            // Create Retrofit
            val searchRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_SEARCH)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val resultsRecyclerView = findViewById<RecyclerView>(R.id.recycler_temp_results)
            resultsRecyclerView.adapter = resultsAdapter
            resultsRecyclerView.layoutManager = LinearLayoutManager(this)
            //add vertical divider to the recyclerview
            val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            resultsRecyclerView.addItemDecoration(dividerItemDecoration)

            val searchMovieAPI = searchRetrofit.create(watchmodeAPISearch::class.java)

            //MG - Onclick complete API Call
            val searchTerm =
                findViewById<EditText>(R.id.editText_searchInput).text.toString() //redundant with userSearchInput
            //HCf7O95g6k5mNTilcYoMoGcNzCrcNa6K0swoBKo6 // R.string.APIkey.toString()
            searchMovieAPI.search(resources.getString(R.string.watchmodeAPIkey), "name", searchTerm)
                .enqueue(object : Callback<SearchData> {
                    override fun onResponse(call: Call<SearchData>, response: Response<SearchData>) {
                        Log.d(TAG, "onResponse: $response")

                        val body = response.body()
                        if (body == null) {
                            Log.w(TAG, "Valid response was not received")
                            return
                        }

                        // The following log messages are just for testing purpose
                        if (body.title_results.isNotEmpty()) {
                            Log.d(TAG, "Movie ID: ${body.title_results.get(0).id}")
                            Log.d(TAG, "Title: ${body.title_results.get(0).name}")
                            Log.d(TAG, "Year: ${body.title_results.get(0).year}")
                            Log.d(TAG, "IMDB_ID: ${body.title_results.get(0).imdb_id}")
                            Log.d(TAG, "Type: ${body.title_results.get(0).type}")
                            searchList.addAll(body.title_results)
                        } else {
                            Log.w(TAG, "Title results list is empty")
                        }
                        if (body.people_results.isNotEmpty()) {
                            Log.d(TAG, "Actor Name: ${body.people_results.get(0).name}")
                            Log.d(TAG, "Actor IMDB_ID: ${body.people_results.get(0).imdb_id}")
                            Log.d(TAG, "Profession: ${body.people_results.get(0).main_profession}")
                            TopPerson = body.people_results.get(0).imdb_id
                            searchListPerson.addAll(body.people_results)

                            // MG -Create Retrofit for the second API call
                            val IMDBsearchRetrofit = Retrofit.Builder()
                                .baseUrl(BASE_URL_IMDB)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            // MG - Call the second API
                            if(TopPerson != "") {
                                val searchIMDBAPI = IMDBsearchRetrofit.create(IMDBAPI::class.java)
                                searchIMDBAPI.search(
                                    resources.getString(R.string.IMDBAPIkey),
                                    TopPerson
                                ).enqueue(object : Callback<SearchData> {

                                    override fun onResponse(
                                        call: Call<SearchData>,
                                        response: Response<SearchData>
                                    ) {
                                        Log.d(TAG, "onResponse: $response")

                                        val body = response.body()
                                        if (body == null) {
                                            Log.w(TAG, "Valid response was not received")
                                            return
                                        }
                                        //Log.w(TAG, "This is the response ${IMDBbody.knownFor.get(0)}")
                                        // The following log messages are just for testing purpose
                                        if (body.castMovies.isNotEmpty()) {
                                            /*Log.d(TAG, "Person ID: ${body.knownFor.get(0).id}")
                                            Log.d(TAG, "Name: ${body.knownFor.get(0).title}")
                                            Log.d(TAG, "Role: ${body.knownFor.get(0).fullTitle}")*/

                                            // VPC - Adding ONLY titles to the list if the role of the person is Actor OR director
                                            // AND the title is in the DB (which implies it is available on either Hulu or Netflix)
                                            for (castMovies in body.castMovies) {
                                                if (castMovies.role == "Actor" || castMovies.role == "Director" || castMovies.role == "Actress") {
                                                    //Calling function and adding matching records
                                                    checkDBforTitle(castMovies.id)
                                                }
                                            }
                                            //VPC - This should update the recycler adapter once the array is build from the
                                            // checking of the DB records
                                            resultsAdapter.notifyDataSetChanged()
                                        }
                                        else {
                                            Log.w(TAG, "IMDB results list is empty")
                                        }

                                    }

                                    override fun onFailure(
                                        call: Call<SearchData>,
                                        t: Throwable
                                    ) {
                                        Log.d(TAG, "onFailure : $t")
                                    }
                                })
                            }
                        } else {
                            Log.w(TAG, "People results list is empty")
                        }

                        //MG - Update the adapter with the new data
                        resultsAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<SearchData>, t: Throwable) {
                        Log.d(TAG, "onFailure : $t")
                    }
                })

            //MG -  Update the adapter with the new data
            resultsAdapter.notifyDataSetChanged()
        }
    }

    //VPC - very simple function to go back to the main activity on click
    fun backClick(view: View){
        finish()
    }
    //VPC - takes a title ID string in the format 'tt0356910' and if it finds a record, adds the record to
    // the recycler view array list
    private fun checkDBforTitle(searchString: String){
        Thread {

            val row = db.titleDAO().imdbIDMatchExsists(searchString)
            Log.d(TAG, "checkDBforTitle: $row")
            if(row > 0) {
                returnRec = db.titleDAO().findimdbIDMatch(searchString)
                Log.d(TAG, "checkDBforTitle: $returnRec")
                val tempTitleResultsElement = title_results(
                    returnRec.contentSourceId,
                    returnRec.id,
                    returnRec.title,
                    returnRec.year,
                    returnRec.imdb_id,
                    returnRec.type
                )
                displayTitles.add(tempTitleResultsElement)
                runOnUiThread {
                    //VPC - was seeing multiple rows commenting this out here but keeping above after the
                    // loop for (castMovies in body.castMovies) might work
                    //resultsAdapter.notifyDataSetChanged()
                }
            }
   
        }.start()
    }
    private fun View.hideKeyboard(){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken,0)
    }
}