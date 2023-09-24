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

class BabyBmiAdapter(private val BmiList: ArrayList<BabyBmiItem>, private val listener: BabyBmi) : RecyclerView.Adapter<BabyBmiAdapter.BabyBmiViewHolder>() {

    // Function to create a new ViewHolder when needed.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyBmiViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bmi_element, parent, false)
        return BabyBmiViewHolder(itemView)
    }

    // Function to bind data to a ViewHolder.
    override fun onBindViewHolder(holder: BabyBmiViewHolder, position: Int) {
        val currentItem = BmiList[position]
        var currentNumber = position + 1
        holder.bmiDate.text = currentItem.date.toString()
        holder.bmiVal.text = currentItem.bmi.toString()
        holder.elementId.text = currentNumber.toString()
        holder.bmiStatus.text = currentItem.bmiStatus.toString()
    }

    // Function to get the number of items in the list.
    override fun getItemCount(): Int {
        return BmiList.size
    }

    inner class BabyBmiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val bmiDate: TextView = itemView.findViewById(R.id.bmiDate)
        val bmiVal: TextView = itemView.findViewById(R.id.bmiValue)
        val bmiStatus: TextView = itemView.findViewById(R.id.bmiStatus)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

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

    // Interface for defining item click behavior.
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
