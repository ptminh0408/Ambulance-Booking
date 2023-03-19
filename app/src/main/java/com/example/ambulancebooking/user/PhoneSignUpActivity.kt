package com.example.ambulancebooking.user

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ambulancebooking.databinding.ActivityPhoneSignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneSignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPhoneSignUpBinding
    private var forceResendingToken : PhoneAuthProvider.ForceResendingToken? = null
    private var mCallBack : PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId : String? = null
    private lateinit var fAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //This callback will be invoked in two situations:
                // 1. Instant verification. In some cases this phone number can be instantly
                // verify without needing to send or enter a verification code
                // 2. Auto retrieval. On some devices Google Play services can automatically
                // detect the incoming verification SMS and perform verification without user action
                loading(true)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                //This callback is invoked in an invalid request for verification is made,
                // for instance if the phone number is not valid
                showToast("${e.message}")
                loading(false)
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                //The SMS verification code has been sent to the provided phone number,
                //we now to ask the user to enter the code and then construct a credential
                //by combining the code with verification ID
                Log.d(TAG, "onCodeSend: $verificationId")
                mVerificationId = verificationId
                forceResendingToken = token
                showToast("Verification code sent")
                val intent = Intent(applicationContext, PhoneVerifyActivity::class.java)
                intent.putExtra("mVerificationId", mVerificationId)
                startActivity(intent)
                loading(false)
            }
        }
        setListener()
    }

    private fun setListener(){
        binding.btnConfirm.setOnClickListener {
            var phone = binding.edtPhoneNumber.text.toString().trim()
            if(phone.isEmpty()){
                binding.edtPhoneNumber.error = "Please enter your phone number"
                binding.edtPhoneNumber.requestFocus()
            }else{
                phone = "+84$phone"
                startPhoneNumberVerification(phone)
                loading(true)
            }
        }
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnConfirm.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnConfirm.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun startPhoneNumberVerification(phone : String){
        val options = PhoneAuthOptions.newBuilder(fAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBack!!)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}