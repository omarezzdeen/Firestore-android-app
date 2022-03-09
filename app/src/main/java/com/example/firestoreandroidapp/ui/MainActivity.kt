package com.example.firestoreandroidapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.example.firestoreandroidapp.screens.AddBookActivity
import com.example.firestoreandroidapp.screens.EditBookActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.grpc.InternalChannelz.id

class MainActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookArrayList: ArrayList<Books>
    private lateinit var myAdapter: BookAdapter
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonPushEditBook = findViewById<Button>(R.id.btn_push_page_edit)

        val buttonPushAddBook = findViewById<FloatingActionButton>(R.id.btn_push_page_add_book)
        buttonPushAddBook.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        bookArrayList = arrayListOf()
        myAdapter = BookAdapter(bookArrayList)
        recyclerView.adapter = myAdapter
        retrieveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveData() {
        bookCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result?.documents!!) {
                    val book = document.toObject<Books>()
                    book?.let {
                        bookArrayList.add(it)
                    }
                    Log.d(TAG, "This is a id: ${document.id} => This is a data: ${document.data}")

                }
                myAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()


            } else {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            println(it.message)
        }
    }
}

