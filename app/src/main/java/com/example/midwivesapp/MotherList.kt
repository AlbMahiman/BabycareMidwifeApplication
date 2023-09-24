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

        // Read and display the list of mothers associated with the current nurse
        readData(user.uid.toString())

        // Add click listeners to navigation buttons
        binding.btnHome.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun readData(userid:String){
        // Read mother data from the Firebase database
        FirebaseDatabase.getInstance().getReference("Mother").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("nurseId").value.toString() == user.uid.toString()){
                            val motherItem = fineSnapshot.getValue(MotherItem::class.java)
                            motherArrayList.add(motherItem!!)
                        }
                    }
                    // Populate the RecyclerView with mother data using the adapter
                    motherRecyclerView.adapter = MotherAdapter(motherArrayList,this@MotherList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read error (if needed)
            }
        })
    }

    // Function to handle item click events in the RecyclerView
    fun onItemClick(position: Int) {
        var currentMother = motherArrayList[position]
        if(currentMother.accountStatus == "pending"){
            // If the mother's account status is pending, navigate to the MotherNotVerified activity
            var intent = Intent(this, MotherNotVerified::class.java).also {
                it.putExtra("motherId",currentMother.motherId)
            }
            startActivity(intent)
        }else if(currentMother.accountStatus == "verified"){
            // If the mother's account status is verified, navigate to the ViewMother activity
            var intent = Intent(this, ViewMother::class.java).also {
                it.putExtra("motherId",currentMother.motherId)
            }
            startActivity(intent)
        }
    }
}
