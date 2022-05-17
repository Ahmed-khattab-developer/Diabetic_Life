package com.rx.diabeticlife.patient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rx.diabeticlife.R
import com.rx.diabeticlife.SessionManagement
import com.rx.diabeticlife.patient.adapter.TrainerAdapter
import com.rx.diabeticlife.trainer.pojo.Trainer

class ChooseTrainerActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    var adapter: TrainerAdapter? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var iSessionManagement: SessionManagement

    var doctorId: String? = null
    var doctorName: String? = null
    var doctorImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_trainer)
        iSessionManagement = SessionManagement(this)

        doctorId = intent.getStringExtra("doctor_id")
        doctorName = intent.getStringExtra("doctor_name")
        doctorImage = intent.getStringExtra("doctor_image")

        recyclerView = findViewById(R.id.recycler)

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("trainer")

        recyclerView?.layoutManager = LinearLayoutManager(this)

        val options: FirebaseRecyclerOptions<Trainer> = FirebaseRecyclerOptions.Builder<Trainer>()
            .setQuery(databaseReference, Trainer::class.java)
            .build()
        adapter = TrainerAdapter(options, doctorId!!,doctorName!!,doctorImage!!)
        recyclerView?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

}