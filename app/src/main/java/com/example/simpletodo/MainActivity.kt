// Cheuk Lam Cheung

package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            // Function to remove selected task
            override fun onItemRemoveClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)

                // 2. Notify the adapter of the change
                adapter.notifyItemRemoved(position)

                // 3. Save the change to the data file
                saveItems()
            }
            // Function to edit selected task
            override fun onItemEditClicked(position: Int) {
                val addButton = findViewById<Button>(R.id.button)
                val cancelButton = findViewById<Button>(R.id.cancelButton)
                val finishButton = findViewById<Button>(R.id.finishButton)
                addButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
                finishButton.visibility = View.VISIBLE

                // 1. Place selected task on input text field
                inputTextField.setText(listOfTasks[position])

                // 2. Replace task with the edited task on text field
                findViewById<Button>(R.id.finishButton).setOnClickListener {
                    listOfTasks[position] = inputTextField.text.toString()
                    adapter.notifyItemChanged(position)
                    saveItems()
                    // Reset the screen to its initial state
                    cancelButton.visibility = View.GONE
                    finishButton.visibility = View.GONE
                    inputTextField.setText("")
                    addButton.visibility = View.VISIBLE
                }

                // Reset the screen to its initial state
                findViewById<Button>(R.id.cancelButton).setOnClickListener {
                    cancelButton.visibility = View.GONE
                    finishButton.visibility = View.GONE
                    inputTextField.setText("")
                    addButton.visibility = View.VISIBLE
                }
            }
        }

        // Populate the list of task based on data file
        loadItems()


        // Look up the recyclerview in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list

        // Get a reference to the button and then set a onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {

            // 1. Grab the text the user has inputted into @+id/addTaskField
            val userInputtedText = inputTextField.text.toString()

            // 2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedText)
            // Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)

            // 3. Reset text field
            inputTextField.setText("")

            saveItems()

        }
    }

    // Save the data that the user has inputted by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File {
        // Every line represent a task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our files
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }
}