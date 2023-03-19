package com.example.ambulancebooking.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.databinding.ActivityPhoneVerifyBinding
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PhoneVerifyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPhoneVerifyBinding
    private lateinit var fAuth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseAppCheck : FirebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        fAuth = FirebaseAuth.getInstance()
        setListener()
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnVerify.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnVerify.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setListener(){
        binding.btnVerify.setOnClickListener {
            receiveOTP()
        }
    }

    private fun receiveOTP(){
        val otp = binding.pvOtp.text.toString().trim()

        if(otp.isEmpty()){
            showToast("Please enter otp code")
        }else{
            loading(true)
            val mVerificationId = intent.getStringExtra("mVerificationId")
            val credential1 : PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId.toString(), otp)
            signInWithPhoneAuthCredential(credential1)
        }
    }

    private fun signInWithPhoneAuthCredential(credential1: PhoneAuthCredential) {
        fAuth.signInWithCredential(credential1)
            .addOnSuccessListener {
                userID = fAuth.currentUser!!.uid
                val phone = fAuth.currentUser!!.phoneNumber
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("phone")
                databaseReference.setValue(phone)
                showToast("Logged in as $phone")
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finishAffinity()
                loading(false)
            }
            .addOnFailureListener {e ->
                showToast("${e.message}")
                loading(false)
            }
    }
}