package com.example.ambulancebooking.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ambulancebooking.databinding.ActivityDriverChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DriverChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDriverChangePasswordBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirm.setOnClickListener {
            changePassword()
        }
    }

    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnConfirm.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnConfirm.visibility = View.VISIBLE
        }
    }

    private fun changePassword() {
        val currentPassword = binding.edtCurrentPassword.text.toString().trim()
        val newPassword = binding.edtNewPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        if(currentPassword.isEmpty()){
            binding.edtCurrentPassword.error = "Please enter your current password"
            binding.edtCurrentPassword.requestFocus()
            return
        }else if(newPassword.isEmpty()){
            binding.edtNewPassword.error = "Please enter your current password"
            binding.edtNewPassword.requestFocus()
            return
        }else if(newPassword.length < 6){
            binding.edtNewPassword.error = "Password must be longer or equal 6 characters"
            binding.edtNewPassword.requestFocus()
            return
        }else if(confirmPassword != newPassword){
            binding.edtConfirmPassword.error = "Password doesn't match"
            binding.edtConfirmPassword.requestFocus()
            return
        }else{
            loading(true)
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseUser = firebaseAuth.currentUser!!
            val authCredential = EmailAuthProvider.getCredential(firebaseUser.email!!, currentPassword)
            firebaseUser.reauthenticate(authCredential).addOnSuccessListener {
                firebaseUser.updatePassword(newPassword).addOnSuccessListener {
                    showToast("Change Password Successful")
                    startActivity(Intent(this@DriverChangePasswordActivity, DriverMainActivity::class.java))
                    finishAffinity()
                    loading(false)
                }.addOnFailureListener {
                    showToast("Change Password Failed")
                    loading(false)
                }
            }.addOnFailureListener {
                binding.edtCurrentPassword.error = "Wrong current password"
                binding.edtCurrentPassword.requestFocus()
                loading(false)
            }
        }
    }
}