package com.zedevstuds.price_equalizer.ui

import android.content.Context
import android.util.Log
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.setFixedScale

class AdapterHelper(private val context: Context) {

    private val divider = "/"
    private var product: Product2? = null
    private var currency: String = ""

    // Первичная инициализация продукта (обязательно выполняется вначале!!!)
    fun setProduct(product: Product2) {
        this.product = product
    }

    // Первичная инициализация денежных единиц (обязательно выполняется вначале!!!)
    fun setCurrency(currency: Int?) {
        currency?.let { this.currency = context.getString(it) }
    }

    // Возвращает текущие параметры введенного продукта как строку
    fun getCurPriceString(position: Int): String {
        product?.let { return "${position + 1}) ${it.curPrice}${currency}$divider${it.curAmount}${context.getString(it.curUnit)}" }
        return "Error!"
    }

    // Возвращает стоимость товара за указанные единицы как строку
    fun getPriceString(amount: Int): String {
        product?.let {
            return "${(it.priceForGram * amount.toBigDecimal()).setFixedScale()}$currency${divider}${getUnit(it.curUnit, amount)}"
        }
        return "Error!"
    }

    // Возвращает разницу цены за указанное кол-во продукта
    fun getDifString(amount: Int): String {
        product?.let { return "+${(it.difInGrams * amount.toBigDecimal()).setFixedScale()}$currency" }
        return "Error!"
    }

    // Возвращает единицу измерения в зависимости от количества товара
    private fun getUnit(unit: Int, amount: Int): String {
        return when(unit) {
            R.string.kilogram, R.string.gram -> {
                if (amount >= 1000) "${amount / 1000}${context.getString(R.string.kilogram)}"
                else "$amount${context.getString(R.string.gram)}"
            }
            R.string.liter, R.string.milliliter -> {
                if (amount >= 1000) "${amount / 1000}${context.getString(R.string.liter)}"
                else "$amount${context.getString(R.string.milliliter)}"
            }
            R.string.meter, R.string.centimeter, R.string.millimeter -> {
                if (amount >= 1000) "${amount / 1000}${context.getString(R.string.meter)}"
                else "$amount${context.getString(R.string.millimeter)}"
            }
            R.string.piece -> {
                // Т.к. в модели хранится цена за 0,001 шт., для получения 1 шт. необходимо кол-во разделить на 1000
                val a = amount.toDouble() / 1000.0
                "${amount.toDouble() / 1000.0}${context.getString(R.string.piece)}"
            }
            else -> "Error!"
        }
    }

}