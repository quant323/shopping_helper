package com.zedevstuds.price_equalizer.utils

import com.zedevstuds.price_equalizer.ui.MainActivity
import com.zedevstuds.price_equalizer.R

const val TAG = "myTag"

lateinit var APP_ACTIVITY: MainActivity

enum class PriceStatus { MAX, MIN, NEUT }

val currencyArray = arrayOf(R.string.ruble, R.string.dollar, R.string.euro)

val measureUnitArray = arrayOf(
    R.string.kilogram, R.string.gram,
    R.string.liter, R.string.milliliter, R.string.meter, R.string.millimeter, R.string.piece
)

// Знаки клавиш клавиатуры
val keyArray = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
const val DOT = "."
