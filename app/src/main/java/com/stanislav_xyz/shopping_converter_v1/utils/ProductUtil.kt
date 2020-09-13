package com.stanislav_xyz.shopping_converter_v1.utils

import com.stanislav_xyz.shopping_converter_v1.R
import com.stanislav_xyz.shopping_converter_v1.models.Product
import com.stanislav_xyz.shopping_converter_v1.models.Product2
import java.math.BigDecimal
import java.math.RoundingMode

object ProductUtil {

    fun createProduct(price: String, amount: String, currency: Int, curUnit: Int) : Product2 {
        val priceBigDecimal = price.toBigDecimal().setFixedScale()
        val amountBigDecimal = amount.toBigDecimal().setFixedScale()
        val amountPerOne = equalizeAmount(amountBigDecimal, curUnit)
        val pricePerOne = calcPrice(amountPerOne, priceBigDecimal)
        val unitPerOne = convertMeasureUnit(curUnit)
        val curProduct = Product2(price, amount, curUnit, currency, pricePerOne, amountPerOne, unitPerOne)
        setRestValues(curProduct)
        return curProduct
    }

    private fun setRestValues(curProduct: Product2) {
        when(curProduct.unitPerOne) {
            R.string.kilogram, R.string.gram -> {
                curProduct.price2 = curProduct.pricePerOne.divide(2.toBigDecimal()).setFixedScale()
                curProduct.price3 = curProduct.pricePerOne.divide(10.toBigDecimal()).setFixedScale()
                curProduct.unit1 = "1${APP_ACTIVITY.getString(R.string.kilogram)}"
                curProduct.unit2 = "0.5${APP_ACTIVITY.getString(R.string.kilogram)}"
                curProduct.unit3 = "100${APP_ACTIVITY.getString(R.string.gram)}"
            }
            R.string.liter, R.string.milliliter -> {
                curProduct.price2 = curProduct.pricePerOne.divide(2.toBigDecimal()).setFixedScale()
                curProduct.price3 = curProduct.pricePerOne.divide(10.toBigDecimal()).setFixedScale()
                curProduct.unit1 = "1${APP_ACTIVITY.getString(R.string.liter)}"
                curProduct.unit2 = "0.5${APP_ACTIVITY.getString(R.string.liter)}"
                curProduct.unit3 = "100${APP_ACTIVITY.getString(R.string.milliliter)}"
            }
            else -> {
                curProduct.price2 = curProduct.pricePerOne.multiply(10.toBigDecimal()).setFixedScale()
                curProduct.price3 = curProduct.pricePerOne.multiply(100.toBigDecimal()).setFixedScale()
                curProduct.unit1 = "1${APP_ACTIVITY.getString(R.string.piece)}"
                curProduct.unit2 = "10${APP_ACTIVITY.getString(R.string.piece)}"
                curProduct.unit3 = "100${APP_ACTIVITY.getString(R.string.piece)}"
            }
        }
    }

    fun setDifferences(list: ArrayList<Product2>) : ArrayList<Product2> {
        val minPrice = findMinPrice(list)
        val maxPrice = findMaxPrice(list)
        calcDifference(minPrice, maxPrice, list)
        calcProfit(maxPrice, list)

        return list
    }

    // Переводит граммы в килограммы, милилитры в литры
    private fun equalizeAmount(amount: BigDecimal, unit: Int): BigDecimal {
        return if (unit == R.string.gram || unit == R.string.milliliter) {
            amount.divide(BigDecimal(1000), 5, RoundingMode.HALF_UP)
        }
        else amount
    }

    private fun convertMeasureUnit(measureUnit: Int?): Int {
        return when(measureUnit) {
            R.string.gram, R.string.kilogram -> R.string.kilogram
            R.string.liter, R.string.milliliter -> R.string.liter
            else -> R.string.piece
        }
//        return if (measureUnit == R.string.gram || measureUnit == R.string.kilogram) R.string.kilogram
//        else if (measureUnit == R.string.liter || measureUnit == R.string.milliliter) R.string.liter
//        else R.string.gram
    }

}