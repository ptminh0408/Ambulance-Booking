package com.example.ambulancebooking.menu

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.databinding.ActivityContactBinding

class Contact : AppCompatActivity() {

    private lateinit var binding : ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        }
        binding.tvPhone.setOnClickListener {
            val phone = binding.tvPhone.text.toString().trim()
            val builder = AlertDialog.Builder(this, R.style.Theme_DeviceDefault_Dialog)
            builder.setTitle("Phone Call")
            builder.setMessage("Do you want to call $phone ?")
            builder.setPositiveButton("Yes") {
                    _, _ -> call() }
            builder.setNegativeButton("No") {
                    _, _ -> }
            builder.show()
        }

        binding.tvFacebook.setOnClickListener {
            try{
                openFacebook("https://www.facebook.com/profile.php?id=100008911560773")
            }catch(e : Exception){
                showToast("Facebook account doesn't exist: ${e.message}")
            }
        }

        binding.tvWeb.setOnClickListener {
            try{
                openWebsite("https://www.fvhospital.com/vi/trang-chu/")
            }catch(e : Exception){
                showToast("Website doesn't exist: ${e.message}")
            }
        }

        binding.tvEmail.setOnClickListener {
            val email : String = binding.tvEmail.text.toString().trim()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showToast(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openWebsite(s : String){
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun openFacebook(s : String){
        val uri = Uri.parse(s)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun call(){
        val phone : String = binding.tvPhone.text.toString().trim()
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }
}