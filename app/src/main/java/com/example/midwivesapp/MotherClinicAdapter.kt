package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class MotherClinicAdapter(
    private val ClinicList: ArrayList<MotherClinicItem>,
    private val listener: MotherClinic
) : RecyclerView.Adapter<MotherClinicAdapter.ClinicViewHolder>() {

    // Database and storage references (not used in this code)
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        // Inflate the layout for each clinic item
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.mother_clinic_element, parent, false)
        return ClinicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val currentItem = ClinicList[position]
        var currentNumber = position + 1

        // Set clinic date, purpose, and other information
        holder.clinicDate.text = currentItem.clinicDate.toString()
        holder.clinicName.text = currentItem.purpose.toString()
        holder.elementId.text = currentNumber.toString()
        holder.createdDate.text = currentItem.date.toString()

        // Set the visibility of status indicators based on the clinic status
        if (currentItem.status == "pending") {
            holder.statusPending.visibility = View.VISIBLE
            holder.statusCompleted.visibility = View.GONE
        } else if (currentItem.status == "completed") {
            holder.statusPending.visibility = View.GONE
            holder.statusCompleted.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        // Return the number of clinic items in the list
        return ClinicList.size
    }

    inner class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val clinicDate: TextView = itemView.findViewById(R.id.clinicDate)
        val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        val clinicName: TextView = itemView.findViewById(R.id.purpose)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

        val statusPending: FrameLayout = itemView.findViewById(R.id.statusPending)
        val statusCompleted: FrameLayout = itemView.findViewById(R.id.statusCompleted)

        init {
            // Set an item click listener for each clinic item
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            // Handle item click events here
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
