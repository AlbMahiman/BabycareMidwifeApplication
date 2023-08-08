package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.midwivesapp.databinding.ActivityMotherAddCompleteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MotherAddComplete : AppCompatActivity() {
    private lateinit var binding: ActivityMotherAddCompleteBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherAddCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        var motherID = intent.getStringExtra("motherID")

        readData(motherID.toString())

        binding.btnViewMother.setOnClickListener{
            val intent = Intent(this,ViewMother::class.java).also{
                it.putExtra("motherID",motherID)
            }
            startActivity(intent)
            finish()
        }
        binding.btnBackToDashboard.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnHome.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun readData(motherID:String){
        FirebaseDatabase.getInstance().getReference("Mother").child(motherID).get().addOnSuccessListener {
            if(it.exists()){
                val fullName = it.child("motherFullName").value.toString()
                val email = it.child("email").value.toString()
                val password = it.child("password").value.toString()

                binding.txtFullName.text = fullName
                binding.txtEmail.text = email
                binding.txtPassword.text = password

            }
            else{
                //something went wrong mother not added.
                binding.motherAddingStatus.text = "Mother not added. Something went wrong. try Again!!!"
                binding.motherAddedController.visibility = View.GONE
            }
        }
    }
}