package com.zedevstuds.price_equalizer.utils

import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product
import java.math.BigDecimal
import java.math.RoundingMode

object ProductUtil {

    fun createProduct(price: String, amount: String, currency: Int, curUnit: Int) : Product {
        val priceBigDecimal = price.toBigDecimal().setFixedScale()
        val amountBigDecimal = amount.toBigDecimal().setFixedScale()
        val amountPerOne = equalizeAmount(amountBigDecimal, curUnit)
        val pricePerOne = calcPrice(amountPerOne, priceBigDecimal)
        val unitPerOne = convertMeasureUnit(curUnit)
        val curProduct = Product(price, amount, curUnit, currency, pricePerOne, amountPerOne, unitPerOne)
        setRestValues(curProduct)
        return curProduct
    }

    private fun setRestValues(curProduct: Product) {
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

    fun setDifferences(list: ArrayList<Product>) : ArrayList<Product> {
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
    }

    private fun findMinPrice(list: ArrayList<Product>): BigDecimal {
        if (list.isNotEmpty()) return list.minBy { it.pricePerOne }!!.pricePerOne
        else return BigDecimal(0)
    }

    private fun findMaxPrice(list: ArrayList<Product>): BigDecimal {
        if (list.isNotEmpty()) return list.maxBy { it.pricePerOne }!!.pricePerOne
        else return BigDecimal(0)
    }

    private fun calcPrice(amount: BigDecimal, price: BigDecimal): BigDecimal {
        return price.div(amount).setFixedScale()
    }

    private fun calcProfit(maxPrice: BigDecimal, productList: ArrayList<Product>) {
        for (product in productList) {
            product.profit = maxPrice - product.pricePerOne
        }
    }

    private fun calcDifference(minPrice: BigDecimal, maxPrice: BigDecimal, productList: ArrayList<Product>) {
        for (product in productList) {
            product.difference1 = (product.pricePerOne - minPrice).setFixedScale()
            if (product.unitPerOne != R.string.piece) {
                product.difference2 = product.difference1.divide(2.toBigDecimal()).setFixedScale()
                product.difference3 = product.difference1.divide(10.toBigDecimal()).setFixedScale()
            } else {
                product.difference2 = product.difference1.multiply(10.toBigDecimal()).setFixedScale()
                product.difference3 = product.difference1.multiply(100.toBigDecimal()).setFixedScale()
            }
            when (product.pricePerOne) {
                minPrice -> product.status = MIN
                maxPrice -> product.status = MAX
                else -> product.status = NEUT
            }
        }
    }

}