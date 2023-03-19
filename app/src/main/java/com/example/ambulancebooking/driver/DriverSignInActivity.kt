package com.example.ambulancebooking.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast

import com.example.ambulancebooking.databinding.ActivityDriverSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DriverSignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDriverSignInBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firebaseUser : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners(){
        binding.driverSignUp.setOnClickListener {
            startActivity(Intent(this, DriverSignUpActivity::class.java))
        }

        binding.driverSignIn.setOnClickListener {
            driverSignIn()
        }
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.driverSignIn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.driverSignIn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun driverSignIn(){
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.edtEmail.error = "Please enter your email"
            binding.edtEmail.requestFocus()
            return
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Invalid email"
            binding.edtEmail.requestFocus()
            return
        }else if(password.isEmpty()){
            binding.edtPassword.error = "Please enter your password"
            binding.edtPassword.requestFocus()
            return
        }else{
            loading(true)
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    loading(false)
                    startActivity(Intent(this, DriverMainActivity::class.java))
                    finishAffinity()
                }else{
                    loading(false)
                    showToast("Wrong password or email")
                }
            }
        }
    }
}