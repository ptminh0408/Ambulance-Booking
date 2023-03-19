package com.example.ambulancebooking.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ambulancebooking.R
import com.example.ambulancebooking.user.SignInOptionActivity
import kotlinx.android.synthetic.main.activity_driver_or_user.*

class DriverOrUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_or_user)

        btnDriver.setOnClickListener{
            val intent = Intent(this, DriverSignInActivity::class.java)
            startActivity(intent)
        }
        btnUser.setOnClickListener{
            val intent = Intent(this, SignInOptionActivity::class.java)
            startActivity(intent)
        }

    }
}