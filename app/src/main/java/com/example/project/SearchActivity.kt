package com.example.project

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    lateinit var userSearchInput: String
    private val TAG = "SearchActivity"
    private var BASE_URL_SEARCH = "https://api.watchmode.com/v1/search/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //VPC - on search click by user, inflate the fragment that will return a recycler
        // View with the search result items
        findViewById<Button>(R.id.button_titleSearch).setOnClickListener(){

            // Get user input search string
            val editTextsearchInput = findViewById<EditText>(R.id.editText_searchInput)
            userSearchInput = editTextsearchInput.text.toString()
            Log.d(TAG, "user searched for: $userSearchInput")
            editTextsearchInput.hideKeyboard()


            // ArrayList Stores List of Movies From API
            val searchList = ArrayList<title_results>()
            // Create Retrofit
            val searchRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_SEARCH)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            //Onclick complete API Call
            val searchMovieAPI = searchRetrofit.create(watchmodeAPISearch::class.java)
            val searchTerm = findViewById<EditText>(R.id.editText_searchInput).text.toString()

            searchMovieAPI.search(API_KEY,"name",searchTerm).enqueue(object: Callback<SearchData> {

                override fun onResponse(call: Call<SearchData>, response: Response<SearchData>) {
                    Log.d(TAG, "onResponse: $response")

                    val body = response.body()
                    if (body == null){
                        Log.w(TAG, "Valid response was not received")
                        return
                    }

                    // The following log messages are just for testing purpose
                    Log.d(TAG, "Movie ID: ${body.title_results.get(0).id}")
                    Log.d(TAG, "Title: ${body.title_results.get(0).name}")
                    Log.d(TAG, "Year: ${body.title_results.get(0).year}")
                    Log.d(TAG, "IMDB_ID: ${body.title_results.get(0).imdb_id}")
                    Log.d(TAG, "Type: ${body.title_results.get(0).type}")


                    // Update the adapter with the new data
                    searchList.addAll(body.title_results)
                    //adapter.notifyDataSetChanged()Log.d(TAG, "onResponse: $response")

                }

                override fun onFailure(call: Call<SearchData>, t: Throwable) {
                    Log.d(TAG, "onFailure : $t")
                }


            })


            //inflate results fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_search, TitleSearchFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    //VPC - very simple function to go back to the main activity on click
    fun backClick(view: View){
        finish()
    }

    private fun View.hideKeyboard(){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken,0)
    }
}