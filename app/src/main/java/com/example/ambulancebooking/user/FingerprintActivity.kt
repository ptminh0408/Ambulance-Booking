package com.example.ambulancebooking.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.ambulancebooking.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_fingerprint.*
import kotlinx.android.synthetic.main.activity_fingerprint.backButton
import java.util.concurrent.Executor

class FingerprintActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)


        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this@FingerprintActivity, executor, object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                authStatus.text = "Authentication Error: $errString"
//                Toast.makeText(this@FingerprintActivity, "Authentication Error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                authStatus.text = "Authentication Succeed...!"
//                Toast.makeText(this@FingerprintActivity, "Authentication Succeed---!", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

                authStatus.text = "Authentication Failed...!"
//                Toast.makeText(this@FingerprintActivity, "Authentication Failed...!", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using fingerprint authentication")
            .setNegativeButtonText("Use App Password instead")
            .build()

        var checkAnim = findViewById<LottieAnimationView>(R.id.btnSwitch)
        checkAnim.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        backButton.setOnClickListener(){
            onBackPressed()
        }
    }
}