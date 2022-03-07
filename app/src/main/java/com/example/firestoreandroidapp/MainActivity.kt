package com.example.firestoreandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firestoreandroidapp.screens.AddBookActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAdd = findViewById<FloatingActionButton>(R.id.but_push_page_add_book)

        buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }


    }
}