package com.example.ambulancebooking.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.ambulancebooking.R
import com.example.ambulancebooking.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var fAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fAuth = FirebaseAuth.getInstance()
        setListener()
    }

    private fun setListener(){
        binding.btnConfirm.setOnClickListener {
            forgotPassword()
        }
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun forgotPassword() {
        val edtEmail = binding.edtEmail.text.toString().trim()
        if(edtEmail.isEmpty()){
            binding.edtEmail.error = "Please enter email"
            binding.edtEmail.requestFocus()
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches()){
            binding.edtEmail.error = "Invalid Email"
            binding.edtEmail.requestFocus()
            return
        }else{
            fAuth.sendPasswordResetEmail(edtEmail).addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    startActivity(Intent(applicationContext, SignInActivity::class.java))
                    finishAffinity()
                    showToast("Please check your email to change password")
                }else{
                    showToast("Wrong email")
                }
            }
        }
    }

}