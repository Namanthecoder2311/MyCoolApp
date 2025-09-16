package com.example.neokotlinui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat // For potential placeholder
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.model.Speciality
import com.example.neokotlinuiconverted.R

class SpecialitiesAdapter(private val specialities: List<Speciality>) :
    RecyclerView.Adapter<SpecialitiesAdapter.SpecialityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_speciality, parent, false)
        return SpecialityViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecialityViewHolder, position: Int) {
        val speciality = specialities[position]
        holder.bind(speciality)
    }

    override fun getItemCount(): Int = specialities.size

    class SpecialityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_speciality_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_speciality_description)
        private val specialityImageView: ImageView = itemView.findViewById(R.id.iv_speciality_image) // New ImageView

        fun bind(speciality: Speciality) {
            nameTextView.text = speciality.name
            descriptionTextView.text = speciality.description

            if (speciality.imageName != null) {
                val imageResId = itemView.context.resources.getIdentifier(
                    speciality.imageName, "drawable", itemView.context.packageName
                )
                if (imageResId != 0) {
                    specialityImageView.setImageResource(imageResId)
                    specialityImageView.visibility = View.VISIBLE
                } else {
                    // Image name provided but resource not found, hide image view
                    specialityImageView.visibility = View.GONE
                    // Optionally, log an error or set a default error placeholder
                }
            } else {
                // No image name provided, hide image view
                specialityImageView.visibility = View.GONE
            }
        }
    }
}