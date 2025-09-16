package com.example.neokotlinui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.model.Doctor
import com.example.neokotlinuiconverted.R // Make sure this R is correct

class DoctorsAdapter(private val doctors: List<Doctor>) :
    RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.bind(doctor)
    }

    override fun getItemCount(): Int = doctors.size

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_doctor_name)
        private val specialityTextView: TextView = itemView.findViewById(R.id.tv_doctor_speciality)
        private val experienceTextView: TextView = itemView.findViewById(R.id.tv_doctor_experience)
        private val statusTextView: TextView = itemView.findViewById(R.id.tv_doctor_status)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_doctor_image)

        fun bind(doctor: Doctor) {
            nameTextView.text = doctor.name
            specialityTextView.text = doctor.speciality
            experienceTextView.text = doctor.experience

            if (doctor.isAvailable) {
                statusTextView.text = "Available"
                statusTextView.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.drawable_status_available_bg
                )
                statusTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.dark_text_welcome))
            } else {
                statusTextView.text = "Busy"
                statusTextView.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.drawable_status_busy_bg
                )
                statusTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.dark_text_welcome))
            }

            // Set doctor image
            if (doctor.name == "Dr. Sarah Johnson") {
                imageView.setImageResource(R.drawable.doctor1) 
                imageView.background = null 
            } else if (doctor.name == "Dr. Michael Chen") {
                imageView.setImageResource(R.drawable.doctor2) 
                imageView.background = null
            } else if (doctor.name == "Dr. James Wilson") {
                imageView.setImageResource(R.drawable.doctor4)
                imageView.background = null
            } else if (doctor.name == "Dr. Emily Rodriguez") {
                imageView.setImageResource(R.drawable.doctor3)
                imageView.background = null
            } else if (doctor.name == "Dr. Linda Karen") {
                imageView.setImageResource(R.drawable.doctor5)
                imageView.background = null
            } else {
                imageView.setImageDrawable(null)
                imageView.background = ContextCompat.getDrawable(itemView.context, R.drawable.drawable_oval_placeholder_light_gray)
            }
        }
    }
}