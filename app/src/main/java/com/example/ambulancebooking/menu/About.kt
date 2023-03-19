package com.example.ambulancebooking.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.R
import kotlinx.android.synthetic.main.activity_about.*

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        backButton.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}