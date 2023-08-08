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

class AddMotherMed : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherMedBinding
    private lateinit var user: FirebaseAuth

    private var counter:Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherMedBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        var pregnancyId = intent.getStringExtra("pregnancyId")

        binding.statusText.text = "Add Medicine for this mother"

        binding.btnConfirm.setOnClickListener{
            addMed(pregnancyId.toString())
        }

        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMed(pregnancyId:String){
        var medName = binding.med.text.toString()
        var instructions = binding.instructions.text.toString()

        if (medName.isNotEmpty() && instructions.isNotEmpty()) {
            var createdDate = LocalDate.now().toString()
            val uuid = UUID.randomUUID()
            var med = MotherMedItem(
                pregnancyId,
                createdDate,
                medName,
                user.uid.toString(),
                uuid.toString(),
                instructions
            )
            FirebaseDatabase.getInstance().getReference("MotherMed").child(uuid.toString())
                .setValue(med).addOnSuccessListener {
                    var intent = Intent(this, MotherMed::class.java).also {
                        it.putExtra("pregnancyId", pregnancyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}