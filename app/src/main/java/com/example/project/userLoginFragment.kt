package com.example.project

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class userLoginFragment : Fragment() {
    lateinit var userName: String
    lateinit var userPW: String
    lateinit var storedName: String
    lateinit var storedPW: String
    lateinit var actUserSharedPref: SharedPreferences
    lateinit var allUserSharedPreferences: SharedPreferences
    private val TAG = "userFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //VPC - Inflate the layout for this fragment
        val loginView = inflater.inflate(R.layout.fragment_user_login, container, false)

        //VPC - checking if the user clicks login button
        loginView.findViewById<Button>(R.id.button_login).setOnClickListener(){
            val editTextObjUser = loginView.findViewById<EditText>(R.id.editText_userName)
            val editTextObjPW = loginView.findViewById<EditText>(R.id.editText_password)
            userName = editTextObjUser.text.toString()
            Log.d(TAG, "userName = $userName")
            userPW = editTextObjPW.text.toString()
            Log.d(TAG, "userName = $userPW")
            //clear fields and hide keyboards
            editTextObjUser.hideKeyboard()
            editTextObjPW.hideKeyboard()
            editTextObjUser.text = null
            editTextObjPW.text = null

            // Get name and password of the current logged in user or null if none
            getActiveUser()

            //VPC - check if the userName is available
            if (userName != ""){
                // checkForExistingUser returns true if user exists else false
                if (checkForExistingUser()){
                    // if user PW on new user is null toast that they must enter a PW
                    if(userPW == ""){
                        Toast.makeText(activity, R.string.missingUserPW, Toast.LENGTH_SHORT).show()
                    }
                    //user entered a password
                    else{

                        //check PW entered against PW in shared preferences.
                        if (userPW == storedPW){ //storedPW is the wrong PW, need to get from a match in the allusers SP
                            // Set the active user as the user from shared prefs.

                            //for now just testing with a display of the user and PW entered
                            val checktext = loginView.findViewById<TextView>(R.id.textView_test)
                            checktext.text = "$userName $userPW"

                            //uncomment this line to close the fragment once the code above is implemented
                            parentFragmentManager.popBackStack()
                        }
                        else{
                            Toast.makeText(activity, R.string.wrongPW, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                // if the user name does not exist in sharedprefs,
                // popup to ask user if they would like to add user
                else{
                    // assume we will add the new user name for now until we implement pop-up for user confirmation
                    createNewUser()
                    Toast.makeText(activity, R.string.userCreated, Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }

            }
            else {
                //VPC - this isn't working, Should check with Dr. Albayram maybe need to import android.widget.Toast
                // into the UserActivity.kt file?
                Toast.makeText(activity, R.string.missingUserName, Toast.LENGTH_SHORT).show()
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

    //VPC - create a new user from the global variables and defaults
    private fun createNewUser(){

        //initializing the shared prefs variable and editor
        allUserSharedPreferences =this.requireActivity().getSharedPreferences(R.string.allUsers.toString(),MODE_PRIVATE)
        val usersEditor = allUserSharedPreferences?.edit()

        //creating an array to store the PW, Netflix enable, and hulu enable as a GSON string
        val userList = ArrayList<String>()
        val userGSON = Gson() // creating a GSON instance

        //might want to if check to see user supplied a PW if not done above
        userList.add(userPW)    // arg 1 = the newly entered user PW
        userList.add("false")   // arg 2 = Netflix enabled bool as string, default false
        userList.add("false")   // arg 3 = Hulu enabled bool as string, default false
        val userJsonString = userGSON.toJson(userList)

        // putting in shared prefs the userName as a key and their PW/config information as a set of data values
        // using the order from the lines above
        usersEditor?.putString(userName, userJsonString) //default new users to be false
        usersEditor?.apply()

    }
    //VPC - need to check the allUsers preferences to see if the user specified is stored
    // return true if user found else false
    private fun checkForExistingUser(): Boolean {
        //These lines deserialze the user data based on their user name
        allUserSharedPreferences =
            this.requireActivity().getSharedPreferences(R.string.allUsers.toString(), MODE_PRIVATE)
        val userConfigsList = allUserSharedPreferences.getString(userName, "")
        val getUsersGson = Gson()
        val stringType = object : TypeToken<List<String>>() {}.type

        return if (userConfigsList != "") { //check that we did find a user match in shared prefs
            val userConfigsStringList =
                getUsersGson.fromJson<List<String>>(userConfigsList, stringType)
            //setting the global var for stored PW to the one found for the user attempting to log in
            storedPW = userConfigsStringList[0]
            true
        } else
            false
    }

        //VPC - setting the active user in shared preferences
    private fun setActiveUser(){
        //
        val actUserEdittext =   actUserSharedPref?.edit()
        actUserEdittext?.clear()                            //take the current active user out
        actUserEdittext?.putString("activeUser",userName )  //put new active user in Sprefs
        actUserEdittext?.putString("activePW",userPW )      // put new active PW in Sprefs
        actUserEdittext?.apply()
    }

    //VPC - getting setting the global variables with user and PW from shared preferences
    private fun getActiveUser(){
        actUserSharedPref = this.requireActivity().getSharedPreferences(R.string.activeUser.toString(), MODE_PRIVATE)
        storedName = actUserSharedPref?.getString("Username of  Subscriber", "").toString()
    }
    private fun View.hideKeyboard(){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken,0)
    }
}
