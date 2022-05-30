package com.rx.diabeticlife.patient.activity

import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.SessionManagement.Constants.AGE
import com.rx.diabeticlife.SessionManagement.Constants.GENDER
import com.rx.diabeticlife.SessionManagement.Constants.NAME
import com.rx.diabeticlife.patient.pojo.Request
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SendRequestActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    var sugarList = ArrayList<String>()
    var targetList = ArrayList<String>()
    var spinner: Spinner? = null
    var spinner2: Spinner? = null
    var buttonSend: Button? = null
    var sugarLevel: String? = null
    var targetLevel: String? = null
    var doctorId: String? = null
    var doctorName: String? = null
    var doctorImage: String? = null
    var trainerId: String? = null
    var trainerName: String? = null
    var trainerAge: String? = null
    var trainerGender: String? = null

    private lateinit var iSessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request)

        iSessionManagement = SessionManagement(this)

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("request")

        doctorId = intent.getStringExtra("doctor_id")
        doctorName = intent.getStringExtra("doctor_name")
        doctorImage = intent.getStringExtra("doctor_image")
        trainerId = intent.getStringExtra("trainer_id")
        trainerName = intent.getStringExtra("trainer_name")
        trainerAge = intent.getStringExtra("trainer_age")
        trainerGender = intent.getStringExtra("trainer_gender")

        spinner = findViewById(R.id.spinner_sample)
        spinner2 = findViewById(R.id.spinner_sample_2)
        buttonSend = findViewById(R.id.button_send)
        buttonSend?.setOnClickListener(this)

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
                textView.setTextColor(Color.WHITE)

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
                textView.setTextColor(Color.WHITE)

                targetLevel = targetList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, targetList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2!!.adapter = bb

    }

    override fun onClick(v: View?) {

        when {
            sugarLevel == null -> {
                Toast.makeText(this, "Enter Sugar Level", Toast.LENGTH_SHORT).show()
            }
            targetLevel == null -> {
                Toast.makeText(this, "Enter Target Level", Toast.LENGTH_SHORT).show()
            }
            findViewById<EditText>(R.id.ed_weight).text.trim().isEmpty() -> {
                Toast.makeText(this, "Enter Weight", Toast.LENGTH_SHORT).show()
            }
            findViewById<EditText>(R.id.ed_height).text.trim().isEmpty() -> {
                Toast.makeText(this, "Enter Height", Toast.LENGTH_SHORT).show()
            }
            else -> {
                addDataToFirebase()
            }
        }
    }


    private fun addDataToFirebase() {

        val myDate: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-M-yyyy - HH:mm")
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("dd-M-yyy - HH:mma")
            formatter.format(date)
        }

        val child = databaseReference.push().key

        val request = Request(
            child!!, doctorId!!, doctorName!!, doctorImage!!, trainerId!!, trainerName!!,
            trainerAge!!, trainerGender!!,
            auth.uid.toString(), iSessionManagement.getUserDetails()[NAME]!!,
            iSessionManagement.getUserDetails()[AGE]!!,
            iSessionManagement.getUserDetails()[GENDER]!!,
            sugarLevel!!, targetLevel!!,
            findViewById<EditText>(R.id.ed_weight).text.toString(),
            findViewById<EditText>(R.id.ed_height).text.toString(),
            myDate
        )

        databaseReference.child(child).setValue(
            request
        ) { error, ref ->

            if (error != null) {
                Toast.makeText(
                    this@SendRequestActivity, "Fail to add data $error", Toast.LENGTH_SHORT
                ).show()
            } else {

                Toast.makeText(
                    this@SendRequestActivity, "send your request successfully",
                    Toast.LENGTH_SHORT
                ).show()

                val intent =
                    Intent(this@SendRequestActivity, MainPatientActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}