package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyMedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddBabyMed : AppCompatActivity() {
    private lateinit var binding: ActivityAddBabyMedBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyMedBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the babyId passed from the previous activity.
        val babyId = intent.getStringExtra("babyId")

        // Set up click listeners for the confirm and back buttons.
        binding.btnConfirm.setOnClickListener {
            addMed(babyId.toString())
        }
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, BabyMed::class.java).also {
                it.putExtra("babyId", babyId)
            }
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMed(babyId: String) {
        // Retrieve medication name and instructions from the input fields.
        val medName = binding.med.text.toString()
        val instructions = binding.instructions.text.toString()

        if (medName.isNotEmpty() && instructions.isNotEmpty()) {
            // Get the current date.
            val createdDate = LocalDate.now().toString()
            val uuid = UUID.randomUUID()

            // Create a BabyMedItem and add it to the Firebase Realtime Database.
            val med = BabyMedItem(
                babyId,
                createdDate,
                medName,
                user.uid.toString(),
                uuid.toString(),
                instructions
            )
            FirebaseDatabase.getInstance().getReference("BabyMed").child(uuid.toString())
                .setValue(med).addOnSuccessListener {
                    // After successful addition, navigate back to the BabyMed activity.
                    val intent = Intent(this, BabyMed::class.java).also {
                        it.putExtra("babyId", babyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}
