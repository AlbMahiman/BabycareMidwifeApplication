package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyBinding
import com.example.midwivesapp.databinding.ActivityViewMotherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

// This is the AddBaby activity, which allows adding a new baby under a mother.

class AddBaby : AppCompatActivity() {
    private lateinit var binding: ActivityAddBabyBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBabyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        setContentView(binding.root)

        // Get motherId and motherName passed from the previous activity.
        val motherId = intent.getStringExtra("motherId")
        val motherName = intent.getStringExtra("motherName")

        // Set the status text to indicate adding a baby under the mother's name.
        binding.statusText.text = "Add Baby under $motherName"

        // Set up click listeners for the confirm and back buttons.
        binding.btnConfirm.setOnClickListener {
            addBaby(motherId.toString())
        }
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, ViewMother::class.java).also {
                it.putExtra("motherId", motherId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBaby(motherId: String) {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val fullName = binding.fullName.text.toString()
        val dob = binding.dob.text.toString()

        if (firstName == "" || lastName == "" || fullName == "" || dob == "") {
            Toast.makeText(this, "Fill all the inputs", Toast.LENGTH_SHORT).show()
        } else {
            val babyId = UUID.randomUUID().toString()
            val createdDate = LocalDate.now().toString()

            // Create a Baby object and add it to the Firebase Realtime Database.
            val baby = Baby(babyId, motherId, user.uid.toString(), createdDate, dob, firstName, lastName, fullName)
            FirebaseDatabase.getInstance().getReference("Baby").child(babyId).setValue(baby)
                .addOnSuccessListener {
                    val intent = Intent(this, ViewBaby::class.java).also {
                        it.putExtra("babyId", babyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}
