package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyCourseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddBabyCourse : AppCompatActivity() {
    private lateinit var binding: ActivityAddBabyCourseBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyCourseBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the babyId passed from the previous activity.
        val babyId = intent.getStringExtra("babyId")

        // Set up click listeners for the confirm and back buttons.
        binding.btnConfirm.setOnClickListener {
            addCourse(babyId.toString())
        }
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, BabyCourses::class.java).also {
                it.putExtra("babyId", babyId)
            }
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourse(babyId: String) {
        // Retrieve the course name from the input field.
        val courseName = binding.course.text.toString()

        if (courseName.isNotEmpty()) {
            // Get the current date.
            val createdDate = LocalDate.now().toString()
            val uuid = UUID.randomUUID()

            // Create a BabyCourseItem and add it to the Firebase Realtime Database.
            val course = BabyCourseItem(
                babyId, createdDate, courseName, user.uid.toString(), uuid.toString()
            )
            FirebaseDatabase.getInstance().getReference("BabyCourse").child(uuid.toString())
                .setValue(course).addOnSuccessListener {
                    // After successful addition, navigate back to the BabyCourses activity.
                    val intent = Intent(this, BabyCourses::class.java).also {
                        it.putExtra("babyId", babyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}
