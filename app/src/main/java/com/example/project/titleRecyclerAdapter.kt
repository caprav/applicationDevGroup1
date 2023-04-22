package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class titleRecyclerAdapter : RecyclerView.Adapter<titleRecyclerAdapter.titleViewHolder>() {

    // VPC - I am a little uncertain if the View should be delcared in the row below.
    // if there are problems with the RV, then this might have to reference the fragment?
    class titleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        // class to represent a single row of data within our recycler view
        val contentTitle = itemView.findViewById<TextView>(R.id.textView_title)
        val contentYear = itemView.findViewById<TextView>(R.id.textView_year)
        val contentType = itemView.findViewById<TextView>(R.id.textView_contentType)
        val contentPosterImg = itemView.findViewById<ImageView>(R.id.imageView_poster)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): titleViewHolder {
        val titleView = LayoutInflater.from(parent.context).inflate(R.layout.title_item, parent, false)
        return titleViewHolder(titleView)
    }

    override fun getItemCount(): Int {
        // VPC - setting to s static value for now, but will need to eventually replace with
        // the number of search results returned from our query
        return 20
    }

    override fun onBindViewHolder(holder: titleViewHolder, position: Int) {

    }

}