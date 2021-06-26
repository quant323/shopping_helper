package com.zedevstuds.price_equalizer.utils

import com.zedevstuds.price_equalizer.ui.MainActivity
import com.zedevstuds.price_equalizer.R

const val TAG = "myTag"

lateinit var APP_ACTIVITY: MainActivity

enum class PriceStatus { MAX, MIN, NEUT }

val currencyArray = listOf(
    R.string.ruble_sign,
    R.string.dollar_sign,
    R.string.euro_sign,
    R.string.ukr_sign,
    R.string.indian_sign,
    R.string.brazil_sign,
    R.string.kazakh_sign,
    R.string.malay_sign,
    R.string.korean_sign,
    R.string.philip_sign
)

val measureUnitArray = arrayOf(
    R.string.kilogram, R.string.gram,
    R.string.liter, R.string.milliliter, R.string.meter, R.string.millimeter, R.string.piece
)

// Знаки клавиш клавиатуры
val keyArray = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
const val DOT = "."

// руссский язык в устройстве
const val LANG_RUS = "ru"
