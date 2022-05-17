package com.rx.diabeticlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.rx.diabeticlife.doctor.activity.Login2Activity
import com.rx.diabeticlife.patient.activity.LoginActivity
import com.rx.diabeticlife.trainer.activity.Login3Activity

class ChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)


        findViewById<AppCompatButton>(R.id.doctorApp).setOnClickListener {
            val mainIntent = Intent(this@ChooseActivity, Login2Activity::class.java)
            startActivity(mainIntent)
        }

        findViewById<AppCompatButton>(R.id.patientApp).setOnClickListener {
            val mainIntent = Intent(this@ChooseActivity, LoginActivity::class.java)
            startActivity(mainIntent)
        }

        findViewById<AppCompatButton>(R.id.trainerApp).setOnClickListener {
            val mainIntent = Intent(this@ChooseActivity, Login3Activity::class.java)
            startActivity(mainIntent)
        }
    }
}