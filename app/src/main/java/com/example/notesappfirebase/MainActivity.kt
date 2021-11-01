package com.example.notesappfirebase

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var myRv: RecyclerView
    lateinit var lv: List<Note>
    private val myViewModel by lazy { ViewModelProvider(this).get(MyViewModel::class.java)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById<EditText>(R.id.editText)
        button = findViewById<Button>(R.id.button)
        myRv = findViewById<RecyclerView>(R.id.recyclerView)

        lv = arrayListOf()
        //retrieve data
        show()

        button.setOnClickListener {
            if (!editText.text.isEmpty()) {
                //add new note
                save()
            } else {
                Toast.makeText(this, "The text is empty", Toast.LENGTH_SHORT).show()
            }
            editText.text.clear()
        }
    }

    fun show() {
    myViewModel.getNotes().observe(this, {
        notes ->
        myRv.adapter = RecyclerViewAdapter(this,notes)
        myRv.layoutManager = LinearLayoutManager(this)
    })
    }

    fun save() {
        var noteText = editText.text.toString()
        myViewModel.addNote(Note("",noteText))
        Toast.makeText(applicationContext, "data saved successfully! ", Toast.LENGTH_SHORT).show()
    }

    fun update(ID: String) {
        //first we create a variable to hold an AlertDialog builder
        val dialogBuilder = AlertDialog.Builder(this)
        // then we set up the input
        val input = EditText(this)
        input.hint = "Enter new note"
        // positive button text and action
        dialogBuilder.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->
            val str = input.text.toString()
            if (str.isEmpty()) {
                Toast.makeText(applicationContext, "Fill all filed please!! ", Toast.LENGTH_SHORT).show()
            } else {
                //update note
                myViewModel.updateNote(Note(ID,str))
                Toast.makeText(applicationContext, "data updated successfully! ", Toast.LENGTH_SHORT).show()
            }
        })
                // negative button text and action
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, id ->
                })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Update Note")
        // add the Edit Text
        alert.setView(input)
        // show alert dialog
        alert.show()
    }

    fun delete(id: String) {
        //delete note
        myViewModel.deleteNote(id)
        Toast.makeText(applicationContext, "data deleted successfully! ", Toast.LENGTH_SHORT).show()
    }

}