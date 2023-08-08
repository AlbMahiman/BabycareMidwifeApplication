package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.UUID

class AddMotherClinic : AppCompatActivity() {

    private lateinit var binding: ActivityAddMotherClinicBinding
    private lateinit var user: FirebaseAuth
    private var counter: Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")

        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }

        binding.btnConfirm.setOnClickListener{
            addClinic(pregnancyId.toString())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(pregnancyId:String){
        var purpose = binding.purpose.text.toString()
        var clinicDate = binding.clinicDate.text.toString()

        var clinicId = UUID.randomUUID()
        var createdDate = LocalDate.now().toString()

        if(purpose.isNotEmpty() && clinicDate.isNotEmpty()){

            var status = "pending"

            var motherClinicItem = MotherClinicItem(clinicId.toString(),pregnancyId,createdDate,purpose,user.uid.toString(),clinicDate,status)

            FirebaseDatabase.getInstance().getReference("MotherClinic").child(clinicId.toString()).setValue(motherClinicItem).addOnSuccessListener {
                var intent = Intent(this,MotherClinic::class.java).also {
                    it.putExtra("pregnancyId",pregnancyId)
                }
                startActivity(intent)
                finish()
            }

        }else{
            Toast.makeText(this, "Fill all the inputs to add a clinic", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmClinic(pregnancyId: String, counter:Int){


    }
}