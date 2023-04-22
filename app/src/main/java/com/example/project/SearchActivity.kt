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

class SearchActivity : AppCompatActivity() {
    lateinit var userSearchInput: String
    private val TAG = "SearchActivity"
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