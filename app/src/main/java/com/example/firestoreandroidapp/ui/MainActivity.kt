package com.example.firestoreandroidapp.ui
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.example.firestoreandroidapp.screens.AddBookActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookArrayList: ArrayList<Books>
    private lateinit var myAdapter: BookAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
//        getBookData()

//        retrieveData()



    }

    private fun getBookData() {
        db.collection("books")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun retrieveData(){
//        val booksList: ArrayList<Books> = ArrayList()
//        val adapterRec = findViewById<RecyclerView>(R.id.recyclerView)
        bookCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                for (document in task.result!!.documents){
                    val book = document.toObject<Books>()
//                    book!!.id == document.id
//                    booksList.add(book)
//                    val adapter = BookAdapter(booksList)
//                    adapterRec.adapter = adapter
                }
                Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"error",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            println(it.message)
        }
    }
}