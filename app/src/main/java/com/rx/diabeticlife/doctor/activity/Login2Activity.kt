package com.rx.diabeticlife.doctor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.rx.diabeticlife.R
import java.util.concurrent.TimeUnit

class Login2Activity : AppCompatActivity() {

    var number: String = ""

    lateinit var auth: FirebaseAuth

    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        auth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.progress_bar)

        findViewById<Button>(R.id.button_otp).setOnClickListener {
            login()
        }
    }

    private fun login() {
        number = findViewById<EditText>(R.id.et_phone_number).text.trim().toString()

        when {
            number.isEmpty() -> {
                Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
            }
            number.length != 11 -> {
                Toast.makeText(this, "Enter valid number", Toast.LENGTH_SHORT).show()
            }
            else -> {
                progressBar.visibility = View.VISIBLE
                number = "+2$number"
                sendVerificationCode(number)
            }
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
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@Login2Activity, p0.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    resendToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    this@Login2Activity.resendToken = resendToken

                    progressBar.visibility = View.GONE
                    val intent = Intent(applicationContext, Otp2Activity::class.java)
                    intent.putExtra("storedVerificationId", storedVerificationId)
                    startActivity(intent)
                    finish()
                }

            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(auth)

    }
}