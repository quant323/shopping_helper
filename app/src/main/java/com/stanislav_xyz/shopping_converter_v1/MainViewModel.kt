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

    private var priceBigDecimal: BigDecimal = BigDecimal(0)
    private var amountBigDecimal: BigDecimal = BigDecimal(0)
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
    var isPriceSelected = MutableLiveData(true)
    var priceString = MutableLiveData("0")
    var amountString = MutableLiveData("0")
    var curLiveText = MutableLiveData<String>()
//    var isDotEnabled = MutableLiveData<Boolean>()

    init {
        curLiveText = priceString
        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
        currency.value = RUBLE
        //      setDotButton()
    }

    fun onOkClicked() {
        if (isPriceSelected.value!!) onAmountClicked()
        else addNewProduct()
    }

    private fun addNewProduct() {
        try {
            priceBigDecimal = priceString.value!!.toBigDecimal()
            amountBigDecimal = amountString.value!!.toBigDecimal()
            val amountPerOne = equalizeAmount(amountBigDecimal)
            val pricePerOne = calcPrice(amountPerOne, priceBigDecimal)
            val measureUnit = setMeasureUnit(curMeasureUnit.value)
            val curProduct = Product(
                priceBigDecimal.toString(), pricePerOne, amountBigDecimal.toString(),
                amountPerOne, curMeasureUnit.value!!, measureUnit
            )
            tempList.add(curProduct)
            minPrice = findMinPrice(tempList)
            maxPrice = findMaxPrice(tempList)
            calcDifference(minPrice, tempList)
            calcProfit(maxPrice, tempList)
            productList.value = tempList
            resetState()
        } catch (e: ArithmeticException) {
            showToast(
                getApplication(),
                getStringFromResource(R.string.toast_no_products_quantity)
            )
        }
    }

    private fun resetState() {
        priceString.value = "0"
        amountString.value = "0"
        isPriceSelected.value = true
        curLiveText = priceString
    }

    private fun getStringFromResource(resource: Int): String {
        return (getApplication() as Context).getString(resource)
    }

//    private fun setDotButton() {
//        if (!isPriceSelected.value!!)
//            isDotEnabled.value =
//                curMeasureUnit.value == R.string.kilogram || curMeasureUnit.value == R.string.liter
//        else isDotEnabled.value = true
//    }

    fun onKeyPressed(keyPressed: String) {
        when (keyPressed) {
            "." -> {
                if (!curLiveText.value?.contains(".")!!)
                    curLiveText.value = curLiveText.value.plus(keyPressed)
            }
            else -> {
                if (isLengthTooBig()) return
                else {
                    if (curLiveText.value == "0")
                        curLiveText.value = keyPressed
                    else curLiveText.value = curLiveText.value.plus(keyPressed)
                }
            }
        }

    }

    private fun isLengthTooBig(): Boolean {
        if (curLiveText.value?.contains(".")!!) {
            val splitTextList = curLiveText.value?.split(".")
            if (splitTextList?.get(1)?.length!! > 1) return true
        } else
            if (curLiveText.value?.length!! > 4) return true
        return false
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

    // Переводит граммы в килограммы, милилитры в литры
    private fun equalizeAmount(amount: BigDecimal): BigDecimal {
        return if (curMeasureUnit.value == R.string.gram || curMeasureUnit.value == R.string.milliliter) {
           amount.divide(BigDecimal(1000), 5, RoundingMode.HALF_UP)
        }
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

    fun onPriceClicked() {
        isPriceSelected.value = true
        curLiveText = priceString
    }

    fun onAmountClicked() {
        isPriceSelected.value = false
        curLiveText = amountString
        //      setDotButton()
    }

    fun onChangeUnitClicked() {
        if (!isPriceSelected.value!!) {
            measureUnitPosition++
            if (measureUnitPosition == measureUnitArray.size) measureUnitPosition = 0
            curMeasureUnit.value = measureUnitArray[measureUnitPosition]
            //          setDotButton()
        }
    }

    fun onClean() {
        resetState()
        tempList.clear()
        productList.value = tempList
        //      setDotButton()
    }

    fun onDel() {
        curLiveText.value = curLiveText.value?.dropLast(1)
        if (curLiveText.value?.isEmpty()!!) curLiveText.value = "0"
    }

}


