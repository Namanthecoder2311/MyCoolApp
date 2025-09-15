package com.example.neokotlinui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.model.Speciality
import com.example.neokotlinuiconverted.R // Ensure this R is correct

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
        private val iconPlaceholderImageView: ImageView = itemView.findViewById(R.id.iv_speciality_icon_placeholder) // We have this in the layout

        fun bind(speciality: Speciality) {
            nameTextView.text = speciality.name
            descriptionTextView.text = speciality.description

            // The icon placeholder is already set by its background in list_item_speciality.xml
            // If speciality.iconPlaceholder was intended to dynamically set an icon,
            // that logic would go here, similar to how images were handled in FacilitiesAdapter.
            // For now, it will just show the drawable_oval_placeholder_light_gray.
        }
    }
}