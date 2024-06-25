package com.example.lens_assignment.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lens_assignment.R
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.LayoutTasksBinding
import com.example.lens_assignment.utils.Constants
import java.text.SimpleDateFormat
import java.util.PrimitiveIterator


class TaskListAdapter(val context:Context , private val onTaskCompleted:(Task)->Unit, private val onLongHold:(Task)->Unit) :
    ListAdapter<Task, TaskListAdapter.NoteViewHolder>(ComparatorDiffUtil()) {

    inner class NoteViewHolder(private val binding: LayoutTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(task: Task) {
            // notes for data
            binding.apply {

                taskTitle.text= task.title
                taskDescription.text=task.description
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                dueDate.text = formatter.format(task.dueDate)

                if (task.completed){
                    completedCheckbox.isChecked=true
                }else{
                    completedCheckbox.isChecked=false
                }

                completedCheckbox.setOnClickListener(){
                        onTaskCompleted(task)
                }

                priorityLevel.text= task.priorityLevel
                taskLocation.text=task.location


                when(task.priorityLevel){
                        Constants.LOW -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.priority_low))
                        Constants.MEDIUM -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.priority_medium))
                        Constants.HIGH -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.priority_high))
                        else -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.white))

                }

                root.setOnLongClickListener(){
                    onLongHold(task)
                    return@setOnLongClickListener false
                }



            }

        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val binding = LayoutTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        Log.d("checkingSize",position.toString())
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
        setAnimationNew(holder.itemView, position)


    }

    private fun setAnimationNew(viewToAnimate: View, position: Int) {
        val animation =   AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_right)

        viewToAnimate.startAnimation(animation)
    }


}