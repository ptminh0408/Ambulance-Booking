package com.example.ambulancebooking

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.ambulancebooking.databinding.ActivityMainBinding
import com.example.ambulancebooking.map.NewMap
import com.example.ambulancebooking.menu.*
import com.example.ambulancebooking.user.ProfileActivity
import com.example.ambulancebooking.model.Users
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fAuth : FirebaseAuth
    private lateinit var fUser : FirebaseUser
    private lateinit var databaseReference : DatabaseReference
    private lateinit var binding : ActivityMainBinding
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseAppCheck : FirebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        fAuth = FirebaseAuth.getInstance()
        fUser = fAuth.currentUser!!
        userID = fUser.uid
        showProfile()
        setListeners()
    }

    private fun setListeners(){
        binding.aboutButton.setOnClickListener{
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }

        binding.contactButton.setOnClickListener{
            val intent = Intent(this, Contact::class.java)
            startActivity(intent)
        }

        bottom_nav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.ic_payment -> {
                    intent = Intent(this, Payment::class.java).also {
                        startActivity(it)
                    }
                    true
                }
                R.id.ic_settings -> {
                    intent = Intent(this, Settings::class.java).also {
                        startActivity(it)
                    }
                    true
                }
                R.id.ic_profile -> {
                    intent = Intent(this, ProfileActivity::class.java).also {
                        startActivity(it)
                    }
                    true
                }
                R.id.ic_hospital -> {
                    intent = Intent(this, Hospitals::class.java).also{
                        startActivity(it)
                    }
                    true
                }
                else -> false
            }
        }
        val checkAnim = findViewById<LottieAnimationView>(R.id.lottieAmbulance)
        checkAnim.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }else{
                val intent = Intent(this, NewMap::class.java)
                startActivity(intent)
            }

        }
    }

    private fun showProfile(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user : Users? = snapshot.getValue(Users::class.java)
                for (dataSnapshot in snapshot.children) {
                    if (dataSnapshot.key == userID) {
                        user = dataSnapshot.getValue(Users::class.java)
                        if(user?.name != null){
                            binding.txtName.text = user.name
                        }else{
                            val phone = fUser.phoneNumber
                            binding.txtName.text = phone
                        }

                        if(user?.image == null){
                            binding.imgProfile.setImageResource(R.drawable.user)
                        }else{
                            Picasso.get().load(user.image).into(binding.imgProfile)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}