package com.rx.diabeticlife.patient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.patient.fragment.NewRequestFragment
import com.rx.diabeticlife.patient.fragment.RequestsFragment
import com.rx.diabeticlife.patient.fragment.ResultsFragment

class MainPatientActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private lateinit var iSessionManagement: SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_patient)
        iSessionManagement = SessionManagement(this)
        loadFragment(NewRequestFragment())
        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.new_request -> {
                    loadFragment(NewRequestFragment())
                }
                R.id.requests -> {
                    loadFragment(RequestsFragment())
                }
                R.id.results -> {
                    loadFragment(ResultsFragment())
                }
            }
            true
        }

        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            iSessionManagement.logoutUser()
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, fragment)
        transaction.commit()
    }
}