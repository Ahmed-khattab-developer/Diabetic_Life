package com.rx.diabeticlife.trainer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.trainer.pojo.Trainer

class Login3Activity : AppCompatActivity() {

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
        setContentView(R.layout.activity_login3)
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

        databaseReference = firebaseDatabase.getReference("trainer")

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            login()
        }

        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, SignUp3Activity::class.java)
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

        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast
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

                    val trainer: Trainer? =
                        dataSnapshot.child(auth.uid.toString()).getValue(Trainer::class.java)
                    iSessionManagement.createLoginSession(
                        true, auth.uid.toString(),
                        "trainer", trainer?.name, trainer?.fees,trainer?.age, trainer?.gender,
                        "", "", "", "", ""
                    )

                    progressBar.visibility = View.GONE
                    val intent = Intent(this@Login3Activity, MainTrainerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {

                    val intent =
                        Intent(this@Login3Activity, TrainerCompleteDataActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}