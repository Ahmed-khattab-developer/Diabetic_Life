package com.rx.diabeticlife.patient.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.rx.diabeticlife.R
import com.rx.diabeticlife.patient.activity.SendRequestActivity
import com.rx.diabeticlife.trainer.pojo.Trainer

class TrainerAdapter(
    options: FirebaseRecyclerOptions<Trainer>, val doctorId: String,
    val doctorName: String, val doctorImage: String,
) :
    FirebaseRecyclerAdapter<Trainer, TrainerAdapter.TrainerViewHolder>(options) {

    private var context: Context? = null

    override fun onBindViewHolder(
        @NonNull holder: TrainerViewHolder, position: Int, @NonNull model: Trainer
    ) {
        holder.firstname.text = model.name

        holder.itemView.setOnClickListener {
            val mainIntent = Intent(context, SendRequestActivity::class.java)
            mainIntent.putExtra("doctor_id", doctorId)
            mainIntent.putExtra("doctor_name", doctorName)
            mainIntent.putExtra("doctor_image", doctorImage)
            mainIntent.putExtra("trainer_id", model.uid)
            mainIntent.putExtra("trainer_name", model.name)
            mainIntent.putExtra("trainer_age", model.age)
            mainIntent.putExtra("trainer_gender", model.gender)
            context?.startActivity(mainIntent)
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): TrainerViewHolder {

        context = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.trainer_item, parent, false)
        return TrainerViewHolder(view)
    }

    class TrainerViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstname: TextView = itemView.findViewById(R.id.firstname)
    }

}