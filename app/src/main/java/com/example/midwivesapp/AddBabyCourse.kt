package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddBabyCourseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*
@RequiresApi(Build.VERSION_CODES.O)
class AddBabyCourse : AppCompatActivity() {
    private lateinit var binding:ActivityAddBabyCourseBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBabyCourseBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")

        binding.btnConfirm.setOnClickListener{
            addCourse(babyId.toString())
        }
        binding.btnBack.setOnClickListener{
            var intent = Intent(this,BabyCourses::class.java).also {
                it.putExtra("babyId",babyId)
            }
            startActivity(intent)
            finish()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourse(babyId:String) {
        var courseName = binding.course.text.toString()

        if (courseName.isNotEmpty()) {
            var createdDate = LocalDate.now().toString()
            val uuid = UUID.randomUUID()
            var course = BabyCourseItem(
                babyId,createdDate,courseName,user.uid.toString(),uuid.toString()
            )
            FirebaseDatabase.getInstance().getReference("BabyCourse").child(uuid.toString())
                .setValue(course).addOnSuccessListener {
                    var intent = Intent(this, BabyCourses::class.java).also {
                        it.putExtra("babyId", babyId)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}