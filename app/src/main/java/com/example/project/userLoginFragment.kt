package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton

class userLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val loginView = inflater.inflate(R.layout.fragment_user_login, container, false)

        loginView.findViewById<Button>(R.id.button_login).setOnClickListener(){

        }

        //if the user clicks the cancel button we just remove the fragment by popping off stack
        loginView.findViewById<Button>(R.id.button_cancel).setOnClickListener(){
            // modified from discourse here:
            // https://stackoverflow.com/questions/43043936/close-a-fragment-on-button-click-which-is-inside-that-fragment
            parentFragmentManager.popBackStack() //
        }

        return loginView
    }
}
