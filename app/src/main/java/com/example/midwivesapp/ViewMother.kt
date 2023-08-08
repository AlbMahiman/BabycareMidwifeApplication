package com.example.midwivesapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.midwivesapp.databinding.ActivityMotherListBinding
import com.example.midwivesapp.databinding.ActivityViewMotherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ViewMother : AppCompatActivity() {
    private lateinit var binding: ActivityViewMotherBinding
    private lateinit var user : FirebaseAuth

    private lateinit var motherName: String
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMotherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val motherId = intent.getStringExtra("motherId")

        readData(motherId.toString())

        binding.addPreg.setOnClickListener{
            var intent = Intent(this,AddPregnancy::class.java).also {
                it.putExtra("motherId",motherId)
                it.putExtra("motherName",motherName)
            }
            startActivity(intent)
        }

        binding.pregList.setOnClickListener{
            var intent = Intent(this,PregnancyList::class.java).also {
                it.putExtra("motherId",motherId)
            }
            startActivity(intent)
        }

        binding.babyList.setOnClickListener{
            var intent = Intent(this,MotherBabyList::class.java).also {
                it.putExtra("motherId",motherId)
            }
            startActivity(intent)
        }

        binding.addBaby.setOnClickListener{
            var intent = Intent(this,AddBaby::class.java).also {
                it.putExtra("motherId",motherId)
                it.putExtra("motherName",motherName)
            }
            startActivity(intent)
        }

        binding.viewChat.setOnClickListener{
            var intent = Intent(this,Chat::class.java).also {
                it.putExtra("motherId",motherId)
                it.putExtra("motherName",motherName)
            }
            startActivity(intent)
        }

        binding.makeCall.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(dialIntent)
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

    private fun readData(motherId:String){
        FirebaseDatabase.getInstance().getReference("Mother").child("$motherId").get().addOnSuccessListener {
            if(it.exists()){
                val fullName =it.child("motherFullName").value.toString()
                val status =it.child("status").value.toString()
                binding.txtMotherFullName.text = fullName
                binding.txtMotherStatus.text = status

                motherName = fullName
                phoneNumber = it.child("phoneNumber").value.toString()

                /*if(status == "pregnant"){
                    binding.btnAddBaby.visibility = View.GONE
                    binding.txtAddBaby.visibility = View.GONE
                }else if(status == "mother"){
                    binding.btnAddBaby.visibility = View.VISIBLE
                    binding.txtAddBaby.visibility = View.VISIBLE
                }

                 */


                binding.txtFullName.text = fullName
                binding.txtStatus.text = status
                binding.txtAddress.text = it.child("address").value.toString()
                binding.txtPhoneNum.text =  "0703135183" //it.child("address").value.toString()
                binding.txtDob.text = it.child("dob").value.toString()
                binding.txtEmail.text = it.child("email").value.toString()
                binding.txtMotherId.text = it.child("motherId").value.toString()
                binding.txtNumOfBabies.text = it.child("numOfBabies").value.toString()

            }
            else{
                startActivity(Intent(this,Dashboard::class.java))
                finish()
            }
        }
    }
}