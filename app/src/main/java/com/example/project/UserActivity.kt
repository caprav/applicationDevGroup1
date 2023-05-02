package com.example.project

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserActivity : AppCompatActivity() {
    lateinit var activeUser: String
    lateinit var actUserSharedPref: SharedPreferences
    lateinit var allUserSharedPreferences : SharedPreferences

   // val userEditor = allUserSharedPreferences?.edit()

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
        allUserSharedPreferences = getSharedPreferences(R.string.allUsers.toString(), MODE_PRIVATE)
        //VPC -getting the user/settings from Shared preferences on Activity create
        getActiveUser() //sets activeUser from SharedPrefs
        if(activeUser != ""){

            getActiveUserSettings()
        }

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

    @SuppressLint("CommitPrefEdits")
    private fun getActiveUserSettings() {
        val gson = Gson()
        val stringType = object : TypeToken<List<String>>() {}.type
        var usernameSaved = allUserSharedPreferences.getString(activeUser, "")
        val userConfigsStringList =
            gson.fromJson<List<String>>(usernameSaved, stringType)
        // val userEditor = allUserSharedPreferences?.edit()
        // usernameSaved = allUserSharedPreferences.getString(activeUser,"")
        if (usernameSaved != null) {
            NetflixSet = userConfigsStringList[1].toBoolean()
            HuluSet = userConfigsStringList[2].toBoolean()
        }

    }
   private fun netflixUser(){
       val gson2 = Gson()
       val stringType = object : TypeToken<List<String>>() {}.type
       var usernameSaved = allUserSharedPreferences.getString(activeUser,"")
       val userEditor = allUserSharedPreferences?.edit()
       val userConfigsStringList =
           gson2.fromJson<List<String>>(usernameSaved, stringType).toMutableList()
       userConfigsStringList[1] = NetflixSet.toString()
       val userList = ArrayList<String>()
       //userList.add("true")
       val userJsonString = gson2.toJson(userList)
       userEditor?.putString(usernameSaved, userJsonString) //default new users to be false
       userEditor?.apply()
    }
    private fun huluUser(){
        val gson2 = Gson()
        val stringType = object : TypeToken<List<String>>() {}.type
        var usernameSaved = allUserSharedPreferences.getString(activeUser,"")
        val userEditor = allUserSharedPreferences?.edit()
        val userConfigsStringList =
            gson2.fromJson<List<String>>(usernameSaved, stringType).toMutableList()
        userConfigsStringList[2] = HuluSet.toString()
        val userList = ArrayList<String>()
        //userList.add("true")
        val userJsonString = gson2.toJson(userList)
        userEditor?.putString(usernameSaved, userJsonString) //default new users to be false
        userEditor?.apply()

    }

    private fun checkActiveUser(): Boolean{
        return if (activeUser != ""){
            true
        } else{
            Toast.makeText(this, R.string.noUser, Toast.LENGTH_SHORT).show()
            false
        }
    }
}
