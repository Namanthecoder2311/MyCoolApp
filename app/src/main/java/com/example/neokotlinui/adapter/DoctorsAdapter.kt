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
        private val imageView: ImageView = itemView.findViewById(R.id.iv_doctor_image) // Assuming you'll load images

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
                // Optionally change text color for available status if needed
                 statusTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.dark_text_welcome)) // Or a specific green
            } else {
                statusTextView.text = "Busy"
                statusTextView.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.drawable_status_busy_bg
                )
                // Optionally change text color for busy status if needed
                 statusTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.dark_text_welcome)) // Or a specific red/darker gray
            }

            // Image loading would go here. For now, it uses the placeholder from the XML.
            // Example:
            // if (doctor.imageName != null) {
            //     val imageResId = itemView.context.resources.getIdentifier(
            //         doctor.imageName, "drawable", itemView.context.packageName
            //     )
            //     if (imageResId != 0) {
            //         imageView.setImageResource(imageResId)
            //     } else {
            //          imageView.setImageResource(R.drawable.drawable_oval_placeholder_light_gray) // Fallback
            //     }
            // } else {
            //     imageView.setImageResource(R.drawable.drawable_oval_placeholder_light_gray) // Default placeholder
            // }
        }
    }
}