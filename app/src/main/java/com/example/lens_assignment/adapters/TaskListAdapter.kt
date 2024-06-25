package com.example.lens_assignment.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lens_assignment.R
import com.example.lens_assignment.data.local.entity.Task
import com.example.lens_assignment.databinding.LayoutTasksBinding
import java.text.SimpleDateFormat


class TaskListAdapter() :
    ListAdapter<Task, TaskListAdapter.NoteViewHolder>(ComparatorDiffUtil()) {

    inner class NoteViewHolder(private val binding: LayoutTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(task: Task) {
            // notes for data
            binding.apply {

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
        val animation = if (position % 2 == 0) {
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_left)
        } else {
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_right)
        }
        viewToAnimate.startAnimation(animation)
    }


}