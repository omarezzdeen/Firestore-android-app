package com.example.firestoreandroidapp.screens

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.example.firestoreandroidapp.ui.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class AddBookActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    private val bookImageRef = Firebase.storage.reference
    var curFile: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val REQUST_CODE = 0


        val addButton = findViewById<Button>(R.id.btn_add_book)
        val uploadImageButton = findViewById<Button>(R.id.btn_upload_image)

        uploadImageButton.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUST_CODE)
            }
        }

        addButton.setOnClickListener {
            val bookID = bookCollectionRef.document().id
            val bookName = findViewById<EditText>(R.id.et_book_name).text.toString()
            val bookAuthor = findViewById<EditText>(R.id.et_book_author).text.toString()
            val bookLaunchYear = findViewById<EditText>(R.id.et_launch_year).text.toString()
            val bookPrice = findViewById<EditText>(R.id.et_price).text.toString()

            if (bookName.isNotEmpty() && bookAuthor.isNotEmpty() && bookLaunchYear.isNotEmpty() && bookPrice.isNotEmpty()) {
                val book = Books(bookID, bookName, bookAuthor, bookLaunchYear, bookPrice.toDouble())
                saveBooks(book)
            }else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun saveBooks(books: Books) {
        bookCollectionRef.add(books).addOnCompleteListener {task->
            if (task.isSuccessful){
                Toast.makeText(this,"successfully added Book", Toast.LENGTH_LONG).show()
                uploadToFirebase("myImage")
                downloadImage("myImage")
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                Toast.makeText(this,"error", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            println(it.message)
        }
    }
    private fun uploadToFirebase(fileName: String){
        curFile?.let {
            bookImageRef.child("images/$fileName").putFile(it)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"uploaded", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun downloadImage(fileName: String) {
        val maxDownloadSize = 5L * 1024 * 1024
        val bytes = bookImageRef.child("images/$fileName").getBytes(maxDownloadSize)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val bmp = BitmapFactory.decodeByteArray(task.result, 0, task.result!!.size)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bmp)
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Activity.RESULT_OK && requestCode == REQUST_CODE){
            data?.data?.let {
                curFile = it
                findViewById<ImageView>(R.id.imageView).setImageURI(it)
            }
        }
    }




}
