package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyMedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*
@RequiresApi(Build.VERSION_CODES.O)
class AddBabyMed : AppCompatActivity() {
    private lateinit var binding:ActivityAddBabyMedBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyMedBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        var babyId = intent.getStringExtra("babyId")

        binding.btnConfirm.setOnClickListener{
            addMed(babyId.toString())
        }
        binding.btnBack.setOnClickListener{
            var intent = Intent(this,BabyMed::class.java).also {
                it.putExtra("babyId",babyId)
            }
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMed(babyId:String) {
        var medName = binding.med.text.toString()
        var instructions = binding.instructions.text.toString()

        if (medName.isNotEmpty() && instructions.isNotEmpty()) {
            var createdDate = LocalDate.now().toString()
            val uuid = UUID.randomUUID()
            var med = BabyMedItem(
                babyId,
                createdDate,
                medName,
                user.uid.toString(),
                uuid.toString(),
                instructions
            )
            FirebaseDatabase.getInstance().getReference("BabyMed").child(uuid.toString())
                .setValue(med).addOnSuccessListener {
                var intent = Intent(this, BabyMed::class.java).also {
                    it.putExtra("babyId", babyId)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}