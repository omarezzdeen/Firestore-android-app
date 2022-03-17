package com.example.firestoreandroidapp.screens

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.example.firestoreandroidapp.ui.MainActivity
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.logging.LogManager
val REQUST_CODE = 0

class EditBookActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    private val db = Firebase.firestore
    private val bookImageRef = Firebase.storage.reference
    var curFile: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)


        val buttonEdit = findViewById<Button>(R.id.btn_edit_book)
        val buttonDelete = findViewById<Button>(R.id.btn_delete_book)


        val uploadImageButton = findViewById<Button>(R.id.btn_upload_image)

        uploadImageButton.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUST_CODE)
            }
        }

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Activity.RESULT_OK && requestCode == REQUST_CODE){
            data?.data?.let {
                curFile = it
                findViewById<ImageView>(R.id.imageView).setImageURI(it)
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



    private fun setupBookData() : Books{
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
        return Books(bookCollectionRef.id,ed_BookName.text.toString(), ed_BookAuthor.text.toString(), ed_BookLaunchYear.text.toString(), ed_BookPrice.text.toString().toDouble())
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
        bookCollectionRef.whereEqualTo("price", book.price).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.documents.isNotEmpty()) {
                        for (document in task.result!!.documents) {
                            bookCollectionRef.document(document.id)
                                .set(newBookMap, SetOptions.merge())
                            uploadToFirebase("myImage")
                            downloadImage("myImage")
                            startActivity(Intent(this, MainActivity::class.java))
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
        bookCollectionRef.whereEqualTo("bookName", book.bookName).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.documents.isNotEmpty()) {
                        for (document in task.result!!.documents) {
                            bookCollectionRef.document(document.id).delete()
                            startActivity(Intent(this, MainActivity::class.java))
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
        val bookName = findViewById<EditText>(R.id.et_book_name_edit).text.toString()
        val bookAuthor = findViewById<EditText>(R.id.et_book_author_edit).text.toString()
        val bookLaunchYear = findViewById<EditText>(R.id.et_launch_year_edit).text.toString()
        val bookPrice = findViewById<EditText>(R.id.et_price_edit).text.toString()

        return Books(bookID, bookName, bookAuthor, bookLaunchYear, bookPrice.toDouble())
        Log.d(TAG, "------------->${bookName}")
    }
}