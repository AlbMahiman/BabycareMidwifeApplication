package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.midwivesapp.databinding.ActivityAddMotherFormBinding
import com.google.firebase.auth.FirebaseAuth

class addMotherForm : AppCompatActivity() {
    private lateinit var binding:ActivityAddMotherFormBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMotherFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        binding.btnAddMother.setOnClickListener{
            addMother()
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
    private fun addMother(){
        val fName = binding.txtMFirstName.text.toString()
        val lName = binding.txtMLastName.text.toString()
        val email = binding.txtMEmail.text.toString()
        val password = binding.txtMPassword.text.toString()
        val dob = binding.txtMDob.text.toString()
        val address = binding.txtMAddress.text.toString()
        val confirmPassword = binding.txtMConfirmPassword.text.toString()
        val phoneNumber = binding.txtPhoneNumber.text.toString()

        if(fName.isNotEmpty() && lName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && dob.isNotEmpty() && address.isNotEmpty() && phoneNumber.isNotEmpty()){
            if(password == confirmPassword){
                val intent = Intent(this,AddMotherConfirmation::class.java).also{
                    it.putExtra("fName",fName)
                    it.putExtra("lName",lName)
                    it.putExtra("email",email)
                    it.putExtra("password",password)
                    it.putExtra("dob",dob)
                    it.putExtra("address",address)
                    it.putExtra("phoneNumber",phoneNumber)
                }
                startActivity(intent)
                finish()   
            }else{
                Toast.makeText(this, "Passwords are not matched. Check the entered passwords and try again. ", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "fill all the input fields", Toast.LENGTH_SHORT).show()
        }
    }
}