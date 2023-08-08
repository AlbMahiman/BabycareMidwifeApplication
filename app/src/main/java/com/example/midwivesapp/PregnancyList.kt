package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityAddPregnancyBinding
import com.example.midwivesapp.databinding.ActivityPregnancyListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PregnancyList : AppCompatActivity() {
    private lateinit var binding: ActivityPregnancyListBinding
    private lateinit var user: FirebaseAuth

    private lateinit var pregnancyArrayList : ArrayList<PregnancyItem>
    private lateinit var pregnancyRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPregnancyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        val motherId = intent.getStringExtra("motherId")

        pregnancyRecyclerView = binding.pregnancyList
        pregnancyRecyclerView.layoutManager = LinearLayoutManager(this)
        pregnancyRecyclerView.setHasFixedSize(true)
        pregnancyArrayList = arrayListOf<PregnancyItem>()
        readData(motherId.toString())

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
        FirebaseDatabase.getInstance().getReference("Pregnancy").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("motherId").value.toString() == motherId){
                            val pregnancyItem =  fineSnapshot.getValue(PregnancyItem::class.java)
                            pregnancyArrayList.add(pregnancyItem!!)
                        }
                    }
                    pregnancyRecyclerView.adapter = PregnancyAdapter(pregnancyArrayList,this@PregnancyList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        var currentPregnancy = pregnancyArrayList[position]
        var intent = Intent(this,ViewPregnancy::class.java).also {
            it.putExtra("pregnancyId",currentPregnancy.pregnancyId)
        }
        startActivity(intent)
    }
}