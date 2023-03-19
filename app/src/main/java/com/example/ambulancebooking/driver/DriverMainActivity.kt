package com.example.ambulancebooking.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ambulancebooking.R
import com.example.ambulancebooking.databinding.ActivityDriverMainBinding

class DriverMainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDriverMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_notification -> {
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this, "History", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_Profile -> {
                    startActivity(Intent(this, DriverProfileActivity::class.java))
                    true
                }
                R.id.nav_Setting -> {
                    startActivity(Intent(this, DriverSettingActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}