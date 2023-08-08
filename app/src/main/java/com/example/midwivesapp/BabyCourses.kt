package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityBabyCoursesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyCourses : AppCompatActivity() {
    private lateinit var binding:ActivityBabyCoursesBinding
    private lateinit var user:FirebaseAuth

    private lateinit var courseArrayList : ArrayList<BabyCourseItem>
    private lateinit var courseRecyclerView : RecyclerView

    private var babyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")
        babyIndex = babyId.toString()

        binding.btnAddCourse.setOnClickListener{
            var intent = Intent(this,AddBabyCourse::class.java).also {
                it.putExtra("babyId",babyId)
            }
            startActivity(intent)
        }

        courseRecyclerView = binding.courseList
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)
        courseArrayList = arrayListOf<BabyCourseItem>()
        readData(babyId.toString())

        binding.btnHome.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }


    }

    private fun readData(babyId:String){
        FirebaseDatabase.getInstance().getReference("BabyCourse").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val courseItem =  fineSnapshot.getValue(BabyCourseItem::class.java)
                            courseArrayList.add(courseItem!!)
                        }
                    }
                    courseRecyclerView.adapter = BabyCourseAdapter(courseArrayList,this@BabyCourses)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun onItemClick(position: Int) {
        FirebaseDatabase.getInstance().getReference("BabyCourse").child(courseArrayList[position].courseId.toString()).removeValue().addOnSuccessListener {
            courseArrayList.clear()
            Toast.makeText(this, "Bmi Element Deleted Successfully", Toast.LENGTH_SHORT).show()
            readData(babyIndex)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to delete. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}