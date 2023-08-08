package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherClinic : AppCompatActivity() {

    private lateinit var binding: ActivityMotherClinicBinding
    private lateinit var user: FirebaseAuth

    private lateinit var clinicArrayList : ArrayList<MotherClinicItem>
    private lateinit var clinicRecyclerView : RecyclerView

    private var publicPregnancyId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")
        publicPregnancyId = pregnancyId.toString()

        clinicRecyclerView = binding.clinicList
        clinicRecyclerView.layoutManager = LinearLayoutManager(this)
        clinicRecyclerView.setHasFixedSize(true)
        clinicArrayList = arrayListOf<MotherClinicItem>()

        readData(pregnancyId.toString())

        binding.btnAdd.setOnClickListener{
            var intent = Intent(this,AddMotherClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
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

    private fun readData(pregnancyId:String){
        FirebaseDatabase.getInstance().getReference("MotherClinic").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val clinicItem =  fineSnapshot.getValue(MotherClinicItem::class.java)
                            clinicArrayList.add(clinicItem!!)
                        }
                    }
                    clinicRecyclerView.adapter = MotherClinicAdapter(clinicArrayList,this@MotherClinic)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        var clinicId = clinicArrayList[position].clinicId.toString()
        var intent = Intent(this,ViewMotherClinic::class.java).also {
            it.putExtra("clinicId",clinicId)
            it.putExtra("status",clinicArrayList[position].status)
            it.putExtra("pregnancyId",publicPregnancyId)
        }
        startActivity(intent)
        finish()
    }
}