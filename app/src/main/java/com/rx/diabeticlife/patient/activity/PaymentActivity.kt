package com.rx.diabeticlife.patient.activity

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.patient.pojo.Request
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var iSessionManagement: SessionManagement

    private lateinit var mCardNumber: TextInputLayout
    private lateinit var mCardExpiry: TextInputLayout
    private lateinit var mCardCVV: TextInputLayout

    var doctorId: String? = null
    var doctorName: String? = null
    var doctorImage: String? = null
    var doctorFees: String? = null
    var trainerId: String? = null
    var trainerName: String? = null
    var trainerAge: String? = null
    var trainerGender: String? = null
    var trainerFees: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("request")
        auth = FirebaseAuth.getInstance()
        iSessionManagement = SessionManagement(this)

        doctorId = intent.getStringExtra("doctor_id")
        doctorName = intent.getStringExtra("doctor_name")
        doctorImage = intent.getStringExtra("doctor_image")
        doctorFees = intent.getStringExtra("doctor_fees")
        trainerId = intent.getStringExtra("trainer_id")
        trainerName = intent.getStringExtra("trainer_name")
        trainerAge = intent.getStringExtra("trainer_age")
        trainerGender = intent.getStringExtra("trainer_gender")
        trainerFees = intent.getStringExtra("trainer_fees")

        findViewById<TextView>(R.id.doctorFees).text = "doctor fees : " + doctorFees + "EG"
        findViewById<TextView>(R.id.trainerFees).text = "trainer fees : " + trainerFees + "EG"

        initializeFormVariables()
    }

    private fun initializeFormVariables() {
        mCardNumber = findViewById(R.id.til_card_number)
        mCardExpiry = findViewById(R.id.til_card_expiry)
        mCardCVV = findViewById(R.id.til_card_cvv)
        // this is used to add a forward slash (/) between the cards expiry month
        // and year (11/21). After the month is entered, a forward slash is added
        // before the year
        mCardExpiry.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 2 && !s.toString().contains("/")) {
                    s!!.append("/")
                }
            }
        })

        val button = findViewById<Button>(R.id.btn_make_payment)
        button.setOnClickListener { performCharge() }
    }

    private fun performCharge() {

        val cardNumber = mCardNumber.editText!!.text.toString()
        val cardExpiry = mCardExpiry.editText!!.text.toString()
        val cvv = mCardCVV.editText!!.text.toString()

        when {
            cardNumber.isEmpty() -> {
                Toast.makeText(this, "Enter card number", Toast.LENGTH_SHORT).show()
            }
            cardNumber.length != 14 -> {
                Toast.makeText(this, "Enter valid card number", Toast.LENGTH_SHORT).show()
            }
            cardExpiry.isEmpty() -> {
                Toast.makeText(this, "Enter card expiry", Toast.LENGTH_SHORT).show()
            }
            cvv.isEmpty() -> {
                Toast.makeText(this, "enter cvv", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val cardExpiryArray = cardExpiry.split("/").toTypedArray()
                val expiryMonth = cardExpiryArray[0].toInt()
                val expiryYear = cardExpiryArray[1].toInt()
                var amount = 100
                amount *= 100
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
            child!!,
            doctorId!!,
            doctorName!!,
            doctorImage!!,
            doctorFees!!,
            trainerId!!,
            trainerName!!,
            trainerAge!!,
            trainerGender!!,
            trainerFees!!,
            auth.uid.toString(),
            iSessionManagement.getUserDetails()[SessionManagement.Constants.NAME]!!,
            iSessionManagement.getUserDetails()[SessionManagement.Constants.AGE]!!,
            iSessionManagement.getUserDetails()[SessionManagement.Constants.GENDER]!!,
            iSessionManagement.getUserDetails()[SessionManagement.Constants.BLOODSUGARLEVEL]!!,
            iSessionManagement.getUserDetails()[SessionManagement.Constants.TARGET]!!,
            iSessionManagement.getUserDetails()[SessionManagement.Constants.WEIGHT]!!,
            iSessionManagement.getUserDetails()[SessionManagement.Constants.HEIGHT]!!,
            myDate,
            "0",
            "0"
        )

        databaseReference.child(child).setValue(
            request
        ) { error, _ ->

            if (error != null) {
                Toast.makeText(this, "Fail to add data $error", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "send your request successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainPatientActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}