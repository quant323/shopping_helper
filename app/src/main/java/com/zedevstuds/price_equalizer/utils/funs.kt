package com.zedevstuds.price_equalizer.utils

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

// Устанавливаеь кол-во знаков после запятой в зависимости от размера BigDecimal
fun BigDecimal.setFixedScale(): BigDecimal {
    return if (this >= 1000.toBigDecimal())
        this.setScale(0, RoundingMode.HALF_UP)
    else this.setScale(2, RoundingMode.HALF_UP)
}

// Проверяет, возможно ли еще добавить число к текущему введенному или оно уже максимальной длины
fun isLengthTooBig(text: String, maxLength: Int, maxDecimalLength: Int): Boolean {
    // Проверяем длину числа без учета точки
    val digWithNoDot = text.replace(".", "")
    if (digWithNoDot.length >= maxLength) return true
    if (text.contains(".")) {
        // Если в числе имеется точка - проверяем длину ее десятичной части
        val splitTextList = text.split(".")
        if (splitTextList[1].length >= maxDecimalLength) return true
    }
    return false
}