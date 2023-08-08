package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener

class MotherList : AppCompatActivity() {
    private lateinit var binding: ActivityMotherListBinding
    private lateinit var user: FirebaseAuth

    private lateinit var motherArrayList : ArrayList<MotherItem>
    private lateinit var motherRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        motherRecyclerView = binding.motherList
        motherRecyclerView.layoutManager = LinearLayoutManager(this)
        motherRecyclerView.setHasFixedSize(true)
        motherArrayList = arrayListOf<MotherItem>()
        readData(user.uid.toString())
        binding.btnHome.setOnClickListener{
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }

    }

    private fun readData(userid:String){
        FirebaseDatabase.getInstance().getReference("Mother").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("nurseId").value.toString() == user.uid.toString()){
                            val motherItem =  fineSnapshot.getValue(MotherItem::class.java)
                            motherArrayList.add(motherItem!!)
                        }
                    }
                    motherRecyclerView.adapter = MotherAdapter(motherArrayList,this@MotherList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        var currentMother = motherArrayList[position]
        if(currentMother.accountStatus == "pending"){
            var intent = Intent(this,MotherNotVerified::class.java).also {
                it.putExtra("motherId",currentMother.motherId)
            }
            startActivity(intent)
        }else if(currentMother.accountStatus == "verified"){
            var intent = Intent(this,ViewMother::class.java).also {
                it.putExtra("motherId",currentMother.motherId)
            }
            startActivity(intent)
        }
    }
}