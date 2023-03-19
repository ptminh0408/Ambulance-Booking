package com.example.ambulancebooking.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ambulancebooking.MainActivity
import com.example.ambulancebooking.databinding.ActivitySettingsBinding
import com.example.ambulancebooking.user.FingerprintActivity
import com.example.ambulancebooking.Content.LanguageSelectionActivity
import com.example.ambulancebooking.user.ChangePasswordActivity
import com.example.ambulancebooking.user.SignInOptionActivity
import com.google.firebase.auth.FirebaseAuth

class Settings : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private lateinit var fAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fAuth = FirebaseAuth.getInstance()
        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners(){
        binding.logoutButton.setOnClickListener {
            fAuth.signOut()
            startActivity(Intent(applicationContext, SignInOptionActivity::class.java))
            finishAffinity()
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(applicationContext, ChangePasswordActivity::class.java))
        }

        binding.tvDevice.text = "BRAND: ${Build.BRAND}" +
                "\nMODEL: ${Build.MODEL}" +
                "\nPRODUCT: ${Build.PRODUCT}" +
                "\nDISPLAY: ${Build.DISPLAY}" +
                "\nFINGERPRINT: ${Build.FINGERPRINT}" +
                "\nID: ${Build.ID}" +
                "\nUSER: ${Build.USER}"

        binding.btnWifiCheck.setOnClickListener {
            val uri = Uri.parse("https://speedtest.vn/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}