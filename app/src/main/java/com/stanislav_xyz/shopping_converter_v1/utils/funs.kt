package com.stanislav_xyz.shopping_converter_v1.utils

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun increaseText(text: TextView) {
    text.setTypeface(null, Typeface.BOLD)
    text.textSize = LARGE_TEXT_SIZE
}

fun decreaseText(text: TextView) {
    text.setTypeface(null, Typeface.NORMAL)
    text.textSize = NORMAL_TEXT_SIZE
}

fun BigDecimal.setFixedScale(): BigDecimal {
    return if (this >= 1000.toBigDecimal())
        this.setScale(0, RoundingMode.HALF_UP)
    else this.setScale(2, RoundingMode.HALF_UP)
}

fun isLengthTooBig(text: String, maxLength: Int): Boolean {
    if (text.contains(".")) {
        val splitTextList = text.split(".")
        if (splitTextList[1].length > 1) return true
    } else
        if (text.length > maxLength) return true
    return false
}