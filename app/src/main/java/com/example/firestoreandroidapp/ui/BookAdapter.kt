package com.example.firestoreandroidapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.example.firestoreandroidapp.ui.viewHolders.BookViewHolder
import com.google.firebase.firestore.CollectionReference
import java.util.*
import kotlin.collections.ArrayList

class BookAdapter(val list: ArrayList<Books>) : RecyclerView.Adapter<BookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book,parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = list[position]
        holder.apply {
            holder.textNameBook.text = currentBook.bookName
            holder.textNameAuthor.text = currentBook.bookAuthor
            holder.textLaunchYear.text = currentBook.launchYear
            holder.textPriceBook.text = currentBook.price.toString()
        }


    }

    override fun getItemCount() = list.size

}