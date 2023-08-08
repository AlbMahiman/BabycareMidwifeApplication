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

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        readData()

        binding.btnLogout.setOnClickListener{
            signOutUser()
        }
        binding.btnCreateMotherAccount.setOnClickListener{
            startActivity(Intent(this,addMotherForm::class.java))
        }
        binding.qrScanner.setOnClickListener{
            startActivity(Intent(this,QrScanner::class.java))
        }
        binding.viewMotherList.setOnClickListener{
            startActivity(Intent(this,MotherList::class.java))
        }


    }

    private fun readData(){
        binding.txtWelcome.text = user.uid.toString()
        FirebaseDatabase.getInstance().getReference("User").child(user.uid.toString()).get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value

                binding.txtWelcome.text = user.uid.toString() /*"Welcome! $firstName $lastName"*/
            }
        }
    }


    private fun signOutUser(){
        /*
        if(ConnectionCheck.checkForInternet(this)){
            user.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }*/

        user.signOut()
        startActivity(Intent(this,Login::class.java))
        finish()
    }
}