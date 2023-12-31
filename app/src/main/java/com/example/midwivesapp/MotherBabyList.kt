package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherBabyListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherBabyList : AppCompatActivity() {
    private lateinit var binding: ActivityMotherBabyListBinding
    private lateinit var user: FirebaseAuth

    private lateinit var babyArrayList: ArrayList<Baby>
    private lateinit var babyRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherBabyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        // Get the motherId from the intent
        val motherId = intent.getStringExtra("motherId")

        babyRecyclerView = binding.babyList
        babyRecyclerView.layoutManager = LinearLayoutManager(this)
        babyRecyclerView.setHasFixedSize(true)
        babyArrayList = arrayListOf<Baby>()

        // Read baby data for the specific mother
        readData(motherId.toString())

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun readData(motherId: String) {
        FirebaseDatabase.getInstance().getReference("Baby").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (fineSnapshot in snapshot.children) {
                        if (fineSnapshot.child("motherId").value.toString() == motherId) {
                            val babyItem = fineSnapshot.getValue(Baby::class.java)
                            babyArrayList.add(babyItem!!)
                        }
                    }
                    // Set up the adapter to display baby data
                    babyRecyclerView.adapter = MotherBabyAdapter(babyArrayList, this@MotherBabyList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }
        })
    }

    // Handle item click event
    fun onItemClick(position: Int) {
        val currentPregnancy = babyArrayList[position]
        val intent = Intent(this, ViewBaby::class.java).also {
            it.putExtra("babyId", currentPregnancy.babyId)
        }
        startActivity(intent)
    }
}
