package com.example.project

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class userLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //VPC - Inflate the layout for this fragment
        val loginView = inflater.inflate(R.layout.fragment_user_login, container, false)

        //VPC - checking if the user clicks login button
        loginView.findViewById<Button>(R.id.button_login).setOnClickListener(){
            val editTextObjUser = loginView.findViewById<EditText>(R.id.editText_userName)
            val editTextObjPW = loginView.findViewById<EditText>(R.id.editText_password)
            val userName = editTextObjUser.text.toString()
            val userPW = editTextObjPW.text.toString()

            //clear fields and hide keyboards
            editTextObjUser.hideKeyboard()
            editTextObjPW.hideKeyboard()
            editTextObjUser.text = null
            editTextObjPW.text = null

            //VPC - check if the userName is available
            if (userName != null){
                // here we need to compare the entered userName to ones in Shared Preferences to check for existing users
                // I suggest a while loop to check userName against all entries in Shared prefs and set this flag
                val foundUser = true
                // else check PW entered against PW in shared preferences.
                if (foundUser){
                    // if user PW on new user is null toast that they must enter a PW
                    if(userPW == null){
                        Toast.makeText(getActivity(), R.string.missingUserPW, Toast.LENGTH_SHORT).show()
                    }
                    //user entered a password
                    else{
                        //check PW entered against PW in shared preferences.
                        if (true){ //the userPW == pw stored in Shared Prefs, for now toggling manually for tests
                            // Set the active user as the user from shared prefs.

                            //for now just testing with a display of the user and PW entered
                            val checktext = loginView.findViewById<TextView>(R.id.textView_test)
                            checktext.text = "$userName $userPW"

                            //uncomment this line to close the fragment once the code above is implemented
                            //parentFragmentManager.popBackStack()
                        }
                        else{
                            Toast.makeText(getActivity(), R.string.wrongPW, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                // if the user name does not exist in sharedprefs,
                // popup to ask user if they would like to add user
                else{

                }

            }
            else {
                //VPC - this isn't working, Should check with Dr. Albayram maybe need to import android.widget.Toast
                // into the UserActivity.kt file?
                Toast.makeText(getActivity(), R.string.missingUserName, Toast.LENGTH_SHORT).show()
            }

        }

        //VPC - if the user clicks the cancel button we just remove the fragment by popping off stack
        loginView.findViewById<Button>(R.id.button_cancel).setOnClickListener(){
            // modified from discourse here:
            // https://stackoverflow.com/questions/43043936/close-a-fragment-on-button-click-which-is-inside-that-fragment
            parentFragmentManager.popBackStack() //
        }

        return loginView
    }

    private fun View.hideKeyboard(){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken,0)
    }
}
