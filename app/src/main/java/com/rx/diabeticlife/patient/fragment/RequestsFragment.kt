package com.rx.diabeticlife.patient.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.SessionManagement.Constants.NAME
import com.rx.diabeticlife.patient.adapter.RequestAdapter
import com.rx.diabeticlife.patient.pojo.Request

class RequestsFragment : Fragment(R.layout.fragment_requests) {

    private var recyclerView: RecyclerView? = null
    var adapter: RequestAdapter? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var iSessionManagement: SessionManagement

    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iSessionManagement = SessionManagement(context)

        view.findViewById<TextView>(R.id.user).text =
            "Hello " + iSessionManagement.getUserDetails()[NAME]
        recyclerView = view.findViewById(R.id.recycler)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("request")

        recyclerView?.layoutManager = LinearLayoutManager(context)

        val options: FirebaseRecyclerOptions<Request> = FirebaseRecyclerOptions.Builder<Request>()
            .setQuery(
                databaseReference.orderByChild("patientId").equalTo(auth.uid.toString()),
                Request::class.java
            )
            .build()
        adapter = RequestAdapter(options)
        recyclerView?.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

}