package com.zedevstuds.price_equalizer.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.FieldPosition

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val maxPriceLength = 3

    private var measureUnitPosition = 0
    private var currencyPosition = 0

    private var curLiveText = MutableLiveData("0")

    private val productManager = ProductManager()

    var productList = MutableLiveData<ArrayList<Product2>>()
    var curMeasureUnit = MutableLiveData<Int>()
    var currency = MutableLiveData<Int>()
    var isAmountSelected = MutableLiveData(false)
    var priceLive = MutableLiveData("0")
    var amountLive = MutableLiveData("0")

    init {
        curLiveText = priceLive
        productList.value = ArrayList()
    }

    // Удаляет элемент из списка продуктов по номеру его позиции в листе
    fun deleteItemFromProductList(position: Int) {
        productList.value = productManager.removeProduct(position)
//        // Необходимо, для срабатывния Observer (если просто добавить/убрать значение - Observer
//        // не сработает)
//        productList.value = productList.value
    }

    fun onOkClicked() {
        addNewProduct()
    }

    fun onAmountClicked() {
        isAmountSelected.value = true
        curLiveText = amountLive
    }

    fun onPriceClicked() {
        isAmountSelected.value = false
        curLiveText = priceLive
    }

    fun onKeyPressed(keyPressed: String) {
        when (keyPressed) {
            "." -> {
                if (!curLiveText.value?.contains(".")!!)
                    curLiveText.value = curLiveText.value.plus(keyPressed)
            }
            else -> {
                if (isLengthTooBig(curLiveText.value.toString(), maxPriceLength)) return
                else {
                    if (curLiveText.value == "0")
                        curLiveText.value = keyPressed
                    else curLiveText.value = curLiveText.value.plus(keyPressed)
                }
            }
        }
    }

    fun onChangeUnitClicked() {
        if (isAmountSelected.value!!) {
            measureUnitPosition++
            if (measureUnitPosition == measureUnitArray.size) measureUnitPosition = 0
            setMeasureUnit()
        }
    }

    fun onClean(view: View) {
        val oldList = productList.value?.map { it.copy() }
        Snackbar.make(view, R.string.message_clean, Snackbar.LENGTH_SHORT)
            .setAction("Cancel") {
                productList.value = oldList as ArrayList<Product2>
            }.show()
        resetState()
        productList.value?.clear()
        productList.value = productList.value
    }

    fun onDel() {
        curLiveText.value = curLiveText.value?.dropLast(1)
        if (curLiveText.value?.isEmpty()!!) curLiveText.value = "0"
    }

    fun onStart() {
        measureUnitPosition = AppPreference.getMeasureUnit()
        setMeasureUnit()
        setCurrency(AppPreference.getCurrency())
    }

    fun onStop() {
        AppPreference.saveMeasureUnit(measureUnitPosition)
        AppPreference.saveCurrency(currencyPosition)
    }

    fun setCurrency(id: Int) {
        currencyPosition = id
        currency.value = currencyArray[currencyPosition]
    }

    fun getCurrencyPos() : Int {
        return currencyPosition
    }


    private fun addNewProduct() {
        try {
//            val curProduct = createProduct(priceLive.value!!, amountLive.value!!, currency.value!!, curMeasureUnit.value!!)
//            productList.value?.add(curProduct)
//            productList.value = setDifferences(productList.value!!)
            productList.value = productManager.addNewProduct(priceLive.value!!.toBigDecimal(), amountLive.value!!.toBigDecimal(), curMeasureUnit.value!!)
            resetState()
        } catch (e: ArithmeticException) {
            e.printStackTrace()
            showToast(
                getApplication(),
                getStringFromResource(R.string.toast_no_products_quantity)
            )
        }
    }

    private fun resetState() {
        priceLive.value = "0"
        amountLive.value = "0"
        isAmountSelected.value = false
        curLiveText = priceLive
    }

    private fun getStringFromResource(resource: Int): String {
        return (getApplication() as Context).getString(resource)
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

    private fun setMeasureUnit() {
        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
    }

}


