package com.example.ambulancebooking.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity(){

    private lateinit var binding : ActivitySignInBinding
    private lateinit var fAuth : FirebaseAuth
    private lateinit var userAuth : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fAuth = FirebaseAuth.getInstance()
        setListener()
    }

    private fun setListener(){
        binding.btnSignUp.setOnClickListener{
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
        binding.txtForgotPassword.setOnClickListener{
            startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
        }
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnSignIn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSignIn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun signIn(){

        val edtEmail = binding.edtEmail.text.toString().trim()
        val edtPassword = binding.edtPassword.text.toString().trim()

        if(edtEmail.isEmpty()){
            binding.edtEmail.error = "Please enter your email"
            binding.edtEmail.requestFocus()
            return
        } else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches()){
            binding.edtEmail.error = "Invalid email"
            binding.edtEmail.requestFocus()
            return
        }else if(edtPassword.isEmpty()){
            binding.edtPassword.error = "Please enter your password"
            binding.edtPassword.requestFocus()
            return
        }else{
            loading(true)
            fAuth.signInWithEmailAndPassword(edtEmail, edtPassword).addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    userAuth = fAuth.currentUser!!
                    if(userAuth.isEmailVerified){
                        loading(false)
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finishAffinity()
                    }else{
                        loading(false)
                        showToast("Please verify your email")
                    }
                }else{
                    loading(false)
                    showToast("Wrong password or email")
                }
            }
        }
    }
}