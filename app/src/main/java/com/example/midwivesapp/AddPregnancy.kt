package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddPregnancyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddPregnancy : AppCompatActivity() {
    private lateinit var binding: ActivityAddPregnancyBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPregnancyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        // Get the motherId and motherName passed from the previous activity.
        val motherId = intent.getStringExtra("motherId")
        val motherName = intent.getStringExtra("motherName")

        // Set the status text to indicate adding a pregnancy for the mother.
        binding.statusText.text = "Add pregnancy for $motherName"

        // Read data to check if the mother is eligible for adding a pregnancy.
        readData(motherId.toString())

        // Set up a click listener for the "Confirm" button to add the pregnancy.
        binding.btnConfirm.setOnClickListener {
            addPregnancy(motherId.toString())
        }

        // Set up a click listener for the "Back" button to navigate back to the ViewMother activity.
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, ViewMother::class.java).also {
                it.putExtra("motherId", motherId)
            }
            startActivity(intent)
        }
    }

    // Function to check if the mother is eligible for adding a pregnancy.
    private fun readData(motherId: String) {
        FirebaseDatabase.getInstance().getReference("Mother").child(motherId).get().addOnSuccessListener { it ->
            if (it.exists()) {
                val status = it.child("status").value.toString()
                if (status == "mother") {
                    // The mother is eligible for adding a pregnancy.
                    binding.btnConfirm.visibility = View.VISIBLE
                } else {
                    // The mother is already pregnant.
                    binding.btnConfirm.visibility = View.GONE
                    Toast.makeText(this, "FAILED!.. mother status: pregnant!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ViewMother::class.java).also {
                        it.putExtra("motherId", motherId)
                    }
                    startActivity(intent)
                }
            } else {
                // Mother not found.
            }
        }
    }

    // Function to add a new pregnancy.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addPregnancy(motherId: String) {
        val pregnancyId = UUID.randomUUID().toString()
        val createdDate = LocalDate.now().toString()

        val status = "1"

        val pregnancy = Pregnancy(pregnancyId, motherId, createdDate, status)
        FirebaseDatabase.getInstance().getReference("Pregnancy").child(pregnancyId).setValue(pregnancy).addOnSuccessListener {
            val intent = Intent(this, ViewPregnancy::class.java).also {
                it.putExtra("pregnancyId", pregnancyId)
            }
            startActivity(intent)
            finish()
        }
    }
}
