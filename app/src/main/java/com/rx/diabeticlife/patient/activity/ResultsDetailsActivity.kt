package com.rx.diabeticlife.patient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.rx.diabeticlife.R
import com.squareup.picasso.Picasso

class ResultsDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_details)

        Picasso.with(this).load(intent.getStringExtra("doctorImage"))
            .into(findViewById<ImageView>(R.id.image))

        Picasso.with(this).load(intent.getStringExtra("resultDoctorImage"))
            .into(findViewById<ImageView>(R.id.image2))

        Picasso.with(this).load(intent.getStringExtra("resultTrainerImage"))
            .into(findViewById<ImageView>(R.id.image3))

        findViewById<TextView>(R.id.doctorName).text = "Dr : " + intent.getStringExtra("doctorName")
        findViewById<TextView>(R.id.doctorFees).text = "Fees : " + intent.getStringExtra("doctorFees")+"EG"
        findViewById<TextView>(R.id.trainerName).text =
            "Trainer : " + intent.getStringExtra("trainerName")
        findViewById<TextView>(R.id.trainerFees).text =
            "Fees : " +  intent.getStringExtra("trainerFees")+"EG"
        findViewById<TextView>(R.id.date).text = "date : " + intent.getStringExtra("date")
        findViewById<TextView>(R.id.sugar).text =
            "sugarLevel : " + intent.getStringExtra("sugarLevel")
        findViewById<TextView>(R.id.target).text =
            "targetLevel : " + intent.getStringExtra("targetLevel")
        findViewById<TextView>(R.id.weight).text = "weight : " + intent.getStringExtra("weight")
        findViewById<TextView>(R.id.height).text = "height : " + intent.getStringExtra("height")

        intent.getStringExtra("doctorId")
        intent.getStringExtra("doctorName")
        intent.getStringExtra("doctorImage")
        intent.getStringExtra("trainerId")
        intent.getStringExtra("trainerName")
        intent.getStringExtra("trainerAge")
        intent.getStringExtra("trainerGender")
        intent.getStringExtra("patientId")
        intent.getStringExtra("patientName")
        intent.getStringExtra("patientAge")
        intent.getStringExtra("patientGender")
        intent.getStringExtra("sugarLevel")
        intent.getStringExtra("targetLevel")
        intent.getStringExtra("weight")
        intent.getStringExtra("height")
        intent.getStringExtra("date")

    }
}