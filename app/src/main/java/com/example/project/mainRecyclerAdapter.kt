package com.example.project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView


class mainRecyclerAdapter(val mainTitles: ArrayList<content_title>) : RecyclerView.Adapter<mainRecyclerAdapter.maincontentViewHolder>() {

    // VPC - creating a recyclerView for the main activity
    class maincontentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        // class to represent a single row of data within our recycler view
        val mainTitle = itemView.findViewById<TextView>(R.id.textView_title)
        val mainYear = itemView.findViewById<TextView>(R.id.textView_year)
        val mainType = itemView.findViewById<TextView>(R.id.textView_contentType)
        val mainPosterImg = itemView.findViewById<ImageView>(R.id.imageView_poster)
        //val mainsource = itemView.findViewById<TextView>(R.id.textView_sourceID) // was for test purposes only
        var background = itemView.findViewById<View>(R.id.constraint_titleItem)

        //VPC - Setting the click listener
        init{
            itemView.setOnClickListener{

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): maincontentViewHolder {
        val mainView = LayoutInflater.from(parent.context).inflate(R.layout.title_item, parent, false)
        return maincontentViewHolder(mainView)
    }

    override fun getItemCount(): Int {
        // VPC - setting to s static value for now, but will need to eventually replace with
        // the number of search results returned from our query
        return mainTitles.count()
    }

    override fun onBindViewHolder(holder: maincontentViewHolder, position: Int) {
        //get one of the titles in the result set at the passed position
        val currentTitle = mainTitles[position]
        //assign values to each row in the RecyclerView from the data class object
        holder.mainTitle.text = currentTitle.title
        holder.mainYear.text = currentTitle.year.toString()
        holder.mainType.text = currentTitle.type
        //holder.mainsource.text = currentTitle.sourceID.toString() // was for test purposes only
        //holder.mainPosterImg = TBD

        //VPC - here is how we can set the background
        //203 = NEtflix, 157 = hulu //0xFFAC1818(red) for NETFLIX and FF3DBC70(green) for HULU
        //used information from https://stackoverflow.com/questions/40692214/changing-background-color-of-selected-item-in-recyclerview
        // to implement the changing in background color.
        if(currentTitle.sourceID == 203) {
            holder.background.setBackgroundColor(Color.parseColor("#AC1818"))
        }
        else
            holder.background.setBackgroundColor(Color.parseColor("#3DBC70"))

    }

}