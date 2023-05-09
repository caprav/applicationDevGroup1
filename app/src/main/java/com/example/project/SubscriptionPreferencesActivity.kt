package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class SubscriptionPreferencesActivity : AppCompatActivity() {

    //Ramya, can we load these values for the active user from shared prefs here
    // if no active user maybe there's a way we can default them to false
    var NetflixSet = false  //global variable to say if Netflix is enabled for a user
    var HuluSet = false     //global variable to say if Hulu is enabled for a user

    private val TAG = "SubscriptionPreferences"

    lateinit var userId: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_preferences)

        val user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "onActivityResult: $user")

        userId = user?.uid.toString()


        findViewById<Button>(R.id.button_pref).setOnClickListener(){


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun checkBoxClick(view: View){
        if (view is CheckBox){
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.checkBox_netflix -> if (checked) NetflixSet = true else NetflixSet = false
                R.id.checkBox_hulu -> if (checked) HuluSet = true else HuluSet = false
            }

        }
        saveSubscriptionChoice()

    }

    private fun saveSubscriptionChoice(){

        // Create an instance of getSharedPreferences for edit
        val sharedPreferences = getSharedPreferences(userId, MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        val gson = Gson()
        val userConfigsStringList = mutableListOf<String>()

        userConfigsStringList.add(NetflixSet.toString())
        userConfigsStringList.add(HuluSet.toString())
        val userJsonString = gson.toJson(userConfigsStringList)

        editor.putString("subs", userJsonString)
        editor.apply()
    }
}