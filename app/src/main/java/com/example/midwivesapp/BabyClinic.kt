package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyClinic : AppCompatActivity() {
    private lateinit var binding:ActivityBabyClinicBinding
    private lateinit var user:FirebaseAuth

    private lateinit var clinicArrayList : ArrayList<BabyClinicItem>
    private lateinit var clinicRecyclerView : RecyclerView

    private var babyIndex:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        var babyId = intent.getStringExtra("babyId")
        babyIndex = babyId.toString()

        clinicRecyclerView = binding.clinicList
        clinicRecyclerView.layoutManager = LinearLayoutManager(this)
        clinicRecyclerView.setHasFixedSize(true)
        clinicArrayList = arrayListOf<BabyClinicItem>()

        readData(babyId.toString())

        binding.btnAdd.setOnClickListener{
            var intent = Intent(this,AddBabyClinic::class.java).also {
                it.putExtra("babyId",babyId)
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

    private fun readData(babyId:String){
        FirebaseDatabase.getInstance().getReference("BabyClinic").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val clinicItem =  fineSnapshot.getValue(BabyClinicItem::class.java)
                            clinicArrayList.add(clinicItem!!)
                        }
                    }
                    clinicRecyclerView.adapter = BabyClinicAdapter(clinicArrayList,this@BabyClinic)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        var clinicId = clinicArrayList[position].clinicId.toString()
        var intent = Intent(this,ViewBabyClinic::class.java).also {
            it.putExtra("clinicId",clinicId)
            it.putExtra("status",clinicArrayList[position].status)
            it.putExtra("babyId",babyIndex)
        }
        startActivity(intent)
        finish()
    }
}