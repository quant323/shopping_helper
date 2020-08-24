package com.stanislav_xyz.shopping_converter_v1

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.stanislav_xyz.shopping_converter_v1.models.Product
import com.stanislav_xyz.shopping_converter_v1.utils.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var priceDouble: Double? = 0.0
    private var amountDouble: Double? = 0.0
    private var minPrice: BigDecimal = BigDecimal(0)
    private var maxPrice: BigDecimal = BigDecimal(0)
    private var measureUnitPosition = 0

    private val tempList = ArrayList<Product>()
    private val measureUnitArray = arrayOf(
        R.string.gram, R.string.kilogram,
        R.string.liter, R.string.milliliter
    )

    var productList = MutableLiveData<ArrayList<Product>>()
    var curMeasureUnit = MutableLiveData<Int>()
    var currency = MutableLiveData<String>()
    var isStageOne = MutableLiveData<Boolean>()
    var priceString = MutableLiveData<String>()
    var amountString = MutableLiveData<String>()
    var curLiveText = MutableLiveData<String>()

    init {
        isStageOne.value = true
        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
        currency.value = RUBLE
        curLiveText = priceString
        resetValues()
    }

    fun onOkPressed() {
        if (isStageOne.value!!) {
            isStageOne.value = false
            curLiveText = amountString
        } else {
            if (amountString.value != "0") {
                priceDouble = priceString.value?.toDouble()
                amountDouble = amountString.value?.toDouble()
                val amountPerOne = equalizeAmount(amountDouble)
                val pricePerOne = calcPrice(amountPerOne, priceDouble)
                val measureUnit = setMeasureUnit(curMeasureUnit.value)
                val curProduct = Product(
                    priceDouble, pricePerOne, amountDouble,
                    amountPerOne, curMeasureUnit.value!!, measureUnit
                )
                tempList.add(curProduct)
                minPrice = findMinPrice(tempList)
                maxPrice = findMaxPrice(tempList)
                calcDifference(minPrice, tempList)
                calcProfit(maxPrice, tempList)
                productList.value = tempList
                isStageOne.value = true
                resetValues()
                curLiveText = priceString
            } else showToast(
                getApplication(),
                getStringFromResource(R.string.toast_no_products_quantity)
            )
        }
    }

    private fun resetValues() {
        priceString.value = "0"
        amountString.value = "0"
    }

    private fun getStringFromResource(resource: Int): String {
        return (getApplication() as Context).getString(resource)
    }

    fun onKeyPressed(keyPressed: String) {
        when (keyPressed) {
            "." -> {
                if (!curLiveText.value?.contains(".")!!)
                    curLiveText.value = curLiveText.value.plus(keyPressed)
            }
            else -> {
                if (curLiveText.value == "0")
                    curLiveText.value = keyPressed
                else curLiveText.value = curLiveText.value.plus(keyPressed)
            }
        }
    }

    fun onChangeMeasureUnit() {
        measureUnitPosition++
        if (measureUnitPosition == measureUnitArray.size) measureUnitPosition = 0
        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
    }

    fun onClean() {
        resetValues()
        productList.value?.clear()
        isStageOne.value = true
    }

    fun onDel() {
        curLiveText.value = curLiveText.value?.dropLast(1)
        if (curLiveText.value?.isEmpty()!!) curLiveText.value = "0"
    }

    private fun findMinPrice(list: ArrayList<Product>): BigDecimal {
        if (list.isNotEmpty()) return list.minBy { it.pricePerOne }!!.pricePerOne
        else return BigDecimal(0)
    }

    private fun findMaxPrice(list: ArrayList<Product>): BigDecimal {
        if (list.isNotEmpty()) return list.maxBy { it.pricePerOne }!!.pricePerOne
        else return BigDecimal(0)
    }

    private fun calcPrice(amount: Double?, price: Double?): BigDecimal {
        return BigDecimal(price?.div(amount!!)!!).setScale(2, RoundingMode.HALF_EVEN)
    }

    // Переводит граммы в килограммы, милилитры в литры
    private fun equalizeAmount(amount: Double?): Double? {
        return if (curMeasureUnit.value == R.string.gram || curMeasureUnit.value == R.string.milliliter) amount?.div(
            1000
        )
        else amount
    }

    private fun setMeasureUnit(measureUnit: Int?): Int {
        return if (measureUnit == R.string.gram || measureUnit == R.string.kilogram) R.string.kilogram
        else if (measureUnit == R.string.liter || measureUnit == R.string.milliliter) R.string.liter
        else R.string.gram
    }

    private fun calcDifference(minPrice: BigDecimal, productList: ArrayList<Product>) {
        for (product in productList) {
            product.difference = product.pricePerOne - minPrice
            when (product.pricePerOne) {
                minPrice -> product.status = MIN
                maxPrice -> product.status = MAX
                else -> product.status = NEUT
            }
        }
    }

    private fun calcProfit(maxPrice: BigDecimal, productList: ArrayList<Product>) {
        for (product in productList) {
            product.profit = maxPrice - product.pricePerOne
        }
    }

}


