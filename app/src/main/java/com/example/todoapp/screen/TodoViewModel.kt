package com.example.todoapp.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Todo
import com.example.todoapp.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class TodoViewModel:ViewModel() {
    private val _todo= MutableStateFlow<List<Todo>>(emptyList())
    val todo=_todo.asStateFlow()
    val repo=TodoRepository()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getData().distinctUntilChanged().collect{
                    _todo.value=it
            }
        }
    }

    fun addTodo(todo: Todo)=viewModelScope.launch { repo.addtodo(todo) }
    fun removeTodo(todo: Todo)=viewModelScope.launch {
        repo.deleteNote(todo)
    }
}