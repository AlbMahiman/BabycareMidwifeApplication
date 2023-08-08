package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class BabyMedAdapter(private val MedList:ArrayList<BabyMedItem>, private val listener: BabyMed): RecyclerView.Adapter<BabyMedAdapter.BabyMedViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyMedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.med_element,parent,false)
        return BabyMedViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: BabyMedViewHolder, position: Int) {
        val currentItem = MedList[position]
        var currentNumber = position + 1
        holder.medDate.text = currentItem.date.toString()
        holder.medName.text = currentItem.medName.toString()
        holder.elementNum.text = currentNumber.toString()
        holder.instructions.text = currentItem.instruction

    }
    override fun getItemCount(): Int {
        return MedList.size
    }

    inner class BabyMedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val medDate: TextView = itemView.findViewById(R.id.medDate)
        val medName: TextView = itemView.findViewById(R.id.medName)
        val elementNum: TextView = itemView.findViewById(R.id.elementId)
        val instructions:TextView = itemView.findViewById(R.id.instructions)

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