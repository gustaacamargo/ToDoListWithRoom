package com.camargo.todolist.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.camargo.todolist.R
import com.camargo.todolist.database.AppDatabase
import com.camargo.todolist.database.dao.ToDoDAO
import com.camargo.todolist.model.ToDo
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_edit.view.bt_save
import kotlinx.android.synthetic.main.item_edit.view.description_edit
import kotlinx.android.synthetic.main.item_edit.view.title_edit
import kotlinx.android.synthetic.main.item_edit.view.bt_delete

class ToDoAdapter(
    private val listener: ToDoListener, context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dao: ToDoDAO
    private var toDoList: MutableList<ToDo> = mutableListOf<ToDo>()
    private val EDIT_ITEM = 0
    private val NORMAL_ITEM = 1

    init {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo-db"
        )
            .allowMainThreadQueries()
            .build()

        dao = db.todoDao()
        toDoList = dao.getAll().toMutableList()
    }

    fun add(todo: ToDo): Int {
        val position = 0
        toDoList.add(position, todo)
        notifyItemInserted(position)
        return position
    }

    fun save(todo: ToDo) {
        dao.insert(todo)
    }

    fun update(todo: ToDo) {
        dao.update(todo)

    }

    fun getToDoInPosition(position: Int): ToDo {
        return toDoList[position]
    }

    fun removeToDoInPosition(position: Int) {
        val todo = getToDoInPosition(position)
        dao.delete(todo)
        toDoList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun returnPositionOfToDo(todo: ToDo): Int {
        return toDoList.indexOf(todo)
    }

    override fun getItemViewType(position: Int): Int {
        return if(toDoList[position].status == "EDITANDO") {
            EDIT_ITEM
        }
        else {
            NORMAL_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NORMAL_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item,
                parent, false)
            return ViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_edit,
                parent, false)
            return EditViewHolder(v)
        }

//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item,
//            parent, false)
//        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == EDIT_ITEM) {
            (holder as EditViewHolder).fillView(toDoList[position])
        }
        else {
            (holder as ViewHolder).fillView(toDoList[position])
        }
//        val todo = toDoList[position]
//        holder.fillView(todo)
    }
    override fun getItemCount() = toDoList.size

    inner class EditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun fillView(todo: ToDo) {
            itemView.title_edit.setText(todo.title)
            itemView.description_edit.setText(todo.description)

            itemView.bt_save.setOnClickListener{
                listener.onBtSaveClick()
            }

            itemView.bt_delete.setOnClickListener{
                listener.onBtDeleteClick(todo)
            }

            itemView.setOnClickListener {
                listener.onItemEditClick(todo)
            }

//            itemView.setOnLongClickListener {
//                listener.onLongClick(todo)
//                true
//            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun fillView(todo: ToDo) {
            itemView.textView1.text = Editable.Factory.getInstance().newEditable(todo.status + " " + todo.title)
            itemView.text_view_2.text = Editable.Factory.getInstance().newEditable(todo.description)
            if(todo.status == "[FEITO]") {
                itemView.textView1.paintFlags = itemView.textView1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemView.bt_share.visibility = View.VISIBLE
            }
            else {
                itemView.bt_share.visibility = View.GONE
            }

            itemView.setOnClickListener {
                listener.onItemClick(todo)
            }

            itemView.bt_share.setOnClickListener {
                listener.onBtShareClick(todo)
            }

            itemView.setOnLongClickListener {
                listener.onLongClick(todo)
                true
            }
        }
    }
}

