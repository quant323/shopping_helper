package com.zedevstuds.price_equalizer.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreference {
    const val NO_VALUE = -1

    private const val APP_PREFERENCES = "APP_PREFERENCES"
    private const val MEASURE_UNIT = "measure_unit"
    private const val CURRENCY = "currency"

    private lateinit var preferences: SharedPreferences

    fun initPreferences(context: Context) : SharedPreferences {
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        return preferences
    }

    fun saveMeasureUnit(unitId: Int) {
        preferences.edit()
            .putInt(MEASURE_UNIT, unitId)
            .apply()
    }

    fun saveCurrency(currency: Int) {
        preferences.edit()
            .putInt(CURRENCY, currency)
            .apply()
    }

    fun getMeasureUnit(): Int {
        return preferences.getInt(MEASURE_UNIT, 0)
    }

    fun getCurrency(): Int {
        return preferences.getInt(CURRENCY, NO_VALUE)
    }

}