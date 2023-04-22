package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TitleSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val searchResultsView =  inflater.inflate(R.layout.fragment_title_search, container, false)
        // all code goes in between these two lines for the recycler view

        return searchResultsView
    }
}