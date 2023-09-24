package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityViewMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

class ViewMotherClinic : AppCompatActivity() {
    private lateinit var binding: ActivityViewMotherClinicBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get data from the intent
        var status = intent.getStringExtra("status")
        var clinicId = intent.getStringExtra("clinicId")
        var pregnancyId = intent.getStringExtra("pregnancyId")

        // Check the status to determine which UI to show
        if(status.toString() == "pending"){
            showForm()
        } else if(status.toString() == "completed"){
            readData(clinicId.toString())
        }

        // Handle button click events
        binding.btnAdd.setOnClickListener{
            addClinic(clinicId.toString(), pregnancyId.toString())
        }
        binding.btnHome.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }
    }

    // Function to show the form layout
    private fun showForm(){
        binding.form.visibility = View.VISIBLE
        binding.view.visibility = View.GONE
    }

    // Function to read data from Firebase and update UI
    private fun readData(clinicId:String){
        binding.form.visibility = View.GONE
        binding.view.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("MotherConservations")
            .orderByChild("clinicId")
            .equalTo(clinicId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (data in snapshot.children) {
                            binding.updatedDate.text = data.child("currentDate").value.toString()
                            binding.hbeat.text = data.child("heartBeat").value.toString()
                            binding.cposition.text = data.child("position").value.toString()
                            binding.cPressure.text = data.child("pressure").value.toString()
                            binding.cswelling.text = data.child("swelling").value.toString()
                            binding.thriposhaNum.text = data.child("thriposha").value.toString()
                            binding.weekCount.text = data.child("week").value.toString()

                            // Get nurse's name from Firebase
                            FirebaseDatabase.getInstance().getReference("User").child(data.child("nurseId").value.toString()).get().addOnSuccessListener {
                                if(it.exists()){
                                    binding.nurseName.text = "${it.child("firstName").value.toString()} ${it.child("lastName").value.toString()}"
                                } else {
                                    binding.nurseName.text = "Nurse's name is not available"
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@ViewMotherClinic, "This Clinical Conservation completed but not available at the moment. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    // Function to add clinic data to Firebase
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(clinicId: String, pregnancyId: String) {
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

        if (week.isNotEmpty() && weight.isNotEmpty() && swelling.isNotEmpty() && pressure.isNotEmpty() && position.isNotEmpty() && heartBeat.isNotEmpty() &&
            createdDate.isNotEmpty() && nurseId.isNotEmpty()) {
            if (powder.isEmpty()) {
                powder = "0"
            }
            var conservationItem = MotherConservationItem(createdDate, nurseId, clinicId, week, weight, swelling, pressure, position, heartBeat, powder)

            // Set the clinic status as "completed" for the specific clinic
            FirebaseDatabase.getInstance().getReference("MotherClinic").child(clinicId).child("status").setValue("completed")

            // Add conservation data to Firebase
            FirebaseDatabase.getInstance().getReference("MotherConservations").child(conservationId).setValue(conservationItem).addOnSuccessListener {
                var intent = Intent(this, MotherClinic::class.java).also {
                    it.putExtra("pregnancyId", pregnancyId)
                }
                startActivity(intent)
                finish()
            }
        }
    }

}
