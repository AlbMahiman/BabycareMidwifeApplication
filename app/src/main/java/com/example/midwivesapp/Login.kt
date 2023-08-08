package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.midwivesapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        checkIfUserIsLogged()
        binding.btnLogin.setOnClickListener{
            registerUser()
        }

    }
    private fun checkIfUserIsLogged(){
        if(user.currentUser != null){
            startActivity(Intent(this,Dashboard::class.java))
            finish()
        }
    }
    private fun registerUser(){
        var email = binding.usernameTxt.text.toString()
        var password = binding.passwordTxt.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){

            //user login only
            user.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{mtask->
                    if(mtask.isSuccessful){
                        FirebaseDatabase.getInstance().getReference("User").child(user.uid.toString()).get().addOnSuccessListener {
                            if(it.exists()){
                                val userType =it.child("type").value
                                if(userType.toString() == "mid"){
                                    //user is a midwife
                                    startActivity(Intent(this,Dashboard::class.java))
                                }else{
                                    //user is not a mid wife
                                    Toast.makeText(this, "There is no account that registered with entered user details.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        /*
                        if(ConnectionCheck.checkForInternet(this)){
                            Toast.makeText(this,mtask.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                        else{
                            startActivity(Intent(this,CheckInternetCon::class.java))
                            finish()
                        }

                         */
                    }
                }


            //password email is not empty - user registration+login
/*
            user.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(Login()){ task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Dashboard::class.java))
                    finish()
                }
                else{
                    user.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener{mtask->
                            if(mtask.isSuccessful){
                                startActivity(Intent(this,Dashboard::class.java))
                            }else{
                                Toast.makeText(this,mtask.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

 */


        }else{
            //password email empty
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}