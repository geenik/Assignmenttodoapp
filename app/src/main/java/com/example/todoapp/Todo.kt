package com.example.todoapp

import java.util.Date

data class Todo(val task:String,val date:Date){
    constructor():this("",Date())
}
