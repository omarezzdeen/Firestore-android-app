package com.example.firestoreandroidapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.example.firestoreandroidapp.screens.EditBookActivity
import com.example.firestoreandroidapp.ui.viewHolders.BookViewHolder
import com.google.android.gms.common.internal.Constants
import java.util.*

class BookAdapter(
    val list: ArrayList<Books>
) : RecyclerView.Adapter<BookViewHolder>() {

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = list[position]
        holder.apply {
            textNameBook.text = currentBook.bookName
            textNameAuthor.text = currentBook.bookAuthor
            textLaunchYear.text = currentBook.launchYear
            textPriceBook.text = currentBook.price.toString()
        }

//        holder.buttonPushEdit.setOnClickListener {
//            // Launch Product details screen.
//            val intent = Intent(this, EditBookActivity::class.java)
//            this.startActivity(intent)
//        }


    }

    override fun getItemCount() = list.size

}