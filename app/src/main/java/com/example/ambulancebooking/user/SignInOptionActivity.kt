package com.example.ambulancebooking.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.databinding.ActivitySignInOptionBinding
import com.google.firebase.auth.FirebaseAuth

class SignInOptionActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInOptionBinding
    private lateinit var fAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fAuth = FirebaseAuth.getInstance()
        checkUser()
        setListeners()

    }

    private fun setListeners(){
        binding.btnEmail.setOnClickListener {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
        }

        binding.btnPhone.setOnClickListener {
            startActivity(Intent(applicationContext, PhoneSignUpActivity::class.java))
        }
    }

    private fun checkUser(){
        if(fAuth.currentUser != null){
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finishAffinity()
        }
    }
}