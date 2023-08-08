package com.example.midwivesapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherBmiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherBmi : AppCompatActivity() {
    private lateinit var binding: ActivityMotherBmiBinding
    private lateinit var user: FirebaseAuth

    private lateinit var bmiArrayList : ArrayList<Bmi>
    private lateinit var bmiRecyclerView : RecyclerView

    private var pregnancyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")
        pregnancyIndex = pregnancyId.toString()

        bmiRecyclerView = binding.bmiList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        bmiArrayList = arrayListOf<Bmi>()
        readData(pregnancyId.toString())

        binding.btnAddBmi.setOnClickListener{
            var intent = Intent(this,AddMotherBmi   ::class.java).also {
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
        binding.viewChart.setOnClickListener{
            var intent = Intent(this, Chart::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun readData(pregnancyId:String){
        bmiArrayList.clear()
        FirebaseDatabase.getInstance().getReference("Bmi").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val bmiItem =  fineSnapshot.getValue(Bmi::class.java)
                            bmiArrayList.add(bmiItem!!)
                        }
                    }
                    bmiRecyclerView.adapter = BmiAdapter(bmiArrayList,this@MotherBmi)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        FirebaseDatabase.getInstance().getReference("Bmi").child(bmiArrayList[position].bmiIndex.toString()).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Bmi Element Deleted Successfully", Toast.LENGTH_SHORT).show()
            readData(pregnancyIndex)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to delete. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}