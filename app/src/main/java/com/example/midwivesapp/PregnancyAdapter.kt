package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class PregnancyAdapter(private val PregnancyList: ArrayList<PregnancyItem>, private val listener: PregnancyList) : RecyclerView.Adapter<PregnancyAdapter.PregnancyViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView: RecyclerView

    // Create a view holder for the pregnancy items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PregnancyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pregnancy_element, parent, false)
        return PregnancyViewHolder(itemView)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: PregnancyViewHolder, position: Int) {
        val currentItem = PregnancyList[position]
        var currentNumber = position + 1

        // Set pregnancy date, number, and element number to the corresponding views
        holder.pregnancyDate.text = "Pregnancy Date: " + currentItem.date.toString()
        holder.pregnancyNumber.text = "Pregnancy Number: $currentNumber"
        holder.elementId.text = currentNumber.toString()
    }

    // Return the number of pregnancy items in the list
    override fun getItemCount(): Int {
        return PregnancyList.size
    }

    inner class PregnancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val pregnancyDate: TextView = itemView.findViewById(R.id.pregnancyDate)
        val pregnancyNumber: TextView = itemView.findViewById(R.id.pregnancyNumber)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Handle item click event by invoking the onItemClick method of the listener
                listener.onItemClick(position)
            }
        }
    }

    // Interface to define item click listener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
