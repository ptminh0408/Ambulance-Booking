package com.example.ambulancebooking.menu

import android.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ambulancebooking.databinding.ActivityHospitalDetailBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso

class HospitalDetail : AppCompatActivity() {

    private lateinit var binding : ActivityHospitalDetailBinding
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setListeners()
        getHospitalData()
    }

    private fun setListeners(){
        binding.btnBook.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.Theme_DeviceDefault_Dialog)
            builder.setTitle("Booking")
            builder.setMessage("Do you want to book this hospital ?")
            builder.setPositiveButton("Yes") {
                    _, _ -> Toast.makeText(this, "Booked", Toast.LENGTH_SHORT).show() }
            builder.setNegativeButton("No") {
                    _, _ -> }
            builder.show()
        }

        binding.btnMap.setOnClickListener {
            fetchLocation()
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, Hospitals::class.java))
            finish()
        }
    }

    private fun getHospitalData(){
        binding.tvID.text = intent.getStringExtra("id")
        binding.tvName.text = intent.getStringExtra("name")
        binding.tvAddress.text = intent.getStringExtra("address")
        binding.tvPhone.text = intent.getStringExtra("phone")
        Picasso.get().load(intent.getStringExtra("image")).fit().centerCrop().into(binding.imgHospital)
    }

    private fun fetchLocation(){
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }else{
            task.addOnSuccessListener {
                if(it != null){
                    try{
                        val uri : Uri = Uri.parse("https://www.google.com/maps/dir/${it.latitude} ${it.longitude}/${intent.getStringExtra("address")}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }catch (e : ActivityNotFoundException){
                        val uri : Uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                }
            }
        }
    }
}