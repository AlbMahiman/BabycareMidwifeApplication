package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityBabyMedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyMed : AppCompatActivity() {
    private lateinit var binding:ActivityBabyMedBinding
    private lateinit var user:FirebaseAuth

    private lateinit var medicineArrayList : ArrayList<BabyMedItem>
    private lateinit var medicineRecyclerView : RecyclerView

    private var babyIndex = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")
        babyIndex = babyId.toString()

        medicineRecyclerView = binding.medList
        medicineRecyclerView.layoutManager = LinearLayoutManager(this)
        medicineRecyclerView.setHasFixedSize(true)
        medicineArrayList = arrayListOf<BabyMedItem>()
        readData(babyId.toString())


        binding.btnAddMed.setOnClickListener{
            var intent = Intent(this,AddBabyMed::class.java).also {
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
        FirebaseDatabase.getInstance().getReference("BabyMed").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val medItem =  fineSnapshot.getValue(BabyMedItem::class.java)
                            medicineArrayList.add(medItem!!)
                        }
                    }
                    medicineRecyclerView.adapter = BabyMedAdapter(medicineArrayList,this@BabyMed)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun onItemClick(position: Int) {
        FirebaseDatabase.getInstance().getReference("BabyMed").child(medicineArrayList[position].medId.toString()).removeValue().addOnSuccessListener {
            medicineArrayList.clear()
            Toast.makeText(this, "Bmi Element Deleted Successfully", Toast.LENGTH_SHORT).show()
            readData(babyIndex)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to delete. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}