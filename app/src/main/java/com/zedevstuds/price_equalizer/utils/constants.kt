package com.zedevstuds.price_equalizer.utils

import com.zedevstuds.price_equalizer.ui.MainActivity
import com.zedevstuds.price_equalizer.R

lateinit var APP_ACTIVITY: MainActivity

const val MAX = "max"
const val MIN = "min"
const val NEUT = "neut"

val currencyArray = arrayOf(R.string.ruble, R.string.dollar, R.string.euro)

val measureUnitArray = arrayOf(
    R.string.kilogram, R.string.gram,
    R.string.liter, R.string.milliliter, R.string.piece
)

const val LARGE_TEXT_SIZE = 22.0f
const val NORMAL_TEXT_SIZE = 20.0f
