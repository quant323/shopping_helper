package com.zedevstuds.price_equalizer.ui

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var measureUnitPosition = 0
    private var currencyPosition = 0

    private var curLiveText = MutableLiveData(keyArray[0])

    private val productManager = ProductManager()

    var productList = MutableLiveData<ArrayList<Product2>>()
    var curMeasureUnit = MutableLiveData<Int>()
    var currency = MutableLiveData<Int>()
    var isAmountSelected = MutableLiveData(false)
    var priceLive = MutableLiveData(keyArray[0])
    var amountLive = MutableLiveData(keyArray[0])

    init {
        curLiveText = priceLive
        productList.value = ArrayList()
    }

    // Удаляет элемент из списка продуктов по номеру его позиции в листе
    fun deleteItemFromProductList(position: Int) {
        productList.value = productManager.removeProduct(productList.value!!, position)
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
        if (curLiveText.value.isNullOrBlank()) return
        // Проверяем длину текущего числа
        if (isLengthTooBig(curLiveText.value.toString(), MAX_NUMBER_LENGTH, MAX_DECIMAL_LENGTH)) return
        if (keyPressed == DOT) {
            // Если в числе уже соодержится точка - выходим из метода
            if (!curLiveText.value?.contains(DOT)!!)
                curLiveText.value = curLiveText.value.plus(keyPressed)
        } else {
            // Если текущее значение 0 - заменяем его на введенное, если другое число - добавляем введенное значение к нему
            if (curLiveText.value == keyArray[0])
                curLiveText.value = keyPressed
            else curLiveText.value = curLiveText.value.plus(keyPressed)
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
        // Для возможности отмены очистки списка сохраняем элементы списка в отдельный лист
        val oldList = productList.value?.map { it.copy() }
        // При нажатии на кнопку "Отмена" SnackBar'a удаленный список восстанавливается
        Snackbar.make(view, R.string.message_clean, Snackbar.LENGTH_SHORT)
            .setAction(R.string.snackbar_cancel_btn) {
                productList.value = oldList as ArrayList<Product2>
            }.show()
        resetState()
        productList.value?.clear()
        productList.value = productList.value
    }

    fun onDel() {
        curLiveText.value = curLiveText.value?.dropLast(1)
        if (curLiveText.value?.isEmpty()!!) curLiveText.value = keyArray[0]
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
            productList.value = productManager.addNewProduct(productList.value!!,
                priceLive.value!!.toBigDecimal(), amountLive.value!!.toBigDecimal(), curMeasureUnit.value!!)
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
        priceLive.value = keyArray[0]
        amountLive.value = keyArray[0]
        isAmountSelected.value = false
        curLiveText = priceLive
    }

    private fun getStringFromResource(resource: Int): String {
        return (getApplication() as Context).getString(resource)
    }

    private fun setMeasureUnit() {
        curMeasureUnit.value = measureUnitArray[measureUnitPosition]
    }

}


