package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityViewBabyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ViewBaby : AppCompatActivity() {
    private lateinit var binding: ActivityViewBabyBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewBabyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the babyId from the intent
        val babyId = intent.getStringExtra("babyId")

        // Read and display baby data
        readData(babyId.toString())

        // Set up click listeners for different actions
        binding.bmi.setOnClickListener{
            val intent = Intent(this, BabyBmi::class.java).also {
                it.putExtra("babyId", babyId)
            }
            startActivity(intent)
        }
        binding.med.setOnClickListener{
            val intent = Intent(this, BabyMed::class.java).also {
                it.putExtra("babyId", babyId)
            }
            startActivity(intent)
        }
        binding.courses.setOnClickListener{
            val intent = Intent(this, BabyCourses::class.java).also {
                it.putExtra("babyId", babyId)
            }
            startActivity(intent)
        }
        binding.clinic.setOnClickListener{
            val intent = Intent(this, BabyClinic::class.java).also {
                it.putExtra("babyId", babyId)
            }
            startActivity(intent)
        }
        binding.btnHome.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }
    }

    // Function to read and display baby data
    private fun readData(babyId:String){
        FirebaseDatabase.getInstance().getReference("Baby").child("$babyId/fullName").get().addOnSuccessListener {
            if(it.exists()){
                // Display the baby's full name in the welcome text
                binding.txtWelcome.text = "${it.value} (Baby profile)"
            }
        }
    }
}
