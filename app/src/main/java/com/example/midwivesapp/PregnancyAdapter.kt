package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class PregnancyAdapter(private val PregnancyList:ArrayList<PregnancyItem>, private val listener: PregnancyList): RecyclerView.Adapter<PregnancyAdapter.PregnancyViewHolder>(){

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView : RecyclerView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PregnancyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pregnancy_element,parent,false)
        return PregnancyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: PregnancyViewHolder, position: Int) {
        val currentItem = PregnancyList[position]
        var currentNumber = position + 1
        holder.pregnancyDate.text = "Pregnancy Date: " + currentItem.date.toString()
        holder.pregnancyNumber.text = "Pregnancy Number: $currentNumber"
        holder.elementId.text = currentNumber.toString()
    }
    override fun getItemCount(): Int {
        return PregnancyList.size
    }

    inner class PregnancyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val pregnancyDate: TextView = itemView.findViewById(R.id.pregnancyDate)
        val pregnancyNumber: TextView = itemView.findViewById(R.id.pregnancyNumber)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}