// Cheuk Lam Cheung
// A bridge that tells the recyclerview how to display the data we give it

package com.example.simpletodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskItemAdapter(private val listOfItems: List<String>, val longClickListener: OnLongClickListener) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(){

    interface OnLongClickListener {
        fun onItemRemoveClicked(position: Int)
        fun onItemEditClicked(position: Int)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.list_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfItems.get(position)

        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Store references to elements in our layout view
        val textView: TextView = itemView.findViewById(android.R.id.text1)
        private val removeButton: Button = itemView.findViewById(R.id.button2)
        private val editButton: Button = itemView.findViewById(R.id.button3)

        init {
            // Remove edit and remove button on click
            itemView.setOnClickListener {
                removeButton.visibility = View.GONE
                editButton.visibility = View.GONE
            }
            // Show edit and remove button on long click
            itemView.setOnLongClickListener{
                removeButton.visibility = View.VISIBLE
                editButton.visibility = View.VISIBLE
                // Remove item when remove is clicked
                removeButton.setOnClickListener {
                    longClickListener.onItemRemoveClicked(adapterPosition)
                    removeButton.visibility = View.GONE
                    editButton.visibility = View.GONE
                }
                // Allow edit when edit is clicked
                editButton.setOnClickListener {
                    longClickListener.onItemEditClicked(adapterPosition)
                    removeButton.visibility = View.GONE
                    editButton.visibility = View.GONE
                }
                true
            }
        }
    }
}