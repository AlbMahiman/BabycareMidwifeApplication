package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

// Adapter for displaying mother items in a list
class MotherAdapter(private val MotherList: ArrayList<MotherItem>, private val listener: MotherList) :
    RecyclerView.Adapter<MotherAdapter.MotherViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView: RecyclerView

    // Create a view holder for the adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mother_element, parent, false)
        return MotherViewHolder(itemView)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: MotherViewHolder, position: Int) {
        val currentItem = MotherList[position]
        val currentNumber = position + 1
        holder.motherName.text = currentItem.motherFullName.toString()
        holder.motherElementId.text = currentNumber.toString()
    }

    // Get the number of items in the list
    override fun getItemCount(): Int {
        return MotherList.size
    }

    // Inner class representing the view holder
    inner class MotherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val motherName: TextView = itemView.findViewById(R.id.txtMotherName)
        val motherElementId: TextView = itemView.findViewById(R.id.txtMotherId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
