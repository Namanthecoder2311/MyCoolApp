package com.example.neokotlinui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.model.Facility
import com.example.neokotlinuiconverted.R // Ensure this R is correct

class FacilitiesAdapter(private val facilities: List<Facility>) :
    RecyclerView.Adapter<FacilitiesAdapter.FacilityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_facility, parent, false)
        return FacilityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val facility = facilities[position]
        holder.bind(facility)
    }

    override fun getItemCount(): Int = facilities.size

    class FacilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_facility_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_facility_description)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_facility_image)

        fun bind(facility: Facility) {
            nameTextView.text = facility.name
            descriptionTextView.text = facility.description

            // For actual image loading, you'd use a library like Glide or Picasso here
            // For now, we'll try to load it as a local drawable if imageName is set.
            // If imageName corresponds to a drawable resource you've added (e.g., "img_examination_room.png" -> R.drawable.img_examination_room)
            // This basic loading will work. Otherwise, it will keep the placeholder background.
            val imageResId = itemView.context.resources.getIdentifier(
                facility.imageName, "drawable", itemView.context.packageName
            )
            if (imageResId != 0) {
                imageView.setImageResource(imageResId)
            } else {
                // You could set a default placeholder image again here if needed,
                // but list_item_facility.xml already has a background color set.
                // imageView.setImageResource(R.drawable.default_facility_placeholder)
            }
        }
    }
}