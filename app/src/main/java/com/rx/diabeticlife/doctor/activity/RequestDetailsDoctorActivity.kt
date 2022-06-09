package com.rx.diabeticlife.doctor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.rx.diabeticlife.R

class RequestDetailsDoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_details_doctor)

        findViewById<TextView>(R.id.name).text = intent.getStringExtra("patientName")
        findViewById<TextView>(R.id.gender).text = intent.getStringExtra("patientGender")
        findViewById<TextView>(R.id.age).text = intent.getStringExtra("patientAge")
        findViewById<TextView>(R.id.date).text = intent.getStringExtra("date")
        findViewById<TextView>(R.id.sugar).text = intent.getStringExtra("sugarLevel")
        findViewById<TextView>(R.id.target).text = intent.getStringExtra("targetLevel")
        findViewById<TextView>(R.id.weight).text = intent.getStringExtra("weight")
        findViewById<TextView>(R.id.height).text = intent.getStringExtra("height")

        findViewById<TextView>(R.id.send).setOnClickListener {

            val mainIntent = Intent(this, DoctorResultActivity::class.java)
            mainIntent.putExtra("doctorId", intent.getStringExtra("doctorId"))
            mainIntent.putExtra("doctorName", intent.getStringExtra("doctorName"))
            mainIntent.putExtra("doctorImage", intent.getStringExtra("doctorImage"))
            mainIntent.putExtra("doctorFees", intent.getStringExtra("doctorFees"))
            mainIntent.putExtra("trainerId", intent.getStringExtra("trainerId"))
            mainIntent.putExtra("trainerName", intent.getStringExtra("trainerName"))
            mainIntent.putExtra("trainerAge", intent.getStringExtra("trainerAge"))
            mainIntent.putExtra("trainerGender", intent.getStringExtra("trainerGender"))
            mainIntent.putExtra("trainerFees", intent.getStringExtra("trainerFees"))
            mainIntent.putExtra("patientId", intent.getStringExtra("patientId"))
            mainIntent.putExtra("patientName", intent.getStringExtra("patientName"))
            mainIntent.putExtra("patientAge", intent.getStringExtra("patientAge"))
            mainIntent.putExtra("patientGender", intent.getStringExtra("patientGender"))
            mainIntent.putExtra("sugarLevel", intent.getStringExtra("sugarLevel"))
            mainIntent.putExtra("targetLevel", intent.getStringExtra("targetLevel"))
            mainIntent.putExtra("weight", intent.getStringExtra("weight"))
            mainIntent.putExtra("height", intent.getStringExtra("height"))
            mainIntent.putExtra("date", intent.getStringExtra("date"))
            mainIntent.putExtra("doctorUpload", intent.getStringExtra("doctorUpload"))
            mainIntent.putExtra("trainerUpload", intent.getStringExtra("trainerUpload"))
            mainIntent.putExtra("type", intent.getStringExtra("type"))
            mainIntent.putExtra("requestId", intent.getStringExtra("requestId"))
            startActivity(mainIntent)

        }


        if (intent.getStringExtra("type") == "doctor") {
            if (intent.getStringExtra("doctorUpload") == "1") {
                findViewById<Button>(R.id.send).visibility = View.GONE
            }
        } else {
            if (intent.getStringExtra("trainerUpload") == "1") {
                findViewById<Button>(R.id.send).visibility = View.GONE
            }
        }
    }
}