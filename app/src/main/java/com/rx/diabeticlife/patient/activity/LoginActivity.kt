package com.rx.diabeticlife.patient.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.patient.pojo.Patient

class LoginActivity : AppCompatActivity() {

    private lateinit var tvRedirectSignUp: TextView
    lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    lateinit var btnLogin: Button

    lateinit var auth: FirebaseAuth

    lateinit var progressBar: ProgressBar

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var iSessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        iSessionManagement = SessionManagement(this)

        progressBar = findViewById(R.id.progress_bar)

        // View Binding
        tvRedirectSignUp = findViewById(R.id.tvRedirectSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmailAddress)
        etPass = findViewById(R.id.etPassword)

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("patient")

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            login()
        }

        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            // using finish() to end the activity
            finish()
        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()

        if (email.isBlank() || pass.isBlank()) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                getUserData()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserData() {

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child(auth.uid.toString()).exists()) {

                    val patient: Patient? =
                        dataSnapshot.child(auth.uid.toString()).getValue(Patient::class.java)
                    iSessionManagement.createLoginSession(
                        true,
                        auth.uid.toString(), "patient", patient?.name,"", patient?.age,
                        patient?.gender, "", patient?.weight, patient?.height,
                        patient?.bloodSugarLevel, patient?.target
                    )
                    progressBar.visibility = View.GONE
                    val intent = Intent(this@LoginActivity, MainPatientActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {

                    val intent =
                        Intent(this@LoginActivity, PatientCompleteDataActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}