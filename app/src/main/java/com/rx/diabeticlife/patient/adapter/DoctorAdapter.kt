package com.rx.diabeticlife.patient.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.rx.diabeticlife.R
import com.rx.diabeticlife.doctor.pojo.Doctor
import com.rx.diabeticlife.patient.activity.ChooseTrainerActivity
import com.squareup.picasso.Picasso

class DoctorAdapter(options: FirebaseRecyclerOptions<Doctor>) :
    FirebaseRecyclerAdapter<Doctor, DoctorAdapter.DoctorViewHolder>(options) {

    private var context: Context? = null

    override fun onBindViewHolder(
        @NonNull holder: DoctorViewHolder, position: Int, @NonNull model: Doctor
    ) {
        holder.firstname.text = "Dr : " + model.name
        holder.fees.text = "fees : " + model.fees + " EG"
        Picasso.with(context).load(model.image).into(holder.image)

        holder.itemView.setOnClickListener {
            val mainIntent = Intent(context, ChooseTrainerActivity::class.java)
            mainIntent.putExtra("doctor_id", model.uid)
            mainIntent.putExtra("doctor_name", model.name)
            mainIntent.putExtra("doctor_image", model.image)
            mainIntent.putExtra("doctor_fees", model.fees)
            context?.startActivity(mainIntent)
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): DoctorViewHolder {

        context = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_item, parent, false)
        return DoctorViewHolder(view)
    }

    class DoctorViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var firstname: TextView = itemView.findViewById(R.id.firstname)
        var fees: TextView = itemView.findViewById(R.id.fees)
    }

}