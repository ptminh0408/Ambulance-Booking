package com.example.ambulancebooking.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.ambulancebooking.databinding.ActivityDriverSignUpBinding
import com.example.ambulancebooking.model.Drivers
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_driver_sign_up.*

class DriverSignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDriverSignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseAppCheck : FirebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        setListeners()
    }

    private fun setListeners(){
        binding.backSignIn.setOnClickListener {
            startActivity(Intent(this, DriverSignInActivity::class.java))
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            driverSignUp()
        }
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnSignUp.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSignUp.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun driverSignUp() {

        val email = binding.edtDriverEmail.text.toString().trim()
        val name = binding.edtDriverName.text.toString().trim()
        val phone = binding.edtDriverPhone.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.edtDriverEmail.error = "Please enter your email"
            binding.edtDriverEmail.requestFocus()
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtDriverEmail.error = "Invalid email"
            binding.edtDriverEmail.requestFocus()
            return
        }else if(name.isEmpty()){
            binding.edtDriverName.error = "Please enter your name"
            binding.edtDriverName.requestFocus()
            return
        }else if(phone.isEmpty()){
            binding.edtDriverPhone.error = "Please enter your phone"
            binding.edtDriverPhone.requestFocus()
            return
        }else if(password.isEmpty()){
            binding.edtPassword.error = "Please enter your password"
            binding.edtPassword.requestFocus()
            return
        }else if(password.length < 6){
            binding.edtPassword.error = "Your password must be bigger or equal 6 characters"
            binding.edtPassword.requestFocus()
            return
        }else if(confirmPassword != password){
            binding.edtConfirmPassword.error = "Password doesn't match"
            binding.edtConfirmPassword.requestFocus()
            return
        }else{
            loading(true)
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                firebaseUser = firebaseAuth.currentUser!!
                userID = firebaseAuth.uid!!
                if(task.isSuccessful){
                    if(firebaseUser.isEmailVerified){
                        loading(false)
                        showToast("Email used. Please try another one")
                    }else{
                        loading(false)
                        firebaseUser.sendEmailVerification()
                        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers")
                        val driver = Drivers(email, name, phone)
                        databaseReference.child(userID).setValue(driver)
                        showToast("Registered Successfully. Please verify your email")
                        startActivity(Intent(this, DriverSignInActivity::class.java))
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