package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityBabyBmiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyBmi : AppCompatActivity() {
    private lateinit var binding:ActivityBabyBmiBinding
    private lateinit var user:FirebaseAuth

    private lateinit var bmiArrayList : ArrayList<BabyBmiItem>
    private lateinit var bmiRecyclerView : RecyclerView

    private var babyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        var babyId = intent.getStringExtra("babyId")
        babyIndex = babyId.toString()

        bmiRecyclerView = binding.bmiList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        bmiArrayList = arrayListOf<BabyBmiItem>()
        readData(babyId.toString())

        binding.btnAddBmi.setOnClickListener{
            var intent = Intent(this,AddBabyBmi::class.java).also {
                it.putExtra("babyId",babyId)
            }
            startActivity(intent)
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
    private fun readData(babyId:String){
        bmiArrayList.clear()
        FirebaseDatabase.getInstance().getReference("BabyBmi").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val bmiItem =  fineSnapshot.getValue(BabyBmiItem::class.java)
                            bmiArrayList.add(bmiItem!!)
                        }
                    }
                    bmiRecyclerView.adapter = BabyBmiAdapter(bmiArrayList,this@BabyBmi)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BabyBmi, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onItemClick(position: Int) {
        FirebaseDatabase.getInstance().getReference("BabyBmi").child(bmiArrayList[position].bmiIndex.toString()).removeValue().addOnSuccessListener {
            bmiArrayList.clear()
            Toast.makeText(this, "Bmi Element Deleted Successfully", Toast.LENGTH_SHORT).show()
            readData(babyIndex)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to delete. Try Again", Toast.LENGTH_SHORT).show()
        }
    }

}