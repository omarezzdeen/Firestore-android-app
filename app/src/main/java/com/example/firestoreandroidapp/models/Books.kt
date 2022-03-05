package com.example.firestoreandroidapp.models

import android.os.Parcelable
import java.util.*

data class Books(
    val id: String = "",
    val bookName: String = "",
    val bookAuthor: String = "",
    val launchYear: Date = Date(),
    val price: Double = 0.0,
)
