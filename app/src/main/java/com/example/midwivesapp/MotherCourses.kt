package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherCoursesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherCourses : AppCompatActivity() {
    private lateinit var binding: ActivityMotherCoursesBinding
    private lateinit var user: FirebaseAuth

    private lateinit var courseArrayList : ArrayList<MotherCourseItem>
    private lateinit var courseRecyclerView : RecyclerView

    private var pregnancyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherCoursesBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the pregnancy ID passed from the previous activity
        var pregnancyId = intent.getStringExtra("pregnancyId")
        pregnancyIndex = pregnancyId.toString()

        // Add a click listener to the "Add Course" button
        binding.btnAddCourse.setOnClickListener{
            var intent = Intent(this, AddMotherCourse::class.java).also {
                it.putExtra("pregnancyId", pregnancyId)
            }
            startActivity(intent)
            finish()
        }

        // Set up the RecyclerView for displaying courses
        courseRecyclerView = binding.courseList
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)
        courseArrayList = arrayListOf<MotherCourseItem>()

        // Read course data from the database and populate the RecyclerView
        readData(pregnancyId.toString())

        // Add click listeners to navigation buttons
        binding.btnHome.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun readData(pregnancyId: String) {
        // Read course data for the given pregnancy from the Firebase database
        FirebaseDatabase.getInstance().getReference("MotherCourse").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (fineSnapshot in snapshot.children) {
                        if (fineSnapshot.child("pregnancyId").value.toString() == pregnancyId) {
                            val courseItem = fineSnapshot.getValue(MotherCourseItem::class.java)
                            courseArrayList.add(courseItem!!)
                        }
                    }
                    // Populate the RecyclerView with course data using the adapter
                    courseRecyclerView.adapter = MotherCourseAdapter(courseArrayList, this@MotherCourses)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error (if needed)
            }
        })
    }

    // Function to handle item click events in the RecyclerView
    fun onItemClick(position: Int) {
        // Remove the selected course item from the database
        FirebaseDatabase.getInstance().getReference("MotherCourse")
            .child(courseArrayList[position].courseId.toString())
            .removeValue()
            .addOnSuccessListener {
                courseArrayList.clear()
                Toast.makeText(this, "Course Element Deleted Successfully", Toast.LENGTH_SHORT).show()
                readData(pregnancyIndex)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete. Try Again", Toast.LENGTH_SHORT).show()
            }
    }
}
