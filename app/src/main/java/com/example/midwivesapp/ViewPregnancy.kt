package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityPregnancyListBinding
import com.example.midwivesapp.databinding.ActivityViewPregnancyBinding
import com.google.firebase.auth.FirebaseAuth

class ViewPregnancy : AppCompatActivity() {
    private lateinit var binding: ActivityViewPregnancyBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPregnancyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        val pregnancyId = intent.getStringExtra("pregnancyId")

        binding.bmi.setOnClickListener{
            var intent = Intent(this,MotherBmi::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        binding.med.setOnClickListener{
            var intent = Intent(this,MotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        binding.courses.setOnClickListener{
            var intent = Intent(this,MotherCourses::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }

        binding.clinic.setOnClickListener{
            var intent = Intent(this,MotherClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
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
}