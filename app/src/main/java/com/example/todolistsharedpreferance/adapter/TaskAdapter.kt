package com.example.todolistsharedpreferance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistsharedpreferance.data.Task
import com.example.todolistsharedpreferance.databinding.ListItemBinding

class TaskAdapter(private val taskList: MutableList<Task>, private val clicklisten: TaskClickListen):
RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    interface TaskClickListen{
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)

    }

    class TaskViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        holder.binding.editBtn.setOnClickListener {
            clicklisten.onEditClick(position)
        }
        holder.binding.deletBtn.setOnClickListener {
            clicklisten.onDeleteClick(position)
        }
        holder.binding.checkbox.isChecked = task.isCompleted

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
        }
    }

}


