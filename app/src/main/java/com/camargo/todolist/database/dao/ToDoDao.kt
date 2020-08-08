package com.camargo.todolist.database.dao

import androidx.room.*
import com.camargo.todolist.model.ToDo

@Dao
interface ToDoDAO {
    @Query("SELECT * FROM todo")
    fun getAll(): List<ToDo>

    @Query("SELECT * FROM todo WHERE id IN (:ids)")
    fun getAllByIds(ids: IntArray): List<ToDo>

//    @Query("SELECT * FROM todo WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): ToDo

    @Insert
    fun insert(vararg todo: ToDo)

    @Update
    fun update(vararg todo: ToDo)

    @Delete
    fun delete(person: ToDo)
}