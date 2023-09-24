package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityPregnancyListBinding
import com.example.midwivesapp.databinding.ActivityViewPregnancyBinding
import com.google.firebase.auth.FirebaseAuth

class ViewPregnancy : AppCompatActivity() {
    private lateinit var binding: ActivityViewPregnancyBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityViewPregnancyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase authentication
        user = FirebaseAuth.getInstance()

        // Get the pregnancyId from the intent
        val pregnancyId = intent.getStringExtra("pregnancyId")

        // Handle BMI button click
        binding.bmi.setOnClickListener{
            var intent = Intent(this,MotherBmi::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        // Handle Medical button click
        binding.med.setOnClickListener{
            var intent = Intent(this,MotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        // Handle Courses button click
        binding.courses.setOnClickListener{
            var intent = Intent(this,MotherCourses::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        // Handle Clinic button click
        binding.clinic.setOnClickListener{
            var intent = Intent(this,MotherClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        // Handle Home button click
        binding.btnHome.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        // Handle Back button click
        binding.back.setOnClickListener{
            finish()
        }
    }
}
