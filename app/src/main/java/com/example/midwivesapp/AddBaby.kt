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

class AddBaby : AppCompatActivity() {
    private lateinit var binding: ActivityAddBabyBinding
    private lateinit var user : FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBabyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        setContentView(binding.root)

        val motherId = intent.getStringExtra("motherId")
        val motherName = intent.getStringExtra("motherName")

        binding.statusText.text = "Add Baby under $motherName"

        binding.btnConfirm.setOnClickListener{
            addBaby(motherId.toString())
        }
        binding.btnBack.setOnClickListener{
            var intent = Intent(this,ViewMother::class.java).also {
                it.putExtra("motherId",motherId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBaby(motherId:String){
        var firstName = binding.firstName.text.toString()
        var lastName = binding.lastName.text.toString()
        var fullName = binding.fullName.text.toString()
        var dob = binding.dob.text.toString()

        if(firstName == "" || lastName == "" || fullName == "" || dob == ""){
            Toast.makeText(this, "fill all the inputs", Toast.LENGTH_SHORT).show()
        }else{
            var babyId = UUID.randomUUID().toString()
            var createdDate = LocalDate.now().toString()

            var baby = Baby(babyId,motherId,user.uid.toString(),createdDate,dob,firstName,lastName,fullName)
            FirebaseDatabase.getInstance().getReference("Baby").child(babyId).setValue(baby).addOnSuccessListener {
                var intent = Intent(this,ViewBaby::class.java).also {
                    it.putExtra("babyId",babyId)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}