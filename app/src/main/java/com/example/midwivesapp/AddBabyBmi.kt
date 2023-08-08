package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyBinding
import com.example.midwivesapp.databinding.ActivityAddBabyBmiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddBabyBmi : AppCompatActivity() {
    private lateinit var binding: ActivityAddBabyBmiBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyBmiBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")

        binding.btnConfirm.setOnClickListener{
            addBmi(babyId.toString())
        }
        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherBmi::class.java).also {
                it.putExtra("babyId",babyId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBmi(babyId:String){
        var weight = binding.weight.text.toString()
        var height = binding.height.text.toString()

        var createdDate = LocalDate.now().toString()

        if(weight.isNotEmpty() && height.isNotEmpty()){
            var bmiVal = (weight.toFloat() / (height.toFloat() * height.toFloat()))*10000
            var bmiStatus = ""
            bmiStatus = if(bmiVal < 18.5){
                "Underweight"
            }else if(bmiVal in 18.5..24.9){
                "Healthy weight"
            }else if(bmiVal in 25.0..29.9){
                "Overweight"
            }else if(bmiVal in 30.0..39.9){
                "obesity"
            }else{
                "Class 3 obesity"
            }

            val uuid = UUID.randomUUID()
            binding.statusText.text = bmiVal.toString()
            var bmi = BabyBmiItem(babyId,weight,height,bmiVal,createdDate,bmiStatus,uuid.toString())
            FirebaseDatabase.getInstance().getReference("BabyBmi").child(uuid.toString()).setValue(bmi).addOnSuccessListener {
                var intent = Intent(this,BabyBmi::class.java).also {
                    it.putExtra("babyId",babyId)
                }
                startActivity(intent)
                finish()
            }

        }else{
            Toast.makeText(this, "Fill all the input fields", Toast.LENGTH_SHORT).show()
        }
    }

}