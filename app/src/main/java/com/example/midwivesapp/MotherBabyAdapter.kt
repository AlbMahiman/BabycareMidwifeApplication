package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class MotherBabyAdapter(private val BabyList:ArrayList<Baby>, private val listener: MotherBabyList): RecyclerView.Adapter<MotherBabyAdapter.BabyViewHolder>(){

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView : RecyclerView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.baby_element,parent,false)
        return BabyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        val currentItem = BabyList[position]
        var currentNumber = position + 1
        holder.babyName.text = "Name: " + currentItem.firstName.toString() +" "+ currentItem.lastName.toString()
        holder.elementId.text = currentNumber.toString()
    }
    override fun getItemCount(): Int {
        return BabyList.size
    }

    inner class BabyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val babyName: TextView = itemView.findViewById(R.id.babyName)
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