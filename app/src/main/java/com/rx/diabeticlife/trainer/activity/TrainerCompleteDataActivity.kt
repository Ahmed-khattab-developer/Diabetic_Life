package com.rx.diabeticlife.trainer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.trainer.pojo.Trainer

class TrainerCompleteDataActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var radioSexGroup: RadioGroup
    private var gender: String? = "male"

    private lateinit var iSessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainer_complete_data)
        iSessionManagement = SessionManagement(this)
        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("trainer").child(auth.uid.toString())

        radioSexGroup = findViewById(R.id.genderRG)

        radioSexGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioM -> gender = "male"
                R.id.radioF -> gender = "female"
            }
        }


        findViewById<Button>(R.id.button_save).setOnClickListener {
            when {
                findViewById<EditText>(R.id.et_name).text.trim().isEmpty() -> {
                    Toast.makeText(applicationContext, "Trainer Name Required ", Toast.LENGTH_SHORT)
                        .show()
                }
                findViewById<EditText>(R.id.et_age).text.trim().isEmpty() -> {
                    Toast.makeText(applicationContext, "Trainer Age Required ", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    addDataToFirebase(
                        findViewById<EditText>(R.id.et_name).text.toString(),
                        findViewById<EditText>(R.id.et_age).text.toString(),
                        gender.toString()
                    )
                }
            }
        }

    }

    private fun addDataToFirebase(name: String, age: String, gender: String) {

        val trainer = Trainer(auth.uid.toString(), name, age, gender)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.setValue(trainer)

                iSessionManagement.createLoginSession(
                    true, auth.uid.toString(), "trainer", name, age, gender, ""
                )

                val intent =
                    Intent(this@TrainerCompleteDataActivity, MainTrainerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@TrainerCompleteDataActivity, "Fail to add data $error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}