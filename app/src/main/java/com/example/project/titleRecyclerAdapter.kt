package com.example.project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// VPC - note rcvTitles in the row following is a local val not a reference to the data class
class titleRecyclerAdapter(val rcvTitles: ArrayList<title_results>) : RecyclerView.Adapter<titleRecyclerAdapter.titleViewHolder>() {

    // VPC - I am a little uncertain if the View should be delcared in the row below.
    // if there are problems with the RV, then this might have to reference the fragment?
    class titleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        // class to represent a single row of data within our recycler view
        val contentTitle = itemView.findViewById<TextView>(R.id.textView_title)
        val contentYear = itemView.findViewById<TextView>(R.id.textView_year)
        val contentType = itemView.findViewById<TextView>(R.id.textView_contentType)
        val contentPosterImg = itemView.findViewById<ImageView>(R.id.imageView_poster)
        var background = itemView.findViewById<View>(R.id.constraint_titleItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): titleViewHolder {
        val titleView = LayoutInflater.from(parent.context).inflate(R.layout.title_item, parent, false)
        return titleViewHolder(titleView)
    }

    override fun getItemCount(): Int {
        // VPC - setting to s static value for now, but will need to eventually replace with
        // the number of search results returned from our query
        return rcvTitles.count()
    }

    override fun onBindViewHolder(holder: titleViewHolder, position: Int) {
        //get one of the titles in the result set at the passed position
        val currentTitle = rcvTitles[position]
        //assign values to each row in the RecyclerView from the data class object
        holder.contentTitle.text = currentTitle.name
        holder.contentYear.text = currentTitle.year.toString()
        holder.contentType.text = currentTitle.type
        //holder.contentPosterImg = TBD

    //VPC - to implement when we properly store the array data from the DB data set.
        if(currentTitle.source_id == 203) {
            holder.background.setBackgroundColor(Color.parseColor("#E85454")) //FFE85454 original red close to trademark AC1818
        }
        else
            holder.background.setBackgroundColor(Color.parseColor("#61A564")) // original green close to trademark 3DBC70
    }

}