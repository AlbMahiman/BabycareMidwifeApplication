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

class AddMotherConfirmation : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherConfirmationBinding
    private lateinit var user:FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMotherConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        //reading the passed dataset
        var motherFName = intent.getStringExtra("fName")
        var motherLName = intent.getStringExtra("lName")
        var motherEmail = intent.getStringExtra("email")
        var motherPassword = intent.getStringExtra("password")
        var dob = intent.getStringExtra("dob")
        var address = intent.getStringExtra("address")
        var phoneNumber = intent.getStringExtra("phoneNumber")

        binding.txtMotherFullName.text = "$motherFName $motherLName"
        binding.txtMotherEmail.text = motherEmail.toString()
        binding.txtMotherPassword.text = motherPassword.toString()

        binding.btnAddMotherConfirm.setOnClickListener{
            addMother(motherFName.toString(),motherLName.toString(),motherEmail.toString(),motherPassword.toString(),address.toString(),dob.toString(),phoneNumber.toString())
        }


        binding.btnHome.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMother(motherFName:String, motherLName:String, motherEmail:String, motherPassword:String,address:String,dob:String,phoneNumber:String){
        var motherID = UUID.randomUUID().toString()
        var babyCount = 0
        var status = "pregnant"
        var fullName = "$motherFName $motherLName"
        var nurseId = user.uid.toString()
        var createdDate = LocalDate.now()

        var accountStatus = "pending"

        var mother = Mother(fullName,motherFName,status,babyCount.toString(),address,dob,nurseId,createdDate.toString(), motherEmail, motherPassword,motherID,phoneNumber,accountStatus)
        FirebaseDatabase.getInstance().getReference("Mother").child(motherID).setValue(mother).addOnSuccessListener {
            var conversation = Conversation(motherID,motherID,nurseId)
            FirebaseDatabase.getInstance().getReference("Conversation").child(motherID).setValue(conversation).addOnSuccessListener {
                val intent = Intent(this,MotherAddComplete::class.java).also{
                    it.putExtra("motherID",motherID)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}