package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

// Adapter class for BMI RecyclerView
class BmiAdapter(
    private val BmiList: ArrayList<Bmi>, // List of BMI data
    private val listener: MotherBmi // Listener for item click events
) : RecyclerView.Adapter<BmiAdapter.BmiViewHolder>() {

    // Firebase references
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView: RecyclerView

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bmi_element, parent, false)
        return BmiViewHolder(itemView)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: BmiViewHolder, position: Int) {
        // Get the current BMI data item
        val currentItem = BmiList[position]
        var currentNumber = position + 1

        // Set the values from the data to the ViewHolder's views
        holder.bmiDate.text = currentItem.date.toString()
        holder.bmiVal.text = currentItem.bmi.toString()
        holder.elementId.text = currentNumber.toString()
        holder.bmiStatus.text = currentItem.bmiStatus.toString()
    }

    // Get the item count
    override fun getItemCount(): Int {
        return BmiList.size
    }

    // ViewHolder for BMI data
    inner class BmiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Views in the ViewHolder
        val bmiDate: TextView = itemView.findViewById(R.id.bmiDate)
        val bmiVal: TextView = itemView.findViewById(R.id.bmiValue)
        val bmiStatus: TextView = itemView.findViewById(R.id.bmiStatus)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

        init {
            // Set an item click listener for the ViewHolder
            itemView.setOnClickListener(this)
        }

        // Handle item click event
        override fun onClick(p0: View?) {
            // Get the position of the clicked item
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Notify the listener about the item click event
                listener.onItemClick(position)
            }
        }
    }

    // Interface for item click events
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
