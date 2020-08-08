package com.camargo.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class ToDo(
    @ColumnInfo(name = "status") var status: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}