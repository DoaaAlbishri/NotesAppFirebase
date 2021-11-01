package com.example.notesappfirebase

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RecyclerViewAdapter(private val mainActivity: MainActivity, private val words: List<Note>) : RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>(){
    class ItemViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    var ctx : Context? = null
   // private var words =  listOf<Note>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        ctx = parent.context
        return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_row
                        ,parent
                        ,false
                )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val word=words[position]

        holder.itemView.apply {
            tv.text = word.note
            if(position%2==0){llItem.setBackgroundColor(Color.GRAY)}
            editBtn.setOnClickListener {
                mainActivity.update(word.ID)
            }
            delBtn.setOnClickListener {
                mainActivity.delete(word.ID)
            }
        }
    }

    override fun getItemCount(): Int =words.size

    //fun update(notes: List<Note>){
    //    this.words = notes
    //    notifyDataSetChanged()
    //}

}