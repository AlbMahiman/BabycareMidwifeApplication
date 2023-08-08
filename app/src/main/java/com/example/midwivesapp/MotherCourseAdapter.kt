package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class MotherCourseAdapter(private val CourseList:ArrayList<MotherCourseItem>, private val listener: MotherCourses): RecyclerView.Adapter<MotherCourseAdapter.CourseViewHolder>(){

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView : RecyclerView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_element,parent,false)
        return CourseViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentItem = CourseList[position]
        var currentNumber = position + 1
        holder.medDate.text = currentItem.date.toString()
        holder.medName.text = currentItem.medName.toString()
        holder.elementNum.text = currentNumber.toString()

    }
    override fun getItemCount(): Int {
        return CourseList.size
    }

    inner class CourseViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
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