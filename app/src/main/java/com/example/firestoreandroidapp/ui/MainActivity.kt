package com.example.firestoreandroidapp.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestoreandroidapp.R
import com.example.firestoreandroidapp.models.Books
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val bookCollectionRef = Firebase.firestore.collection("books")
    private val bookAdapter = BookAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrieveData()


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }


    }
    private fun retrieveData(){
        val booksList: ArrayList<Books> = ArrayList()
        val adapterRec = findViewById<RecyclerView>(R.id.recyclerView)
        bookCollectionRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                for (document in task.result!!.documents){
                    val book = document.toObject<Books>()
                    book!!.id == document.id
                    booksList.add(book)
                    val adapter = BookAdapter(booksList)
                    adapterRec.adapter = adapter
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