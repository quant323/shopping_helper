package com.zedevstuds.price_equalizer.ui

import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.PriceStatus
import java.math.BigDecimal
import java.math.RoundingMode

class ProductManager {

    fun addNewProduct(list: ArrayList<Product2>, price: BigDecimal, amount: BigDecimal, unit: Int): ArrayList<Product2> {
        val amountInGrams =  if (unit == R.string.kilogram || unit == R. string.liter || unit == R.string.meter || unit == R.string.piece)
            amount * 1000.toBigDecimal()
        else amount
        val priceForGram = price.divide(amountInGrams, 10, RoundingMode.HALF_UP)
        val product = Product2(price, amount, unit,priceForGram)
        list.add(product)
        setDifference(list)
        return list
    }

    fun removeProduct(list: ArrayList<Product2>, position: Int): ArrayList<Product2> {
        list.removeAt(position)
        setDifference(list)
        return list
    }

    private fun setDifference(list: ArrayList<Product2>) {
        val maxPrice = findMaxPrice(list)
        val minPrice = findMinPrice(list)
        for (product in list) {
            product.difInGrams = product.priceForGram - minPrice
            when(product.priceForGram) {
                minPrice -> product.status = PriceStatus.MIN
                maxPrice -> product.status = PriceStatus.MAX
                else -> product.status = PriceStatus.NEUT
            }
        }
    }

    private fun findMaxPrice(list: ArrayList<Product2>): BigDecimal {
        if (list.isNotEmpty()) return list.maxBy { it.priceForGram }!!.priceForGram
        else return BigDecimal(0)
    }

    private fun findMinPrice(list: ArrayList<Product2>): BigDecimal {
        if (list.isNotEmpty()) return list.minBy { it.priceForGram }!!.priceForGram
        else return BigDecimal(0)
    }

}