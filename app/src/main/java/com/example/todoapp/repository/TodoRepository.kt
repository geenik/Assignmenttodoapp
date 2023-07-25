package com.example.todoapp.repository

import com.example.todoapp.Todo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TodoRepository {
    val db=Firebase.firestore
    val auth=Firebase.auth
    fun getData(): Flow<List<Todo>> = callbackFlow {
        val subscription = db.collection("tasks").document(auth.uid!!).collection("usertasks")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val dataList = it.documents.map { doc ->
                        doc.toObject(Todo::class.java)
                    }
                    trySend(dataList as List<Todo>)
                }
            }

        awaitClose {
            subscription.remove()
        }
    }
    fun addtodo(todo: Todo) {
        db.collection("tasks").document(auth.uid!!).collection("usertasks").add(todo)
    }
    suspend fun deleteNote(todo: Todo) {
        try {
            val querySnapshot = db.collection("tasks")
                .document(auth.uid!!).collection("usertasks")
                .whereEqualTo("task",todo.task)
                .get()
                .await()
            for (document in querySnapshot.documents) {
                document.reference.delete().await()
            }
        } catch (e: Exception) {
            // Handle any potential errors here (e.g., logging or notifying the user)
            throw e
        }
    }

}