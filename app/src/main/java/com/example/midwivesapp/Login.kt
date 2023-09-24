package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.midwivesapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Login activity
class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        // Check if the user is already logged in, if yes, navigate to the Dashboard
        checkIfUserIsLogged()

        // Login button click listener
        binding.btnLogin.setOnClickListener {
            registerUser()
        }
    }

    // Check if the user is already logged in
    private fun checkIfUserIsLogged() {
        if (user.currentUser != null) {
            // User is already logged in, navigate to the Dashboard
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
    }

    // Register and login the user
    private fun registerUser() {
        var email = binding.usernameTxt.text.toString()
        var password = binding.passwordTxt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Attempt to sign in with the provided email and password
            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { mtask ->
                    if (mtask.isSuccessful) {
                        // User login was successful, check user type
                        FirebaseDatabase.getInstance().getReference("User").child(user.uid.toString()).get()
                            .addOnSuccessListener {
                                if (it.exists()) {
                                    val userType = it.child("type").value
                                    if (userType.toString() == "mid") {
                                        // User is a midwife, navigate to the Dashboard
                                        startActivity(Intent(this, Dashboard::class.java))
                                    } else {
                                        // User is not a midwife
                                        Toast.makeText(
                                            this,
                                            "There is no account registered with the entered user details.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                    } else {
                        // User login failed, show an error message
                        // Handle the error as needed
                        Toast.makeText(this, mtask.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Email and password cannot be empty
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}
