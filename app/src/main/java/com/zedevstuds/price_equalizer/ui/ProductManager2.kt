package com.zedevstuds.price_equalizer.ui

import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.App
import com.zedevstuds.price_equalizer.utils.PriceStatus
import java.math.BigDecimal
import java.math.RoundingMode

class ProductManager2 {

    private val roundScale = 7  // точночсть, с которой считается цена за мин. единицу продукта

    private val productList = ArrayList<Product2>()

    fun addNewProduct(price: BigDecimal, amount: BigDecimal, unit: Int, calcCases: List<Int>,
                      title: String = ""): ArrayList<Product2> {
        val amountInGrams =  if (unit == R.string.kilogram || unit == R. string.liter || unit == R.string.meter || unit == R.string.piece) {
            amount * 1000.toBigDecimal()
        } else amount
        val priceForSmallestValue = price.divide(amountInGrams, roundScale, RoundingMode.HALF_UP)
        val product = Product2(price, amount, priceForSmallestValue, unit)
        productList.add(product)
        setDifference(productList)
        product.priceList = getPriceList(calcCases, product.priceForSmallestValue)
        product.amountList = getAmountList(calcCases, unit)
        productList.forEach {
            it.difList = getDifList(calcCases, it.difForSmallestValue)
        }
        return productList
    }

    fun removeProduct(list: ArrayList<Product2>, position: Int): ArrayList<Product2> {
        list.removeAt(position)
        setDifference(list)
        return list
    }

    // Возвращает список всех цен и разности
    private fun getPriceList(calsCases: List<Int>, smallestPrice: BigDecimal): ArrayList<String> {
        val priceList = ArrayList<String>()
        calsCases.forEach { calcCase ->
            val price = calcCase.toBigDecimal() * smallestPrice
            priceList.add(price.setFixedScale().toString())
        }
        return priceList
    }

    // Возвращает список разности
    private fun getDifList(calsCases: List<Int>, smallestDif: BigDecimal): ArrayList<String> {
        val difList = ArrayList<String>()
        calsCases.forEach { calcCase ->
            val price = calcCase.toBigDecimal() * smallestDif
            difList.add(price.setFixedScale().toString())
        }
        return difList
    }

    // Возвращает список amount вместе с единицами измерения
    private fun getAmountList(calsCases: List<Int>, unit: Int): ArrayList<String> {
        val amountList = ArrayList<String>()
        calsCases.forEach { calcCase ->
            val amount = getUnit(unit, calcCase)
            amountList.add(amount)
        }
        return amountList
    }

    private fun setDifference(list: ArrayList<Product2>) {
        val maxPrice = findMaxPrice(list)
        val minPrice = findMinPrice(list)
        for (product in list) {
            product.difForSmallestValue = product.priceForSmallestValue - minPrice
            when(product.priceForSmallestValue) {
                minPrice -> product.status = PriceStatus.MIN
                maxPrice -> product.status = PriceStatus.MAX
                else -> product.status = PriceStatus.NEUT
            }
        }
    }

    private fun findMaxPrice(list: ArrayList<Product2>): BigDecimal {
        return if (list.isNotEmpty()) list.maxByOrNull { it.priceForSmallestValue }!!.priceForSmallestValue
        else BigDecimal(0)
    }

    private fun findMinPrice(list: ArrayList<Product2>): BigDecimal {
        return if (list.isNotEmpty()) list.minByOrNull { it.priceForSmallestValue }!!.priceForSmallestValue
        else BigDecimal(0)
    }

    // Устанавливает кол-во знаков после запятой в зависимости от размера BigDecimal
    private fun BigDecimal.setFixedScale(): BigDecimal {
        return if (this >= 1000.toBigDecimal())
            this.setScale(0, RoundingMode.HALF_UP)
        else this.setScale(2, RoundingMode.HALF_UP)
    }

    // Возвращает единицу измерения в зависимости от количества товара
    private fun getUnit(unit: Int, amount: Int): String {
        return when(unit) {
            R.string.kilogram, R.string.gram -> {
                if (amount >= 1000) "${amount / 1000} ${App.app.getString(R.string.kilogram)}"
                else "$amount ${App.app.getString(R.string.gram)}"
            }
            R.string.liter, R.string.milliliter -> {
                if (amount >= 1000) "${amount / 1000} ${App.app.getString(R.string.liter)}"
                else "$amount ${App.app.getString(R.string.milliliter)}"
            }
            R.string.meter, R.string.centimeter, R.string.millimeter -> {
                if (amount >= 1000) "${amount / 1000} ${App.app.getString(R.string.meter)}"
                else "$amount ${App.app.getString(R.string.millimeter)}"
            }
            R.string.piece -> {
                // Т.к. в модели хранится цена за 0,001 шт., для получения 1 шт. необходимо кол-во разделить на 1000
                "${amount / 1000} ${App.app.getString(R.string.piece)}"
            }
            else -> "Error!"
        }
    }

}