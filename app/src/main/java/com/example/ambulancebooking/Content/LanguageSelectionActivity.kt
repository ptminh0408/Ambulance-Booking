package com.example.ambulancebooking.Content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.example.ambulancebooking.R
import kotlinx.android.synthetic.main.activity_language_selection.*

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        val r1 = findViewById<RadioButton>(R.id.radioButton)
        r1.isChecked = true

        backButton.setOnClickListener(){
            onBackPressed()
        }
    }
}