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

class AddMotherCourse : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherCourseBinding
    private lateinit var user:FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherCourseBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.statusText.text = "Add Injection Course for this mother"

        var pregnancyId = intent.getStringExtra("pregnancyId")

        binding.btnConfirm.setOnClickListener{
            addCourse(pregnancyId.toString())
        }

        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherCourses::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourse(pregnancyId: String) {
        var courseName = binding.course.text.toString()

        if(courseName.isNotEmpty()) {
            var createdDate = LocalDate.now().toString()
            val uuid = UUID.randomUUID()
            var course = MotherCourseItem(
                pregnancyId,createdDate,courseName,user.uid.toString(),uuid.toString()
            )
            FirebaseDatabase.getInstance().getReference("MotherCourse").child(uuid.toString())
                .setValue(course).addOnSuccessListener {
                    var intent = Intent(this, MotherCourses::class.java).also {
                        it.putExtra("pregnancyId", pregnancyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}