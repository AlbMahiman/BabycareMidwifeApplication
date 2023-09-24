package com.example.midwivesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.midwivesapp.databinding.ActivityDashboardBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

// Dashboard activity
class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        // Read user data
        readData()

        // Logout button click listener
        binding.btnLogout.setOnClickListener {
            signOutUser()
        }

        // Create Mother Account button click listener
        binding.btnCreateMotherAccount.setOnClickListener {
            startActivity(Intent(this, addMotherForm::class.java))
        }

        // QR Scanner button click listener
        binding.qrScanner.setOnClickListener {
            startActivity(Intent(this, QrScanner::class.java))
        }

        // View Mother List button click listener
        binding.viewMotherList.setOnClickListener {
            startActivity(Intent(this, MotherList::class.java))
        }
    }

    // Read user data and display welcome message
    private fun readData() {
        binding.txtWelcome.text = user.uid.toString() // Default text before retrieving user data
        FirebaseDatabase.getInstance().getReference("User").child(user.uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                // Retrieve user's first name and last name
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value

                // Display a welcome message with the user's name
                binding.txtWelcome.text = user.uid.toString() /*"Welcome! $firstName $lastName"*/
            }
        }
    }

    // Sign out the user and navigate to the login screen
    private fun signOutUser() {
        user.signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}
