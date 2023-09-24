package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class AddMotherClinic : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherClinicBinding
    private lateinit var user: FirebaseAuth
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the pregnancyId passed from the previous activity.
        val pregnancyId = intent.getStringExtra("pregnancyId")

        // Set up a click listener for the back button.
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MotherClinic::class.java).also {
                it.putExtra("pregnancyId", pregnancyId)
            }
            startActivity(intent)
            finish()
        }

        // Set up a click listener for the confirm button to add a clinic.
        binding.btnConfirm.setOnClickListener {
            addClinic(pregnancyId.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(pregnancyId: String) {
        // Retrieve purpose and clinic date from the input fields.
        val purpose = binding.purpose.text.toString()
        val clinicDate = binding.clinicDate.text.toString()

        // Generate a unique clinicId using UUID.
        val clinicId = UUID.randomUUID().toString()
        val createdDate = LocalDate.now().toString()

        if (purpose.isNotEmpty() && clinicDate.isNotEmpty()) {
            val status = "pending"

            // Create a MotherClinicItem and add it to the Firebase Realtime Database.
            val motherClinicItem = MotherClinicItem(
                clinicId,
                pregnancyId,
                createdDate,
                purpose,
                user.uid.toString(),
                clinicDate,
                status
            )

            FirebaseDatabase.getInstance().getReference("MotherClinic").child(clinicId)
                .setValue(motherClinicItem).addOnSuccessListener {
                    // After successful addition, navigate back to the MotherClinic activity.
                    val intent = Intent(this, MotherClinic::class.java).also {
                        it.putExtra("pregnancyId", pregnancyId)
                    }
                    startActivity(intent)
                    finish()
                }
        } else {
            Toast.makeText(this, "Fill all the inputs to add a clinic", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmClinic(pregnancyId: String, counter: Int) {
        // Implement the logic for confirming a clinic here, if needed.
        // You can add comments describing this logic once it's implemented.
    }
}
