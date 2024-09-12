package com.example.todolistsharedpreferance

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistsharedpreferance.adapter.TaskAdapter
import com.example.todolistsharedpreferance.data.Task

class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTaskEditTaxts: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("Tasks", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.recyclerView)
        editTaskEditTaxts = findViewById(R.id.editTaskEt)
        taskList = retrieveTasks()



        val saveButton: Button = findViewById(R.id.saveEt)

        saveButton.setOnClickListener {
            val taskTitle = editTaskEditTaxts.text.toString()
            if (taskTitle.isNotEmpty()) {
                val task = Task(taskTitle, false)
                saveTask(taskList)
                taskList.add(task)
                taskAdapter.notifyItemInserted(taskList.size - 1)
                editTaskEditTaxts.text.clear()

            }
            else{
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()

            }
        }
        taskAdapter = TaskAdapter(taskList, object : TaskAdapter.TaskClickListen {
            override fun onEditClick(position: Int) {
                editTaskEditTaxts.setText(taskList[position].title)
                taskList.removeAt(position)
                taskAdapter.notifyItemRemoved(position)
            }

            override fun onDeleteClick(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure you want to delete this task?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    deleteTask(position)
                }
                alertDialog.setNegativeButton("No") { dialog, _ ->}
                alertDialog.show()

            }



        })
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun saveTask(taskList: MutableList<Task>) {
        val editor = sharedPreferences.edit()
        val taskset = HashSet<String>()

        taskList.forEach {taskset.add(it.title)}
        editor.putStringSet("tasks", taskset)
        editor.apply()

    }

    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTask(taskList)

    }


    private fun retrieveTasks(): MutableList<Task> {
        val tasks = sharedPreferences.getStringSet("tasks", HashSet()) ?: HashSet()
        return tasks.map { Task(it, false) }.toMutableList()


    }

}