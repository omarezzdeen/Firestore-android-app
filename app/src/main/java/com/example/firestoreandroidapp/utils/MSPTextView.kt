package com.example.firestoreandroidapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextView(context: Context, attributeSet: AttributeSet): AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }
    private fun applyFont(){
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)

    }
}