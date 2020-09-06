package com.stanislav_xyz.shopping_converter_v1.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.stanislav_xyz.shopping_converter_v1.R
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
    private var currencyPosition = 0

    private val tempList = ArrayList<Product>()

    var productList = MutableLiveData<ArrayList<Product>>()
    var curMeasureUnit = MutableLiveData<Int>()
    var currency = MutableLiveData<Int>()
    var isPriceSelected = MutableLiveData(true)
    var priceString = MutableLiveData("0")
    var amountString = MutableLiveData("0")
    private var curLiveText = MutableLiveData<String>()
//    var isDotEnabled = MutableLiveData<Boolean>()

    init {
        curLiveText = priceString
//        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
//        currency.value = currencyArray[0]
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
            val measureUnit = convertMeasureUnit(curMeasureUnit.value)
            val curProduct = Product(
                priceBigDecimal.toString(), pricePerOne, amountBigDecimal.toString(),
                amountPerOne, curMeasureUnit.value!!, measureUnit, currency.value!!
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

    // Переводит граммы в килограммы, милилитры в литры
    private fun equalizeAmount(amount: BigDecimal): BigDecimal {
        return if (curMeasureUnit.value == R.string.gram || curMeasureUnit.value == R.string.milliliter) {
           amount.divide(BigDecimal(1000), 5, RoundingMode.HALF_UP)
        }
        else amount
    }

    private fun convertMeasureUnit(measureUnit: Int?): Int {
        return if (measureUnit == R.string.gram || measureUnit == R.string.kilogram) R.string.kilogram
        else if (measureUnit == R.string.liter || measureUnit == R.string.milliliter) R.string.liter
        else R.string.gram
    }

    private fun setMeasureUnit(id: Int) {
        measureUnitPosition = id
        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
    }

    fun setCurrency(id: Int) {
        currencyPosition = id
        currency.value = currencyArray[currencyPosition]
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

    fun onStart() {
        setMeasureUnit(AppPreference.getMeasureUnit())
        setCurrency(AppPreference.getCurrency())
    }

    fun onStop() {
        AppPreference.saveMeasureUnit(measureUnitPosition)
        AppPreference.saveCurrency(currencyPosition)
    }

}


