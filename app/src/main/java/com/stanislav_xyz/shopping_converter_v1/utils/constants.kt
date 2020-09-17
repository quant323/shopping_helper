package com.stanislav_xyz.shopping_converter_v1.utils

import com.stanislav_xyz.shopping_converter_v1.ui.MainActivity
import com.stanislav_xyz.shopping_converter_v1.R

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
