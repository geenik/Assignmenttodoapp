package com.example.todoapp.screen

import android.os.Build
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.todoapp.R
import com.example.todoapp.Todo
import com.example.todoapp.components.NoteInputText
import com.example.todoapp.components.TodoButton
import com.example.todoapp.components.TodoRow
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun TodoScreen(
    navController: NavController,
) {
    val todowork= remember {
        mutableStateOf("")
    }
    val viewModel:TodoViewModel= viewModel()
    val list= viewModel.todo.collectAsState()
    val context= LocalContext.current
    Column(modifier = Modifier.padding(6.dp),) {
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {},
            colors = TopAppBarDefaults.smallTopAppBarColors(
                Color(0xFFDADFE3)
            )
        )
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            NoteInputText(modifier = Modifier.padding(vertical = 9.dp),text = todowork.value,
                label = "Task",
                onTextChange = {
                    if( it.all { c: Char -> c.isLetter()||c.isWhitespace() })
                        todowork.value=it
                }
            )
            TodoButton(text = "Save", onClick = {
                if(todowork.value.isNotEmpty()){
                    val task=Todo(todowork.value,Date())
                    viewModel.addTodo(task)
                    todowork.value=""
                    Toast.makeText(context,"todo Added", Toast.LENGTH_SHORT).show()
                }
            })
        }
        Divider(modifier= Modifier.padding(10.dp))
        LazyColumn{

            items(list.value){
                    todo->
                TodoRow(todo=todo){
                    viewModel.removeTodo(todo)
                }
            }
        }

    }
}