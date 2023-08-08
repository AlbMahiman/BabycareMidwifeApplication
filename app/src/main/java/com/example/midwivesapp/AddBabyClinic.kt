package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

class AddBabyClinic : AppCompatActivity() {

    private lateinit var binding:ActivityAddBabyClinicBinding
    private lateinit var user:FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")

        binding.btnConfirm.setOnClickListener{
            addClinic(babyId.toString())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(babyId:String){
        var purpose = binding.purpose.text.toString()
        var clinicDate = binding.clinicDate.text.toString()
        if(purpose.isNotEmpty() && clinicDate.isNotEmpty()){
            var createdDate = LocalDate.now().toString()
            var status = "pending"
            val uuid = UUID.randomUUID()
            var clinicItem = BabyClinicItem(uuid.toString(),babyId,createdDate,purpose,user.uid.toString(),clinicDate,status)

            FirebaseDatabase.getInstance().getReference("BabyClinic").child(uuid.toString())
                .setValue(clinicItem).addOnSuccessListener {
                    var intent = Intent(this, BabyClinic::class.java).also {
                        it.putExtra("babyId", babyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }else{
            Toast.makeText(this, "Fill all the inputs to add a clinic", Toast.LENGTH_SHORT).show()
        }
    }
}