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
import com.rx.diabeticlife.patient.activity.RequestDetailsActivity
import com.rx.diabeticlife.patient.pojo.Request
import com.squareup.picasso.Picasso

class RequestAdapter(options: FirebaseRecyclerOptions<Request>) :
    FirebaseRecyclerAdapter<Request, RequestAdapter.RequestViewHolder>(options) {

    private var context: Context? = null

    override fun onBindViewHolder(
        @NonNull holder: RequestViewHolder, position: Int, @NonNull model: Request
    ) {
        holder.firstname.text = "Dr : "+model.doctorName
        holder.date.text ="date : "+ model.date
        Picasso.with(context).load(model.doctorImage).into(holder.image)

        holder.itemView.setOnClickListener {
            val mainIntent = Intent(context, RequestDetailsActivity::class.java)
            mainIntent.putExtra("doctorId", model.doctorId)
            mainIntent.putExtra("doctorName", model.doctorName)
            mainIntent.putExtra("doctorImage", model.doctorImage)
            mainIntent.putExtra("trainerId", model.trainerId)
            mainIntent.putExtra("trainerName", model.trainerName)
            mainIntent.putExtra("trainerAge", model.trainerAge)
            mainIntent.putExtra("trainerGender", model.trainerGender)
            mainIntent.putExtra("patientId", model.patientId)
            mainIntent.putExtra("patientName", model.patientName)
            mainIntent.putExtra("patientAge", model.patientAge)
            mainIntent.putExtra("patientGender", model.patientGender)
            mainIntent.putExtra("sugarLevel", model.sugarLevel)
            mainIntent.putExtra("targetLevel", model.targetLevel)
            mainIntent.putExtra("weight", model.weight)
            mainIntent.putExtra("height", model.height)
            mainIntent.putExtra("date", model.date)
            context?.startActivity(mainIntent)
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RequestViewHolder {

        context = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_item, parent, false)
        return RequestViewHolder(view)
    }

    class RequestViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var firstname: TextView = itemView.findViewById(R.id.firstname)
        var date: TextView = itemView.findViewById(R.id.date)
    }

}