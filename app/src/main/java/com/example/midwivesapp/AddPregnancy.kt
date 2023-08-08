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

class AddPregnancy : AppCompatActivity() {
    private lateinit var binding: ActivityAddPregnancyBinding
    private lateinit var user: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPregnancyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        val motherId = intent.getStringExtra("motherId")
        val motherName = intent.getStringExtra("motherName")

        binding.statusText.text = "Add pregnancy for $motherName"

        readData(motherId.toString())

        binding.btnConfirm.setOnClickListener{
            addPregnancy(motherId.toString())
        }
        binding.btnBack.setOnClickListener{
            var intent = Intent(this,ViewMother::class.java).also {
                it.putExtra("motherId",motherId)
            }
            startActivity(intent)
        }

    }

    private fun readData(motherId: String){
        FirebaseDatabase.getInstance().getReference("Mother").child(motherId).get().addOnSuccessListener { it ->
            if(it.exists()){
                var status = it.child("status").value.toString()
                if(status == "mother"){
                    binding.btnConfirm.visibility = View.VISIBLE
                }else{
                    binding.btnConfirm.visibility = View.GONE
                    Toast.makeText(this, "FAILED!.. mother status: pregnant!", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this,ViewMother::class.java).also {
                        it.putExtra("motherId",motherId)
                    }
                    startActivity(intent)
                }
            }else{
                //mother not found
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addPregnancy(motherId:String){
        var pregnancyId = UUID.randomUUID().toString()
        var createdDate = LocalDate.now().toString()

        var status = "1"

        var pregnancy = Pregnancy(pregnancyId,motherId,createdDate,status)
        FirebaseDatabase.getInstance().getReference("Pregnancy").child(pregnancyId).setValue(pregnancy).addOnSuccessListener {
            var intent = Intent(this,ViewPregnancy::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }
    }
}