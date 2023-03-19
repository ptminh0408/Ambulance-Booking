package com.example.ambulancebooking.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.R
import com.example.ambulancebooking.databinding.ActivityProfileBinding
import com.example.ambulancebooking.model.Users
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userID : String
    private val IMAGE_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseAppCheck : FirebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        firebaseAuth = FirebaseAuth.getInstance()
        setListeners()
        showProfile()
    }

    private fun setListeners(){
        binding.imgProfile.setOnClickListener {
            selectImage()
        }

        binding.btnOk.setOnClickListener {
            updateProfile()
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnOk.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnOk.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showProfile(){
        loading(true)
        firebaseUser = firebaseAuth.currentUser!!
        userID = firebaseUser.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user : Users? = snapshot.getValue(Users::class.java)
                for (dataSnapshot in snapshot.children) {
                    if (dataSnapshot.key == userID) {
                        user = dataSnapshot.getValue(Users::class.java)
                        binding.edtEmail.setText(user!!.email)
                        binding.edtName.setText(user.name)
                        binding.edtPhone.setText(user.phone)
                        binding.edtAddress.setText(user.address)
                        if(user.image == null){
                            binding.imgProfile.setImageResource(R.drawable.ic_baseline_person_24)
                        }else{
                            Picasso.get().load(user.image).into(binding.imgProfile)
                        }
                    }
                }
                loading(false)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateProfile(){
        val email = binding.edtEmail.text.toString().trim()
        val name = binding.edtName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Email Invalid"
            binding.edtEmail.requestFocus()
        }else{
            loading(true)
            firebaseUser = firebaseAuth.currentUser!!
            userID = firebaseUser.uid
            val user = HashMap<String, Any>()
            user["email"] = email
            user["name"] = name
            user["phone"] = phone
            user["address"] = address
            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child(userID).updateChildren(user).addOnCompleteListener {task ->
                if(task.isSuccessful){
                    loading(false)
                    showToast("Updated Successful")
                }
            }.addOnFailureListener {
                loading(false)
                showToast("Updated Fail")
            }
        }
    }

    private fun selectImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            val imageUri : Uri = data?.data!!
            binding.imgProfile.setImageURI(imageUri)
            uploadImage(imageUri)
        }
    }

    private fun uploadImage(imageUri : Uri){
        loading(true)
        firebaseUser = firebaseAuth.currentUser!!
        userID = firebaseUser.uid
        val storageReference = FirebaseStorage.getInstance().getReference("users/$userID.jpg")
        storageReference.putFile(imageUri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(userID).child("image")
                databaseReference.setValue(it.toString())
                showToast("Change Avatar Successful")
                loading(false)
            }
        }
    }
}