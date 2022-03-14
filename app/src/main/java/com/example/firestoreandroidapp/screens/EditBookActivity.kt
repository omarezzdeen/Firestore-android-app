package com.example.firestoreandroidapp.screens

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditBookActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        val buttonEdit = findViewById<Button>(R.id.btn_edit_book)
        val buttonDelete = findViewById<Button>(R.id.btn_delete_book)



        buttonEdit.setOnClickListener {
            val book = getOldBookData()
                val newBookMap = getNewBookData()
                updateBookData(book,newBookMap)

        }

        buttonDelete.setOnClickListener {
            val book = getOldBookData()
            deleteBook(book)
        }
        setupBookData()


    }

    private fun setupBookData(){
        val ed_BookName = findViewById<EditText>(R.id.et_book_name_edit)
        val ed_BookAuthor = findViewById<EditText>(R.id.et_book_author_edit)
        val ed_BookLaunchYear = findViewById<EditText>(R.id.et_launch_year_edit)
        val ed_BookPrice = findViewById<EditText>(R.id.et_price_edit)

        val book = intent.getParcelableExtra<Books>("Book")
        ed_BookName.setText(book?.bookName)
        ed_BookAuthor.setText(book?.bookAuthor)
        ed_BookLaunchYear.setText(book?.launchYear)
        ed_BookPrice.setText(book?.price.toString())
        Log.d("id", "setupBookData: $book")
    }

    private fun getNewBookData(): Map<String, Any> {
        val bookName = findViewById<EditText>(R.id.et_book_name_edit).text.toString()
        val bookAuthor = findViewById<EditText>(R.id.et_book_author_edit).text.toString()
        val bookLaunchYear = findViewById<EditText>(R.id.et_launch_year_edit).text.toString()
        val bookPrice = findViewById<EditText>(R.id.et_price_edit).text.toString()
        val map = mutableMapOf<String, Any>()
        if (bookName.isNotEmpty()) {
            map["bookName"] = bookName
        }
        if (bookAuthor.isNotEmpty()) {
            map["bookAuthor"] = bookAuthor
        }
        if (bookLaunchYear.isNotEmpty()) {
            map["bookLaunchYear"] = bookLaunchYear
        }
        if (bookPrice.isNotEmpty()) {
            map["bookPrice"] = bookPrice
        }
        return map
    }

    private fun updateBookData(book: Books, newBookMap: Map<String, Any>) {
        bookCollectionRef.whereEqualTo("bookName", book.bookName)
            .whereEqualTo("bookAuthor", book.bookAuthor).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.documents.isNotEmpty()) {
                        for (document in task.result!!.documents) {
                            bookCollectionRef.document(document.id)
                                .set(newBookMap, SetOptions.merge())
                        }
                    }else{
                        Toast.makeText(this,"No matching documents",Toast.LENGTH_LONG).show()
                        Log.d(ContentValues.TAG, "No matching documents")
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                Log.d(ContentValues.TAG, "This is a message Error${it.message}")
            }
    }

    private fun deleteBook(book: Books) {
        bookCollectionRef.whereEqualTo("bookName", book.bookName)
            .whereEqualTo("bookAuthor", book.bookAuthor).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.documents.isNotEmpty()) {
                        for (document in task.result!!.documents) {
                            bookCollectionRef.document(document.id)
                        }
                    }else{
                        Toast.makeText(this,"No matching documents",Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
    }

    private fun getOldBookData(): Books {
        val bookID = bookCollectionRef.document().id
        val bookName = findViewById<EditText>(R.id.et_book_name).text.toString()
        val bookAuthor = findViewById<EditText>(R.id.et_book_author).text.toString()
        val bookLaunchYear = findViewById<EditText>(R.id.et_launch_year).text.toString()
        val bookPrice = findViewById<EditText>(R.id.et_price).text.toString()

        return Books(bookID, bookName, bookAuthor, bookLaunchYear, bookPrice.toDouble())
    }
}