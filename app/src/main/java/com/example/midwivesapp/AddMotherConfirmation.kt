package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherConfirmationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddMotherConfirmation : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherConfirmationBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMotherConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        // Reading the passed dataset
        val motherFName = intent.getStringExtra("fName")
        val motherLName = intent.getStringExtra("lName")
        val motherEmail = intent.getStringExtra("email")
        val motherPassword = intent.getStringExtra("password")
        val dob = intent.getStringExtra("dob")
        val address = intent.getStringExtra("address")
        val phoneNumber = intent.getStringExtra("phoneNumber")

        // Displaying the passed data on the confirmation screen
        binding.txtMotherFullName.text = "$motherFName $motherLName"
        binding.txtMotherEmail.text = motherEmail.toString()
        binding.txtMotherPassword.text = motherPassword.toString()

        // Set up a click listener for the "Confirm" button to add the mother to the database.
        binding.btnAddMotherConfirm.setOnClickListener {
            addMother(
                motherFName.toString(),
                motherLName.toString(),
                motherEmail.toString(),
                motherPassword.toString(),
                address.toString(),
                dob.toString(),
                phoneNumber.toString()
            )
        }

        // Set up click listeners for navigation buttons.
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMother(
        motherFName: String,
        motherLName: String,
        motherEmail: String,
        motherPassword: String,
        address: String,
        dob: String,
        phoneNumber: String
    ) {
        // Generate a unique motherID using UUID.
        val motherID = UUID.randomUUID().toString()

        // Initialize other mother-related data.
        val babyCount = 0
        val status = "pregnant"
        val fullName = "$motherFName $motherLName"
        val nurseId = user.uid.toString()
        val createdDate = LocalDate.now()
        val accountStatus = "pending"

        // Create a Mother object and add it to the Firebase Realtime Database.
        val mother = Mother(
            fullName,
            motherFName,
            status,
            babyCount.toString(),
            address,
            dob,
            nurseId,
            createdDate.toString(),
            motherEmail,
            motherPassword,
            motherID,
            phoneNumber,
            accountStatus
        )

        FirebaseDatabase.getInstance().getReference("Mother").child(motherID).setValue(mother)
            .addOnSuccessListener {
                // After successful addition, create a conversation entry and navigate to the MotherAddComplete activity.
                val conversation = Conversation(motherID, motherID, nurseId)
                FirebaseDatabase.getInstance().getReference("Conversation").child(motherID)
                    .setValue(conversation).addOnSuccessListener {
                        val intent = Intent(this, MotherAddComplete::class.java).also {
                            it.putExtra("motherID", motherID)
                        }
                        startActivity(intent)
                        finish()
                    }
            }
    }
}
