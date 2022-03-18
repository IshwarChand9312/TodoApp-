package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class TodoAdapter(val list : List<TodoModel>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int): Long {
        return list[position].id
    }


    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val txtShowCategory = itemView.findViewById<TextView>(R.id.txtShowCategory)
        val txtShowTask = itemView.findViewById<TextView>(R.id.txtShowTask)
        val txtShowTitle = itemView.findViewById<TextView>(R.id.txtShowTitle)
        val txtShowTime  = itemView.findViewById<TextView>(R.id.txtShowTime)
        val txtShowDate = itemView.findViewById<TextView>(R.id.txtShowDate)

        fun bind(todoModel: TodoModel) {
            with(itemView) {
                txtShowTitle.text = todoModel.title
                txtShowTask.text = todoModel.description
                txtShowCategory.text = todoModel.Category
                updateTime(todoModel.time)
                updateDate(todoModel.date)

            }
        }
        private fun updateTime(time: Long) {
            //Mon, 5 Jan 2020
            val myformat = "h:mm a"
            val sdf = SimpleDateFormat(myformat)
            txtShowTime.text = sdf.format(Date(time))

        }

        private fun updateDate(time: Long) {
            //Mon, 5 Jan 2020
            val myformat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myformat)
            txtShowDate.text = sdf.format(Date(time))

        }
    }


}