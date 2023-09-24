package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityViewBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

class ViewBabyClinic : AppCompatActivity() {

    private lateinit var binding: ActivityViewBabyClinicBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewBabyClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get data from the intent
        var status = intent.getStringExtra("status")
        var clinicId = intent.getStringExtra("clinicId")
        var babyId = intent.getStringExtra("babyId")

        // Check the status and decide whether to show the form or read data
        if (status.toString() == "pending") {
            showForm()
        } else if (status.toString() == "completed") {
            readData(clinicId.toString())
        }

        // Set click listeners for buttons
        binding.btnAdd.setOnClickListener {
            addClinic(clinicId.toString(), babyId.toString())
        }
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun showForm() {
        // Show the form and hide the view
        binding.form.visibility = View.VISIBLE
        binding.view.visibility = View.GONE
    }

    private fun readData(clinicId: String) {
        // Hide the form and show the view
        binding.form.visibility = View.GONE
        binding.view.visibility = View.VISIBLE

        // Query the database for clinic data
        FirebaseDatabase.getInstance().getReference("BabyConservations")
            .orderByChild("clinicId")
            .equalTo(clinicId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            // Populate the view with clinic data
                            binding.updatedDate.text = data.child("currentDate").value.toString()
                            binding.hbeat.text = data.child("heartBeat").value.toString()
                            binding.skinColour.text = data.child("skin").value.toString()
                            binding.cPressure.text = data.child("pressure").value.toString()
                            binding.cswelling.text = data.child("swelling").value.toString()
                            binding.thriposhaNum.text = data.child("thriposha").value.toString()
                            binding.weekCount.text = data.child("week").value.toString()

                            // Get nurse data and display nurse's name
                            FirebaseDatabase.getInstance().getReference("User")
                                .child(data.child("nurseId").value.toString())
                                .get()
                                .addOnSuccessListener {
                                    if (it.exists()) {
                                        binding.nurseName.text =
                                            "${it.child("firstName").value.toString()} ${it.child("lastName").value.toString()}"
                                    } else {
                                        binding.nurseName.text = "Nurse's name is not available"
                                    }
                                }
                        }
                    } else {
                        // Display a message if clinic data is not available
                        Toast.makeText(
                            this@ViewBabyClinic,
                            "This Clinical Conservation completed but not available at the moment. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error if needed
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(clinicId: String, babyId: String) {
        // Get data from form fields
        var week = binding.pregnancyWeek.text.toString()
        var weight = binding.motherWeight.text.toString()
        var swelling = binding.swelling.text.toString()
        var pressure = binding.bloodPressure.text.toString()
        var position = binding.position.text.toString()
        var heartBeat = binding.heartBeat.text.toString()
        var powder = binding.powderCount.text.toString()
        var createdDate = LocalDate.now().toString()
        var nurseId = user.uid.toString()
        var conservationId = UUID.randomUUID().toString()

        // Handle optional field (powder count)
        if (powder.isEmpty()) {
            powder = "0"
        }

        // Create a BabyConservationItem and save it to the database
        var conservationItem = BabyConservationItem(
            createdDate,
            nurseId,
            clinicId,
            week,
            weight,
            swelling,
            pressure,
            position,
            heartBeat,
            powder
        )

        FirebaseDatabase.getInstance().getReference("BabyConservations")
            .child(conservationId)
            .setValue(conservationItem)
            .addOnSuccessListener {
                // Mark the clinic as completed in the BabyClinic node
                FirebaseDatabase.getInstance().getReference("BabyClinic")
                    .child("$clinicId/status")
                    .setValue("completed")

                // Navigate to the BabyClinic activity
                var intent = Intent(this, BabyClinic::class.java).also {
                    it.putExtra("babyId", babyId)
                }
                startActivity(intent)
                finish()
            }
    }
}
