package com.camargo.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.camargo.todolist.database.dao.ToDoDAO
import com.camargo.todolist.model.ToDo

@Database(entities = [ToDo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDAO
}