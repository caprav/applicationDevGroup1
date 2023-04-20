package com.example.project

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class UserActivity : AppCompatActivity() {
    lateinit var activeUser: String
    lateinit var actUserSharedPref: SharedPreferences
    //Ramya, can we load these values for the active user from shared prefs here
    // if no active user maybe there's a way we can default them to false
    var NetflixSet = false  //global variable to say if Netflix is enabled for a user
    var HuluSet = false     //global variable to say if Hulu is enabled for a user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //VPC - referencing the activity views
        val checkboxNetflix = findViewById<CheckBox>(R.id.checkBox_netflix)
        val checkboxHulu = findViewById<CheckBox>(R.id.checkBox_hulu)
        val userText = findViewById<TextView>(R.id.textView_activeUser)

        //VPC -getting the user/settings from Shared preferences on Activity create
        getActiveUser() //sets activeUser from SharedPrefs
        userText.text = activeUser
        checkboxNetflix.isChecked = NetflixSet
        checkboxHulu.isChecked = HuluSet
        //VPC - we will set the global value of Netflix and Hulu as set if the user clicks either
        // checkbox and they are an active user
        checkboxNetflix.setOnClickListener(){
            if(checkActiveUser()){
                NetflixSet = checkboxNetflix.isChecked
                //Ramya - updated shared prefs for user here
            }
        }
        checkboxHulu.setOnClickListener(){
            if(checkActiveUser()){
                HuluSet = checkboxHulu.isChecked
                //Ramya - updated shared prefs for user here
                val sharedPreferences = getPreferences(MODE_PRIVATE)
                activeUser = sharedPreferences?.getString("Username of  Subscriber","").toString()
                userText.text = activeUser
            }
        }
        //VPC - button click to bring up the user login Fragment
        findViewById<Button>(R.id.button_chgUser).setOnClickListener(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_user, userLoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    //VPC - getting setting the global activeUser with from shared preferences
    private fun getActiveUser(){
        actUserSharedPref = getSharedPreferences(R.string.activeUser.toString(), MODE_PRIVATE)
        //set global variable with the active user from shared preferences
        activeUser = actUserSharedPref?.getString("Username of  Subscriber", "").toString()
    }


    private fun getActiveUserSettings(){
        val allUserSharedPreferences = getSharedPreferences(R.string.allUsers.toString(), MODE_PRIVATE)
        //Ramya - Can you check the last 5 minutes of the 03-08-2021 recorded lecture to get the
        // data that was set in the createNewUser function in the userLoginFragment? then test out
        // all of the user logic?
    }
    private fun checkActiveUser(): Boolean{
        return if (activeUser != null){
            true
        } else{
            Toast.makeText(this, R.string.noUser, Toast.LENGTH_SHORT).show()
            false
        }
    }
}