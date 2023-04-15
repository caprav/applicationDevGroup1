package com.example.project

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

    //Ramya, can we load these values for the active user from shared prefs here
    // if no active user maybe there's a way we can default them to false
    var NetflixSet = false //global variable to say if Netflix is enabled for a user
    var HuluSet = false //global variable to say if Hulu is enabled for a user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //VPC - referencing the activity views
        val checkboxNetflix = findViewById<CheckBox>(R.id.checkBox_netflix)
        val checkboxHulu = findViewById<CheckBox>(R.id.checkBox_hulu)
        val userText = findViewById<TextView>(R.id.textView_activeUser)

        //VPC -getting the user/settings from Shared preferences on Activity create
        activeUser = "" //get value from the shared Preferences//textView_activeUser
        userText.text = activeUser
        checkboxNetflix.isChecked = NetflixSet //
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

    private fun checkActiveUser(): Boolean{
        return if (activeUser != null){
            true
        } else{
            Toast.makeText(this, R.string.noUser, Toast.LENGTH_SHORT).show()
            false
        }
    }
}