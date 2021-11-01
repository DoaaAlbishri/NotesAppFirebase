package com.example.notesappfirebase

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel (applicationContext : Application): AndroidViewModel(applicationContext)  {
    val applicationContext = applicationContext
    private var notes : MutableLiveData<List<Note>> = MutableLiveData()
    val db = Firebase.firestore

    fun getNotes(): LiveData<List<Note>> {
        db.collection("notes")
            .get()
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
            .addOnSuccessListener { result ->
                var list : MutableList<Note> = mutableListOf()
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    document.data.map { (key, value)
                        // add Note
                        // id is document id
                        //value is the value of key note
                        -> list.add(Note(document.id, value.toString()))
                    }
                }
                //post value to notes list
                notes.postValue(list)
                println(notes.value)
            }
        return notes
    }

    fun addNote(note :Note){
        CoroutineScope(Dispatchers.IO).launch {
            val note = hashMapOf(
                "note" to note.note
            )
            db.collection("notes")
                    // add note
                .add(note)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(
                        applicationContext,
                        "Save success with id:" + "${documentReference.id}", Toast.LENGTH_SHORT
                    ).show();
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
        getNotes()
    }

    fun updateNote(note:Note){
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("notes")
                .get()
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        // update for note by id
                        if(document.id == note.ID){
                            db.collection("notes").document(note.ID).update("note", note.note)
                        }
                    }
                    getNotes()
                }
        }
        }

    fun deleteNote(ID : String){
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("notes")
                .get()
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        //delete note by id
                        if(document.id == ID){
                            db.collection("notes").document(ID).delete()
                        }
                    }
                    getNotes()
                }
        }
    }

}