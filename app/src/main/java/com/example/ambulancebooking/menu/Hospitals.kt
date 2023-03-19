package com.example.ambulancebooking.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.adapter.HospitalAdapter
import com.example.ambulancebooking.R
import com.example.ambulancebooking.model.Hospital
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_hospitals.*

class Hospitals : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var hospitalRecyclerView : RecyclerView
    private lateinit var hospitalArrayList : ArrayList<Hospital>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitals)
        val firebaseAppCheck : FirebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        val btnBack : Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        hospitalRecyclerView = findViewById(R.id.recyclerViewHospital)
        hospitalRecyclerView.layoutManager = LinearLayoutManager(this)
        hospitalRecyclerView.setHasFixedSize(true)

        hospitalArrayList = arrayListOf<Hospital>()
        getHospitalData()
    }

    private fun getHospitalData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Hospitals")

        databaseReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(hospitalSnapshot in snapshot.children){
                        val hospital = hospitalSnapshot.getValue(Hospital::class.java)
                        hospitalArrayList.add(hospital!!)
                    }
                    hospitalRecyclerView.adapter = HospitalAdapter(hospitalArrayList)

                    val adapter = HospitalAdapter(hospitalArrayList)
                    recyclerViewHospital.adapter = adapter
                    adapter.setOnItemClickListener(object : HospitalAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@Hospitals, HospitalDetail::class.java)
                            intent.putExtra("id", hospitalArrayList[position].id)
                            intent.putExtra("name", hospitalArrayList[position].name)
                            intent.putExtra("address", hospitalArrayList[position].address)
                            intent.putExtra("phone", hospitalArrayList[position].phone)
                            intent.putExtra("image", hospitalArrayList[position].image)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}