package com.example.neokotlinui.appointments.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint // <<< ADDED IMPORT
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neokotlinui.BookingActivity // For FAB if you add it later
import com.example.neokotlinui.appointments.data.local.Appointment
import com.example.neokotlinui.appointments.viewmodel.AppointmentEvent
import com.example.neokotlinui.appointments.viewmodel.AppointmentViewModel
import com.example.neokotlinuiconverted.R
import com.example.neokotlinuiconverted.databinding.ActivityMyAppointmentsBinding // ViewBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MyAppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAppointmentsBinding
    private val appointmentViewModel: AppointmentViewModel by viewModel()
    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupViewModelObservers()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMyAppointments)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(
            onCancelClicked = { appointment ->
                showCancelConfirmationDialog(appointment)
            },
            onItemClicked = { appointment ->
                Toast.makeText(this, "View details for: ${appointment.doctorName}", Toast.LENGTH_SHORT).show()
                // Future: Navigate to a detailed view
            }
        )
        binding.rvAppointmentsList.apply {
            adapter = appointmentAdapter
            layoutManager = LinearLayoutManager(this@MyAppointmentsActivity)
        }

        // Setup ItemTouchHelper for swipe to delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // No drag & drop
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Swipe left or right
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Not used
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val appointment = appointmentAdapter.currentList[position]
                    showDeleteConfirmationDialog(appointment, position)
                }
            }

            // For drawing background and icon during swipe
            private val deleteIcon = ContextCompat.getDrawable(this@MyAppointmentsActivity, R.drawable.ic_delete_sweep)
            private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
            private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
            private val backgroundPaint = Paint() // Renamed from 'background' to avoid conflict

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    // Clear canvas if swipe is canceled - draw with RecyclerView's background color
                    val clearPaint = Paint().apply { color = ContextCompat.getColor(this@MyAppointmentsActivity, R.color.light_gray_background) } // Assuming light_gray_background is your RV bg
                    c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), clearPaint)
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                if (dX > 0) { // Swiping to the right
                    backgroundPaint.color = ContextCompat.getColor(this@MyAppointmentsActivity, R.color.icon_tint_danger)
                    c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), itemView.left.toFloat() + dX, itemView.bottom.toFloat(), backgroundPaint)

                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.left + deleteIconMargin
                    val deleteIconRight = itemView.left + deleteIconMargin + intrinsicWidth
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    deleteIcon?.draw(c)
                } else if (dX < 0) { // Swiping to the left
                    backgroundPaint.color = ContextCompat.getColor(this@MyAppointmentsActivity, R.color.icon_tint_danger)
                    c.drawRect(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), backgroundPaint)

                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    deleteIcon?.draw(c)
                } else { // view is unSwiped - this might not be strictly necessary if isCanceled is handled well
                    // super.onChildDraw might handle restoring the default item view
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvAppointmentsList)
    }

    private fun setupViewModelObservers() {
        appointmentViewModel.allAppointments.observe(this) { appointments ->
            appointmentAdapter.submitList(appointments)
            binding.layoutEmptyState.visibility = if (appointments.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.rvAppointmentsList.visibility = if (appointments.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        appointmentViewModel.event.observe(this) { event ->
            when (event) {
                is AppointmentEvent.ShowToast -> {
                    Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showCancelConfirmationDialog(appointment: Appointment) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.cancel_appointment_title))
            .setMessage(getString(R.string.cancel_appointment_message, appointment.doctorName, SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(appointment.appointmentDateTime)))
            .setPositiveButton(getString(R.string.action_cancel_appointment)) { dialog, _ ->
                appointmentViewModel.updateAppointmentStatus(appointment.id, "CANCELLED")
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.action_keep_appointment)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteConfirmationDialog(appointment: Appointment, adapterPosition: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_appointment_title))
            .setMessage(getString(R.string.delete_appointment_message, appointment.doctorName, SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(appointment.appointmentDateTime)))
            .setPositiveButton(getString(R.string.action_delete)) { dialog, _ ->
                appointmentViewModel.deleteAppointmentById(appointment.id)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.action_dont_delete)) { dialog, _ ->
                appointmentAdapter.notifyItemChanged(adapterPosition)
                dialog.dismiss()
            }
            .setOnCancelListener {
                appointmentAdapter.notifyItemChanged(adapterPosition)
            }
            .show()
    }
}
