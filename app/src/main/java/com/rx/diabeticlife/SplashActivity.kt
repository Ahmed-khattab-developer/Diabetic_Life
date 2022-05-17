package com.rx.diabeticlife

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rx.diabeticlife.SessionManagement.Constants.TYPE
import com.rx.diabeticlife.doctor.activity.MainDoctorActivity
import com.rx.diabeticlife.patient.activity.MainPatientActivity
import com.rx.diabeticlife.trainer.activity.MainTrainerActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var iSessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        iSessionManagement = SessionManagement(this)

        Handler().postDelayed({

            if (iSessionManagement.isLoggedIn()) {
                when (iSessionManagement.getUserDetails()[TYPE]) {
                    "patient" -> {
                        val intent =
                            Intent(this@SplashActivity, MainPatientActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                    "doctor" -> {
                        val intent =
                            Intent(this@SplashActivity, MainDoctorActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                    "trainer" -> {
                        val intent =
                            Intent(this@SplashActivity, MainTrainerActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                val mainIntent = Intent(this@SplashActivity, ChooseActivity::class.java)
                startActivity(mainIntent)
                finish()
            }


        }, 2000)


    }
}