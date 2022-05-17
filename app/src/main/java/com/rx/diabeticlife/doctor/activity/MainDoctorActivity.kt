package com.rx.diabeticlife.doctor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.doctor.adapter.RequestDoctorAdapter
import com.rx.diabeticlife.patient.pojo.Request

class MainDoctorActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    var adapter: RequestDoctorAdapter? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var iSessionManagement: SessionManagement

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_doctor)
        iSessionManagement = SessionManagement(this)


        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            iSessionManagement.logoutUser()
        }
        iSessionManagement = SessionManagement(this)

        findViewById<TextView>(R.id.user).text =
            "Hello " + iSessionManagement.getUserDetails()[SessionManagement.Constants.NAME]
        recyclerView = findViewById(R.id.recycler)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("request")

        recyclerView?.layoutManager = LinearLayoutManager(this)

        val options: FirebaseRecyclerOptions<Request> = FirebaseRecyclerOptions.Builder<Request>()
            .setQuery(
                databaseReference.orderByChild("doctorId").equalTo(auth.uid.toString()),
                Request::class.java
            )
            .build()
        adapter = RequestDoctorAdapter(options,"doctor")
        recyclerView?.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

}