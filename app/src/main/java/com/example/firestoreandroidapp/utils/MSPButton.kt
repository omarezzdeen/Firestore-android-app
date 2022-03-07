package com.example.firestoreandroidapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class  MSPButton(context: Context, attributeSet: AttributeSet): AppCompatButton(context,attributeSet) {

    init {
        applyFont()
    }
    private fun applyFont(){
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)

    }

}