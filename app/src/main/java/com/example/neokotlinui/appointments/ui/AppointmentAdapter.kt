package com.example.neokotlinui.appointments.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.appointments.data.local.Appointment
import com.example.neokotlinuiconverted.R // Corrected R class import
import com.example.neokotlinuiconverted.databinding.ItemAppointmentBinding // Correct ViewBinding import
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentAdapter(
    private val onCancelClicked: (Appointment) -> Unit,
    private val onItemClicked: (Appointment) -> Unit // For future use (e.g., view details)
) : ListAdapter<Appointment, AppointmentAdapter.AppointmentViewHolder>(AppointmentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding, onCancelClicked, onItemClicked)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppointmentViewHolder(
        private val binding: ItemAppointmentBinding,
        private val onCancelClicked: (Appointment) -> Unit,
        private val onItemClicked: (Appointment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateTimeFormatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        fun bind(appointment: Appointment) {
            binding.tvAppointmentDoctorName.text = appointment.doctorName
            // Assuming 'speciality' and 'appointmentDateTime' are valid fields in your Appointment data class
            // If not, you'll need to adjust these lines or your Appointment data class
            binding.tvAppointmentSpeciality.text = appointment.speciality 
            binding.tvAppointmentDateTime.text = dateTimeFormatter.format(Date(appointment.appointmentDateTime))

            val context = itemView.context
            when (appointment.status.uppercase(Locale.ROOT)) {
                "BOOKED" -> {
                    binding.chipAppointmentStatus.text = context.getString(R.string.status_booked)
                    binding.chipAppointmentStatus.setChipBackgroundColorResource(R.color.chip_bg_booked)
                    binding.chipAppointmentStatus.setTextColor(ContextCompat.getColor(context, R.color.chip_text_booked))
                    binding.ibCancelAppointment.visibility = View.VISIBLE
                }
                "CANCELLED" -> {
                    binding.chipAppointmentStatus.text = context.getString(R.string.status_cancelled)
                    binding.chipAppointmentStatus.setChipBackgroundColorResource(R.color.chip_bg_cancelled)
                    binding.chipAppointmentStatus.setTextColor(ContextCompat.getColor(context, R.color.chip_text_cancelled))
                    binding.ibCancelAppointment.visibility = View.GONE
                }
                "COMPLETED" -> {
                    binding.chipAppointmentStatus.text = context.getString(R.string.status_completed)
                    binding.chipAppointmentStatus.setChipBackgroundColorResource(R.color.chip_bg_completed)
                    binding.chipAppointmentStatus.setTextColor(ContextCompat.getColor(context, R.color.chip_text_completed))
                    binding.ibCancelAppointment.visibility = View.GONE
                }
                else -> { // Default or unknown status
                    binding.chipAppointmentStatus.text = appointment.status.uppercase(Locale.ROOT)
                    binding.chipAppointmentStatus.setChipBackgroundColorResource(R.color.light_gray_background) // A neutral default
                    binding.chipAppointmentStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_text_welcome))
                    binding.ibCancelAppointment.visibility = View.GONE
                }
            }

            // Placeholder for icon, can be improved based on speciality later
            binding.ivAppointmentIcon.setImageResource(R.drawable.ic_event_note_placeholder)

            binding.ibCancelAppointment.setOnClickListener {
                onCancelClicked(appointment)
            }
            binding.root.setOnClickListener {
                onItemClicked(appointment) // Hook for future item click handling
            }
        }
    }

    class AppointmentDiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem == newItem
        }
    }
}
