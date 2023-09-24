package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherMedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddMotherMed : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherMedBinding
    private lateinit var user: FirebaseAuth

    private var counter: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherMedBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        // Get the pregnancyId passed from the previous activity.
        val pregnancyId = intent.getStringExtra("pregnancyId")

        // Set the status text to indicate adding medicine for the mother.
        binding.statusText.text = "Add Medicine for this mother"

        // Set up a click listener for the "Confirm" button to add the medicine.
        binding.btnConfirm.setOnClickListener {
            addMed(pregnancyId.toString())
        }

        // Set up a click listener for the "Back" button to navigate back to the MotherMed activity.
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MotherMed::class.java).also {
                it.putExtra("pregnancyId", pregnancyId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMed(pregnancyId: String) {
        // Get the medicine name and instructions from the input fields.
        val medName = binding.med.text.toString()
        val instructions = binding.instructions.text.toString()

        if (medName.isNotEmpty() && instructions.isNotEmpty()) {
            // Get the current date as the medicine creation date.
            val createdDate = LocalDate.now().toString()

            // Generate a unique UUID for the medicine.
            val uuid = UUID.randomUUID()

            // Create a MotherMedItem object.
            val med = MotherMedItem(
                pregnancyId,
                createdDate,
                medName,
                user.uid.toString(),
                uuid.toString(),
                instructions
            )

            // Add the medicine to the Firebase Realtime Database.
            FirebaseDatabase.getInstance().getReference("MotherMed").child(uuid.toString())
                .setValue(med).addOnSuccessListener {
                    // After successfully adding the medicine, navigate back to the MotherMed activity.
                    val intent = Intent(this, MotherMed::class.java).also {
                        it.putExtra("pregnancyId", pregnancyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}
