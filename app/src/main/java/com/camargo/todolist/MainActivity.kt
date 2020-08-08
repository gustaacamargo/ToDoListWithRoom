package com.camargo.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.camargo.todolist.adapters.ToDoAdapter
import com.camargo.todolist.adapters.ToDoListener
import com.camargo.todolist.model.ToDo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*

class MainActivity : AppCompatActivity(), ToDoListener {

    private lateinit var adapter: ToDoAdapter
    private var indexDaAtividadeSelecionada = 0;
    private var todoEdit: ToDo? = null
    private var statusBkp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            insertItem()
        }



        adapter = ToDoAdapter(this, applicationContext)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        reset()
    }

    fun insertItem() {
        val newItem = ToDo(
            "EDITANDO",
            "",
            ""
        )

        val position = adapter.add(newItem)
        (recycler_view.layoutManager as LinearLayoutManager).scrollToPosition(position)

        todoEdit = newItem
//        statusBkp = clickedItem.status
//        clickedItem.status = "EDITANDO"
        bt_save.setOnClickListener{
            saveToDo()
        }
        adapter.notifyItemChanged(position)

    }

    fun saveToDo() {
        val todoo = todoEdit as ToDo

        todoo.status = "[NÃO FEITO]"
        todoo.title = title_edit.text.toString()
        todoo.description = description_edit.text.toString()

        Toast.makeText(this, "clicado", Toast.LENGTH_SHORT).show()
        adapter.save(todoo)

        val p = adapter.returnPositionOfToDo(todoo)
        adapter.notifyItemChanged(p)

    }

    fun updateItem() {
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        clickedItem.title = title_edit.text.toString()
        clickedItem.description = description_edit.text.toString()

        adapter.update(clickedItem)
        adapter.notifyItemChanged(indexDaAtividadeSelecionada)

        reset()
    }

    fun removeItem() {
        adapter.removeToDoInPosition(indexDaAtividadeSelecionada)
        reset()
    }

    fun reset() {
//        title_text.text = Editable.Factory.getInstance().newEditable("")
//        description_text.text = Editable.Factory.getInstance().newEditable("")
//        add_bt.text = "Adicionar"
//        add_bt.setOnClickListener {
//            insertItem()
//        }
//        remove_bt.isClickable=false
//        remove_bt.visibility= View.GONE
    }

    override fun onItemEditClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        clickedItem.status = statusBkp
        adapter.notifyItemChanged(indexDaAtividadeSelecionada)
    }

    override fun onItemClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        statusBkp = clickedItem.status
        clickedItem.status = "EDITANDO"
        //showEditCard()
//        title_edit.text = Editable.Factory.getInstance().newEditable(clickedItem.title)
//        description_edit.text = Editable.Factory.getInstance().newEditable(clickedItem.description)

        bt_delete.setOnClickListener {
            removeItem()
        }

        bt_save.setOnClickListener {
            saveToDo()
        }

        adapter.notifyItemChanged(indexDaAtividadeSelecionada)
    }

    override fun onLongClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)

        if(clickedItem.status == "[NÃO FEITO]") {
            clickedItem.status = "[FEITO]"
        }
        else if(clickedItem.status == "[FEITO]") {
            clickedItem.status = "[NÃO FEITO]"
        }

        adapter.notifyItemChanged(indexDaAtividadeSelecionada)
    }

}