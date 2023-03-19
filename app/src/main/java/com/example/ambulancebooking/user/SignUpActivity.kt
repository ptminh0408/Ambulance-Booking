package com.example.ambulancebooking.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.ambulancebooking.databinding.ActivitySignUpBinding
import com.example.ambulancebooking.model.Users
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var database : DatabaseReference
    private lateinit var userAuth : FirebaseUser
    private lateinit var fAuth : FirebaseAuth
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseAppCheck : FirebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        fAuth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener{
            signUp()
        }
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnSignUp.visibility = View.GONE
            binding.ProgressBar.visibility = View.VISIBLE
        }else{
            binding.btnSignUp.visibility = View.VISIBLE
            binding.ProgressBar.visibility = View.GONE
        }
    }

    private fun signUp() {

        val edtName = binding.edtName.text.toString().trim()
        val edtEmail = binding.edtEmail.text.toString().trim()
        val edtPassword = binding.edtPassword.text.toString().trim()
        val edtConfirmPassword = binding.edtConfirmPassword.text.toString().trim()

        if(edtEmail.isEmpty()){
            binding.edtName.error = "Please enter your email"
            binding.edtName.requestFocus()
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches()){
            binding.edtEmail.error = "Invalid email"
            binding.edtEmail.requestFocus()
            return
        }else if(edtName.isEmpty()){
            binding.edtEmail.error = "Please enter your name"
            binding.edtEmail.requestFocus()
            return
        }else if(edtPassword.isEmpty()){
            binding.edtPassword.error = "Please enter your password"
            binding.edtPassword.requestFocus()
            return
        }else if(edtPassword.length < 6){
            binding.edtPassword.error = "Your password must be bigger or equal 6 characters"
            binding.edtPassword.requestFocus()
            return
        }else if(edtConfirmPassword != edtPassword){
            binding.edtConfirmPassword.error = "Password doesn't match"
            binding.edtConfirmPassword.requestFocus()
            return
        }else{
            loading(true)
            fAuth.createUserWithEmailAndPassword(edtEmail, edtPassword).addOnCompleteListener {task ->
                userAuth = FirebaseAuth.getInstance().currentUser!!
                userID = fAuth.uid!!
                if(task.isSuccessful){
                    if(userAuth.isEmailVerified){
                        loading(false)
                        showToast("Email used. Please try another one")
                    }else{
                        loading(false)
                        userAuth.sendEmailVerification()
                        database = FirebaseDatabase.getInstance().getReference("Users")
                        val user = Users(edtName, edtEmail, null, null, null)
                        database.child(userID).setValue(user)
                        showToast("Registered Successfully")
                        startActivity(Intent(applicationContext, SignInActivity::class.java))
                        finishAffinity()
                    }
                }else{
                    loading(false)
                    showToast("Email already existed")
                }
            }
        }
    }
}

