package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityViewBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

class ViewBabyClinic : AppCompatActivity() {

    private lateinit var binding:ActivityViewBabyClinicBinding
    private lateinit var user:FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewBabyClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var status = intent.getStringExtra("status")
        var clinicId = intent.getStringExtra("clinicId")
        var babyId = intent.getStringExtra("babyId")

        if(status.toString() == "pending"){
            showForm()
        }else if(status.toString() == "completed"){
            readData(clinicId.toString())
        }
        binding.btnAdd.setOnClickListener{
            addClinic(clinicId.toString(),babyId.toString())
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
    private fun showForm(){
        binding.form.visibility = View.VISIBLE
        binding.view.visibility = View.GONE
    }

    private fun readData(clinicId:String){
        binding.form.visibility = View.GONE
        binding.view.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("BabyConservations")
            .orderByChild("clinicId")
            .equalTo(clinicId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (data in snapshot.children) {
                            binding.updatedDate.text = data.child("currentDate").value.toString()
                            binding.hbeat.text = data.child("heartBeat").value.toString()
                            binding.skinColour.text = data.child("skin").value.toString()
                            binding.cPressure.text = data.child("pressure").value.toString()
                            binding.cswelling.text = data.child("swelling").value.toString()
                            binding.thriposhaNum.text = data.child("thriposha").value.toString()
                            binding.weekCount.text = data.child("week").value.toString()

                            FirebaseDatabase.getInstance().getReference("User").child(data.child("nurseId").value.toString()).get().addOnSuccessListener {
                                if(it.exists()){
                                    binding.nurseName.text = "${it.child("firstName").value.toString()} ${it.child("lastName").value.toString()}"
                                }else{
                                    binding.nurseName.text = "Nurse's name is not available"
                                }
                            }

                        }
                    }else{
                        Toast.makeText(this@ViewBabyClinic, "This Clinical Conservation completed but not available at the movement. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(clinicId: String,babyId:String){
        var week = binding.pregnancyWeek.text.toString()
        var weight = binding.motherWeight.text.toString()
        var swelling = binding.swelling.text.toString()
        var pressure = binding.bloodPressure.text.toString()
        var position = binding.position.text.toString()
        var heartBeat = binding.heartBeat.text.toString()
        var powder = binding.powderCount.text.toString()
        var createdDate = LocalDate.now().toString()
        var nurseId = user.uid.toString()
        var conservationId = UUID.randomUUID().toString()

        if(week.isNotEmpty() && weight.isNotEmpty() && swelling.isNotEmpty() && pressure.isNotEmpty() && position.isNotEmpty() && heartBeat.isNotEmpty() &&
            createdDate.isNotEmpty() && nurseId.isNotEmpty()){
            if(powder.isEmpty()){
                powder = "0"
            }
            var conservationItem = BabyConservationItem(createdDate,nurseId,clinicId,week,weight,swelling,pressure,position,heartBeat,powder)

            FirebaseDatabase.getInstance().getReference("BabyConservations").child(conservationId).setValue(conservationItem).addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("BabyClinic").child("$clinicId/status").setValue("completed")
                var intent = Intent(this,BabyClinic::class.java).also {
                    it.putExtra("babyId",babyId)
                }
                startActivity(intent)
                finish()
            }

        }
    }
}