package com.example.firestoreandroidapp.models

import android.os.Parcel
import android.os.Parcelable

data class Books(
    var id: String? = "",
    val bookName: String? = "",
    val bookAuthor: String? = "",
    val launchYear: String?= "",
    val price: Double? = 0.0,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(bookName)
        parcel.writeString(bookAuthor)
        parcel.writeString(launchYear)
        parcel.writeValue(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Books> {
        override fun createFromParcel(parcel: Parcel): Books {
            return Books(parcel)
        }

        override fun newArray(size: Int): Array<Books?> {
            return arrayOfNulls(size)
        }
    }
}