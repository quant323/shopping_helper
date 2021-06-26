package com.zedevstuds.price_equalizer.ui

import android.R
import android.content.Context
import android.graphics.Typeface
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

const val LARGE_TEXT_SIZE = 22.0f
const val NORMAL_TEXT_SIZE = 20.0f

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Увеличивает размер текста и выделяет его жирным
fun increaseText(text: TextView) {
    text.setTypeface(null, Typeface.BOLD)
    text.textSize = LARGE_TEXT_SIZE
}

// Уменьшает размер текста и делает его обычной толщины
fun decreaseText(text: TextView) {
    text.setTypeface(null, Typeface.NORMAL)
    text.textSize = NORMAL_TEXT_SIZE
}

// Создает адаптер для spinner
fun createSpinnerAdapter(context: Context, itemArray: Array<String>): ArrayAdapter<String> {
    return ArrayAdapter(context, R.layout.simple_spinner_item, itemArray).apply {
        setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    }
}