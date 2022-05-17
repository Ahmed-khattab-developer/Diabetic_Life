package com.rx.diabeticlife.doctor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            mainIntent.putExtra("trainerId", intent.getStringExtra("trainerId"))
            mainIntent.putExtra("trainerName", intent.getStringExtra("trainerName"))
            mainIntent.putExtra("trainerAge", intent.getStringExtra("trainerAge"))
            mainIntent.putExtra("trainerGender", intent.getStringExtra("trainerGender"))
            mainIntent.putExtra("patientId", intent.getStringExtra("patientId"))
            mainIntent.putExtra("patientName", intent.getStringExtra("patientName"))
            mainIntent.putExtra("patientAge", intent.getStringExtra("patientAge"))
            mainIntent.putExtra("patientGender", intent.getStringExtra("patientGender"))
            mainIntent.putExtra("sugarLevel", intent.getStringExtra("sugarLevel"))
            mainIntent.putExtra("targetLevel", intent.getStringExtra("targetLevel"))
            mainIntent.putExtra("weight", intent.getStringExtra("weight"))
            mainIntent.putExtra("height", intent.getStringExtra("height"))
            mainIntent.putExtra("date", intent.getStringExtra("date"))
            mainIntent.putExtra("type", intent.getStringExtra("type"))
            mainIntent.putExtra("requestId", intent.getStringExtra("requestId"))
            startActivity(mainIntent)

        }
    }
}