package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //VPC - button click to bring up the user login Fragment
        findViewById<Button>(R.id.button_chgUser).setOnClickListener(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_user, userLoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}