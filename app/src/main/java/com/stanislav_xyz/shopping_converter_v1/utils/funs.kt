package com.stanislav_xyz.shopping_converter_v1.utils

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import android.widget.Toast
import com.stanislav_xyz.shopping_converter_v1.models.Product
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
    return this.setScale(2, RoundingMode.HALF_UP)
}

fun findMinPrice(list: ArrayList<Product>): BigDecimal {
    if (list.isNotEmpty()) return list.minBy { it.pricePerOne }!!.pricePerOne
    else return BigDecimal(0)
}

fun findMaxPrice(list: ArrayList<Product>): BigDecimal {
    if (list.isNotEmpty()) return list.maxBy { it.pricePerOne }!!.pricePerOne
    else return BigDecimal(0)
}

fun calcPrice(amount: BigDecimal, price: BigDecimal): BigDecimal {
    return price.div(amount).setFixedScale()
}