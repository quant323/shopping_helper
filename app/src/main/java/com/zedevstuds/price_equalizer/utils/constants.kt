package com.zedevstuds.price_equalizer.utils

import com.zedevstuds.price_equalizer.ui.MainActivity
import com.zedevstuds.price_equalizer.R

const val TAG = "myTag"

lateinit var APP_ACTIVITY: MainActivity

enum class PriceStatus { MAX, MIN, NEUT }

// todo delete
const val MAX = "max"
const val MIN = "min"
const val NEUT = "neut"

val currencyArray = arrayOf(R.string.ruble, R.string.dollar, R.string.euro)

val measureUnitArray = arrayOf(
    R.string.kilogram, R.string.gram,
    R.string.liter, R.string.milliliter, R.string.meter, R.string.millimeter, R.string.piece
)

const val LARGE_TEXT_SIZE = 22.0f
const val NORMAL_TEXT_SIZE = 20.0f

const val MAX_NUMBER_LENGTH = 6     // max длина вводимого числа
const val MAX_DECIMAL_LENGTH = 2    // max длина десятичной части числа

// Знаки клавиш клавиатуры
val keyArray = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
const val DOT = "."
