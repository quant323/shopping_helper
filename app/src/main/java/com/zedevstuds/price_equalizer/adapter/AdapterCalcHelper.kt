package com.zedevstuds.price_equalizer.adapter

import android.content.Context
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product
import java.math.BigDecimal
import java.math.RoundingMode

// Вспомогательный класс для осуществления расчетов
class AdapterCalcHelper(private val context: Context) {

    private val divider = "/"
    private var currency: String = ""

    // Первичная инициализация денежных единиц (обязательно выполняется вначале!!!)
    fun setCurrency(currency: Int?) {
        currency?.let { this.currency = context.getString(it) }
    }

    // Возвращает текущие параметры введенного продукта как строку
    fun getCurPriceString(product: Product, position: Int): String {
        return "${position + 1}) ${product.curPrice}${currency}$divider${product.curAmount}${context.getString(product.curUnit)}"
    }

    // Возвращает стоимость товара за указанные единицы как строку
    fun getCalcPriceString(product: Product, amount: Int): String {
        return "${(product.priceForGram * amount.toBigDecimal()).setFixedScale()}$currency${divider}${getUnit(product.curUnit, amount)}"
    }

    // Возвращает разницу цены за указанное кол-во продукта
    fun getDifString(product: Product, amount: Int): String {
        return "+${(product.difInGrams * amount.toBigDecimal()).setFixedScale()}$currency"
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
                "${amount.toDouble() / 1000.0}${context.getString(R.string.piece)}"
            }
            else -> "Error!"
        }
    }

    // Устанавливает кол-во знаков после запятой в зависимости от размера BigDecimal
    private fun BigDecimal.setFixedScale(): BigDecimal {
        return if (this >= 1000.toBigDecimal())
            this.setScale(0, RoundingMode.HALF_UP)
        else this.setScale(2, RoundingMode.HALF_UP)
    }

}