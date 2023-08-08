package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherMedBinding
import com.example.midwivesapp.databinding.ActivityPregnancyListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherMed : AppCompatActivity() {

    private lateinit var binding: ActivityMotherMedBinding
    private lateinit var user: FirebaseAuth

    private lateinit var medicineArrayList : ArrayList<MotherMedItem>
    private lateinit var medicineRecyclerView : RecyclerView

    private var pregnancyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherMedBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")
        pregnancyIndex = pregnancyId.toString()

        binding.btnAddMed.setOnClickListener{
            var intent = Intent(this,AddMotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }

        medicineRecyclerView = binding.medList
        medicineRecyclerView.layoutManager = LinearLayoutManager(this)
        medicineRecyclerView.setHasFixedSize(true)
        medicineArrayList = arrayListOf<MotherMedItem>()
        readData(pregnancyId.toString())

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
        FirebaseDatabase.getInstance().getReference("MotherMed").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val motherMedItem =  fineSnapshot.getValue(MotherMedItem::class.java)
                            medicineArrayList.add(motherMedItem!!)
                        }
                    }
                    medicineRecyclerView.adapter = MotherMedAdapter(medicineArrayList,this@MotherMed)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        FirebaseDatabase.getInstance().getReference("MotherMed").child(medicineArrayList[position].medId.toString()).removeValue().addOnSuccessListener {
            medicineArrayList.clear()
            Toast.makeText(this, "Bmi Element Deleted Successfully", Toast.LENGTH_SHORT).show()
            readData(pregnancyIndex)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to delete. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}