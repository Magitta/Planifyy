package com.example.planifyy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: List<Array<Any>>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTaskTitle)
        val time: TextView = view.findViewById(R.id.tvTaskTime)
        val deleteBtn: ImageButton = view.findViewById(R.id.btnDelete)
        val checkBox: CheckBox = view.findViewById(R.id.cbCompleted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val title = task[0].toString()
        val isDone = task[3].toString().toInt()

        holder.title.text = title
        holder.time.text = task[1].toString()

        // Настройвам чекбокса
        holder.checkBox.setOnCheckedChangeListener(null) // Важно: нулираме слушателя
        holder.checkBox.isChecked = (isDone == 1)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val dbHelper = DatabaseHelper(holder.itemView.context)
            dbHelper.updateTaskStatus(title, if (isChecked) 1 else 0)
        }

        holder.deleteBtn.setOnClickListener { onDelete(position) }
    }

    override fun getItemCount() = tasks.size
}