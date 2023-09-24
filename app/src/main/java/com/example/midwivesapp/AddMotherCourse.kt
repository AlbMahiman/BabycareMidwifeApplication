package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherCourseBinding
import com.example.midwivesapp.databinding.ActivityAddMotherMedBinding
import com.example.midwivesapp.databinding.ActivityMotherCoursesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddMotherCourse : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherCourseBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherCourseBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set the status text to indicate adding an injection course for the mother.
        binding.statusText.text = "Add Injection Course for this mother"

        // Get the pregnancyId passed from the previous activity.
        val pregnancyId = intent.getStringExtra("pregnancyId")

        // Set up a click listener for the "Confirm" button to add the course.
        binding.btnConfirm.setOnClickListener {
            addCourse(pregnancyId.toString())
        }

        // Set up a click listener for the "Back" button to navigate back to the MotherCourses activity.
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MotherCourses::class.java).also {
                it.putExtra("pregnancyId", pregnancyId)
            }
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourse(pregnancyId: String) {
        // Get the course name from the input field.
        val courseName = binding.course.text.toString()

        if (courseName.isNotEmpty()) {
            // Get the current date as the course creation date.
            val createdDate = LocalDate.now().toString()

            // Generate a unique UUID for the course.
            val uuid = UUID.randomUUID()

            // Create a MotherCourseItem object.
            val course = MotherCourseItem(
                pregnancyId, createdDate, courseName, user.uid.toString(), uuid.toString()
            )

            // Add the course to the Firebase Realtime Database.
            FirebaseDatabase.getInstance().getReference("MotherCourse").child(uuid.toString())
                .setValue(course).addOnSuccessListener {
                    // After successfully adding the course, navigate back to the MotherCourses activity.
                    val intent = Intent(this, MotherCourses::class.java).also {
                        it.putExtra("pregnancyId", pregnancyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}
