package com.stanislav_xyz.shopping_converter_v1.utils

import com.stanislav_xyz.shopping_converter_v1.R
import java.math.BigDecimal
import java.math.RoundingMode

object ProductUtil {

    fun createProduct(price: String, amount: String, currency: Int, curUnit: Int) {
        val priceBigDecimal = price.toBigDecimal().setFixedScale()
        val amountBigDecimal = amount.toBigDecimal().setFixedScale()
        val amountPerOne = equalizeAmount(amountBigDecimal, curUnit)
        val pricePerOne = calcPrice(amountPerOne, priceBigDecimal)
        val measureUnit = convertMeasureUnit(curUnit)
    }

    // Переводит граммы в килограммы, милилитры в литры
    private fun equalizeAmount(amount: BigDecimal, unit: Int): BigDecimal {
        return if (unit == R.string.gram || unit == R.string.milliliter) {
            amount.divide(BigDecimal(1000), 5, RoundingMode.HALF_UP)
        }
        else amount
    }

    private fun convertMeasureUnit(measureUnit: Int?): Int {
        return if (measureUnit == R.string.gram || measureUnit == R.string.kilogram) R.string.kilogram
        else if (measureUnit == R.string.liter || measureUnit == R.string.milliliter) R.string.liter
        else R.string.gram
    }

}