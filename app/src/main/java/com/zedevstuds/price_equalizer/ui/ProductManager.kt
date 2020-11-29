package com.zedevstuds.price_equalizer.ui

import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product
import com.zedevstuds.price_equalizer.utils.PriceStatus
import java.math.BigDecimal
import java.math.RoundingMode

class ProductManager {

    private val roundScale = 7  // точночсть, с которой считается цена за мин. единицу продукта

    fun addNewProduct(list: ArrayList<Product>, price: BigDecimal, amount: BigDecimal, unit: Int): ArrayList<Product> {
        val amountInGrams =  if (unit == R.string.kilogram || unit == R. string.liter || unit == R.string.meter || unit == R.string.piece)
            amount * 1000.toBigDecimal()
        else amount
        val priceForGram = price.divide(amountInGrams, roundScale, RoundingMode.HALF_UP)
        val product = Product(price, amount, unit,priceForGram)
        list.add(product)
        setDifference(list)
        return list
    }

    fun removeProduct(list: ArrayList<Product>, position: Int): ArrayList<Product> {
        list.removeAt(position)
        setDifference(list)
        return list
    }

    private fun setDifference(list: ArrayList<Product>) {
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

    private fun findMaxPrice(list: ArrayList<Product>): BigDecimal {
        if (list.isNotEmpty()) return list.maxBy { it.priceForGram }!!.priceForGram
        else return BigDecimal(0)
    }

    private fun findMinPrice(list: ArrayList<Product>): BigDecimal {
        if (list.isNotEmpty()) return list.minBy { it.priceForGram }!!.priceForGram
        else return BigDecimal(0)
    }

}