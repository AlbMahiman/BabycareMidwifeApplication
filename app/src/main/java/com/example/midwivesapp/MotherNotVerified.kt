package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityMotherNotVerifiedBinding

class MotherNotVerified : AppCompatActivity() {
    private lateinit var binding: ActivityMotherNotVerifiedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherNotVerifiedBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnHome.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }

    }
}