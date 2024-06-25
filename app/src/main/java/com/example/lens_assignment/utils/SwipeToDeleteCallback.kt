package com.example.lens_assignment.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lens_assignment.R
import com.example.lens_assignment.adapters.TaskListAdapter
import com.google.android.material.snackbar.Snackbar
class SwipeToDeleteCallback(
    private val adapter: TaskListAdapter,
    private val context: Context,
    private val taskActionListener: TaskActionListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val task = adapter.currentList[position]

        // Show a dialog to edit or delete
        AlertDialog.Builder(context)
            .setTitle("Edit or Delete Task")
            .setMessage("What would you like to do with this task?")
            .setPositiveButton("Edit") { dialog, which ->
                // Handle the edit action
                dialog.dismiss()
                adapter.notifyItemChanged(position) // Reset swipe
                taskActionListener.onEditTask(task)
            }
            .setNegativeButton("Delete") { dialog, which ->
                // Handle the delete action
                taskActionListener.onDeleteTask(task)
                dialog.dismiss()
            }
            .setOnCancelListener {
                adapter.notifyItemChanged(position) // Reset swipe if dialog is cancelled
            }
            .show()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val background = ColorDrawable()
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_red) // Use your own icon

        background.color = Color.RED
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        icon.setBounds(
            itemView.right - iconMargin - icon.intrinsicWidth,
            itemView.top + iconMargin,
            itemView.right - iconMargin,
            itemView.bottom - iconMargin
        )
        icon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
