package com.rx.diabeticlife.patient.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.rx.diabeticlife.R
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    var number: String = ""

    lateinit var auth: FirebaseAuth

    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.button_otp).setOnClickListener {
            login()
        }
    }

    private fun login() {
        number = findViewById<EditText>(R.id.et_phone_number).text.trim().toString()

        if (number.isNotEmpty()) {
            number = "+2$number"
            sendVerificationCode(number)
        } else {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(number: String) {

        val auth = PhoneAuthOptions
            .newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.MILLISECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                }

                override fun onCodeSent(
                    verificationId: String, resendToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    this@LoginActivity.resendToken = resendToken

                    val intent = Intent(applicationContext, OtpActivity::class.java)
                    intent.putExtra("storedVerificationId", storedVerificationId)
                    startActivity(intent)
                    finish()
                }

            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(auth)
    }
}