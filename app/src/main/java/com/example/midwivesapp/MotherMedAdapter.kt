package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class MotherMedAdapter(private val MedList:ArrayList<MotherMedItem>, private val listener: MotherMed): RecyclerView.Adapter<MotherMedAdapter.MedViewHolder>(){

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView : RecyclerView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.med_element,parent,false)
        return MedViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        val currentItem = MedList[position]
        var currentNumber = position + 1
        holder.medDate.text = currentItem.date.toString()
        holder.medName.text = currentItem.medName.toString()
        holder.elementNum.text = currentNumber.toString()

    }
    override fun getItemCount(): Int {
        return MedList.size
    }

    inner class MedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val medDate: TextView = itemView.findViewById(R.id.medDate)
        val medName: TextView = itemView.findViewById(R.id.medName)
        val elementNum: TextView = itemView.findViewById(R.id.elementId)

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