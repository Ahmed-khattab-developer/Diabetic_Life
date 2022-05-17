package com.rx.diabeticlife.patient.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.SessionManagement.Constants.NAME
import com.rx.diabeticlife.doctor.pojo.Doctor
import com.rx.diabeticlife.patient.adapter.DoctorAdapter

class NewRequestFragment : Fragment(R.layout.fragment_new_request) {

    private var recyclerView: RecyclerView? = null
    var adapter: DoctorAdapter? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var iSessionManagement: SessionManagement

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iSessionManagement = SessionManagement(context)

        view.findViewById<TextView>(R.id.user).text = "Hello " + iSessionManagement.getUserDetails()[NAME]
        recyclerView = view.findViewById(R.id.recycler)

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("doctor")

        recyclerView?.layoutManager = LinearLayoutManager(context)

        val options: FirebaseRecyclerOptions<Doctor> = FirebaseRecyclerOptions.Builder<Doctor>()
            .setQuery(databaseReference, Doctor::class.java)
            .build()
        adapter = DoctorAdapter(options)
        recyclerView?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

}