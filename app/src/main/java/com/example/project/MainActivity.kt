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

class MainActivity : AppCompatActivity() {
    private val BASE_URL = "https://api.watchmode.com/v1/list-titles/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //VPC - creating user activity launcher
        val userActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // can use this section to get back info passed from the user Intent
            }
        }

        //VPC - creating search activity launcher
        val searchActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
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