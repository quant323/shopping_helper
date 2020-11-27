package com.zedevstuds.price_equalizer.ui

import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.PriceStatus
import java.math.BigDecimal
import java.math.RoundingMode

class ProductManager {

    companion object {
        var maxPrice = BigDecimal(0)
        var minPrice = BigDecimal(0)
    }

    private val productList = ArrayList<Product2>()

    fun addNewProduct(price: BigDecimal, amount: BigDecimal, unit: Int): ArrayList<Product2> {
        val amountInGrams =  if (unit == R.string.kilogram || unit == R. string.liter)
            amount * 1000.toBigDecimal()
        else amount
        val priceForGram = price.divide(amountInGrams, 10, RoundingMode.HALF_UP)
        val product = Product2(price, amount, unit,priceForGram)
        productList.add(product)
        maxPrice = findMaxPrice(productList)
        minPrice = findMinPrice(productList)
        setDifference(productList)
        return productList
    }

    fun removeProduct(position: Int): List<Product2> {
        productList.removeAt(position)
        return productList
    }

    private fun findMaxPrice(list: ArrayList<Product2>): BigDecimal {
        if (list.isNotEmpty()) return list.maxBy { it.priceForGram }!!.priceForGram
        else return BigDecimal(0)
    }

    private fun findMinPrice(list: ArrayList<Product2>): BigDecimal {
        if (list.isNotEmpty()) return list.minBy { it.priceForGram }!!.priceForGram
        else return BigDecimal(0)
    }

    private fun setDifference(list: ArrayList<Product2>) {
        for (product in list) {
            product.difInGrams = product.priceForGram - minPrice
            when(product.priceForGram) {
                minPrice -> product.status = PriceStatus.MIN
                maxPrice -> product.status = PriceStatus.MAX
                else -> product.status = PriceStatus.NEUT
            }
        }
    }

}