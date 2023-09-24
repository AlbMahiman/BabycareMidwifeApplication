package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

// This is the AddBabyClinic activity, which allows adding clinic information for a baby.

class AddBabyClinic : AppCompatActivity() {

    private lateinit var binding: ActivityAddBabyClinicBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the babyId passed from the previous activity.
        val babyId = intent.getStringExtra("babyId")

        // Set up a click listener for the confirm button.
        binding.btnConfirm.setOnClickListener {
            addClinic(babyId.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(babyId: String) {
        // Retrieve purpose and clinic date from input fields.
        val purpose = binding.purpose.text.toString()
        val clinicDate = binding.clinicDate.text.toString()

        if (purpose.isNotEmpty() && clinicDate.isNotEmpty()) {
            // Get the current date and set the status as "pending".
            val createdDate = LocalDate.now().toString()
            val status = "pending"
            val uuid = UUID.randomUUID()

            // Create a BabyClinicItem and add it to the Firebase Realtime Database.
            val clinicItem = BabyClinicItem(uuid.toString(), babyId, createdDate, purpose, user.uid.toString(), clinicDate, status)
            FirebaseDatabase.getInstance().getReference("BabyClinic").child(uuid.toString())
                .setValue(clinicItem).addOnSuccessListener {
                    // After successful addition, navigate to the BabyClinic activity.
                    val intent = Intent(this, BabyClinic::class.java).also {
                        it.putExtra("babyId", babyId)
                    }
                    startActivity(intent)
                    finish()
                }
        } else {
            Toast.makeText(this, "Fill all the inputs to add a clinic", Toast.LENGTH_SHORT).show()
        }
    }
}
