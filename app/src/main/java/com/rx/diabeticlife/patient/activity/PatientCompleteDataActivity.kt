package com.rx.diabeticlife.patient.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.patient.pojo.Patient
import java.util.ArrayList

class PatientCompleteDataActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var radioSexGroup: RadioGroup
    private var gender: String? = "male"

    private lateinit var iSessionManagement: SessionManagement

    var sugarList = ArrayList<String>()
    var targetList = ArrayList<String>()
    var spinner: Spinner? = null
    var spinner2: Spinner? = null
    var sugarLevel: String? = null
    var targetLevel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_complete_data)
        iSessionManagement = SessionManagement(this)
        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("patient").child(auth.uid.toString())

        radioSexGroup = findViewById(R.id.genderRG)

        radioSexGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioM -> gender = "male"
                R.id.radioF -> gender = "female"
            }
        }

        spinner = findViewById(R.id.spinner_sample)
        spinner2 = findViewById(R.id.spinner_sample_2)

        sugarList.add("low")
        sugarList.add("medium")
        sugarList.add("high")

        targetList.add("lose weight")
        targetList.add("gain muscles")
        targetList.add("get stronger")

        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val textView: TextView = parent.getChildAt(0) as TextView
                textView.setTextColor(Color.BLACK)
                sugarLevel = sugarList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, sugarList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = aa

        spinner2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val textView: TextView = parent.getChildAt(0) as TextView
                textView.setTextColor(Color.BLACK)
                targetLevel = targetList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, targetList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2!!.adapter = bb

        findViewById<Button>(R.id.button_save).setOnClickListener {
            when {
                findViewById<EditText>(R.id.et_name).text.trim().isEmpty() -> {
                    Toast.makeText(applicationContext, "Patient Name Required ", Toast.LENGTH_SHORT)
                        .show()
                }
                findViewById<EditText>(R.id.et_age).text.trim().isEmpty() -> {
                    Toast.makeText(applicationContext, "Patient Age Required ", Toast.LENGTH_SHORT)
                        .show()
                }
                findViewById<EditText>(R.id.ed_weight).text.trim().isEmpty() -> {
                    Toast.makeText(this, "Enter Weight", Toast.LENGTH_SHORT).show()
                }
                findViewById<EditText>(R.id.ed_height).text.trim().isEmpty() -> {
                    Toast.makeText(this, "Enter Height", Toast.LENGTH_SHORT).show()
                }
                sugarLevel == null -> {
                    Toast.makeText(this, "Enter Sugar Level", Toast.LENGTH_SHORT).show()
                }
                targetLevel == null -> {
                    Toast.makeText(this, "Enter Target Level", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    addDataToFirebase(
                        findViewById<EditText>(R.id.et_name).text.toString(),
                        findViewById<EditText>(R.id.et_age).text.toString(), gender.toString()
                    )
                }
            }
        }

    }

    private fun addDataToFirebase(name: String, age: String, gender: String) {

        val patient = Patient(
            auth.uid.toString(), name, age, gender,
            findViewById<EditText>(R.id.ed_weight).text.toString(),
            findViewById<EditText>(R.id.ed_height).text.toString(),
            sugarLevel!!, targetLevel!!
        )

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.setValue(patient)
                iSessionManagement.createLoginSession(
                    true, auth.uid.toString(), "patient", name,"", age, gender, "",
                    findViewById<EditText>(R.id.ed_weight).text.toString(),
                    findViewById<EditText>(R.id.ed_height).text.toString(),
                    sugarLevel!!, targetLevel!!
                )

                val intent =
                    Intent(this@PatientCompleteDataActivity, MainPatientActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@PatientCompleteDataActivity, "Fail to add data $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}