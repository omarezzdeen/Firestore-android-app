package com.example.firestoreandroidapp.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddBookActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    lateinit var binding: AddBookActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val addButton = findViewById<Button>(R.id.btn_add_book)

        addButton.setOnClickListener {
            val bookName = findViewById<EditText>(R.id.et_book_name).text.toString()
            val bookAuthor = findViewById<EditText>(R.id.et_book_author).text.toString()
            val bookLaunchYear = findViewById<EditText>(R.id.et_launch_year).text.toString()
            val bookPrice = findViewById<EditText>(R.id.et_price).text.toString()

            if (bookName.isNotEmpty() && bookAuthor.isNotEmpty() && bookLaunchYear.isNotEmpty() && bookPrice.isNotEmpty()) {
                val book = Books(bookName, bookAuthor, bookLaunchYear, bookPrice.toDouble())
                saveBooks(book)
            }else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun saveBooks(books: Books) {
        bookCollectionRef.add(books).addOnCompleteListener {task->
            if (task.isSuccessful){
                Toast.makeText(this,"successfully added user", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"error", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            println(it.message)
        }
    }

}
