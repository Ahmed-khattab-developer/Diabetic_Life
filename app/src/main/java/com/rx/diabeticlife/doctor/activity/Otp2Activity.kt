package com.rx.diabeticlife.doctor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.doctor.pojo.Doctor

class Otp2Activity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var iSessionManagement: SessionManagement

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp2)
        iSessionManagement = SessionManagement(this)

        auth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.progress_bar)

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("doctor")

        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        findViewById<Button>(R.id.login).setOnClickListener {
            val otp = findViewById<EditText>(R.id.et_otp).text.trim().toString()
            if (otp.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    getUserData()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun getUserData() {

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child(auth.uid.toString()).exists()) {

                    val doctor: Doctor? = dataSnapshot.child(auth.uid.toString()).getValue(Doctor::class.java)
                    iSessionManagement.createLoginSession(
                        true, auth.uid.toString(),
                        "doctor", doctor?.name, "", "", doctor?.image!!
                    )

                    progressBar.visibility = View.GONE
                    val intent = Intent(this@Otp2Activity, MainDoctorActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {

                    val intent =
                        Intent(this@Otp2Activity, DoctorCompleteDataActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}