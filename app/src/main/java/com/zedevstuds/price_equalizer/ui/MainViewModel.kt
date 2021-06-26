package com.zedevstuds.price_equalizer.ui

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.models.Product
import com.zedevstuds.price_equalizer.utils.*
import java.util.*
import kotlin.collections.ArrayList


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val MAX_NUMBER_LENGTH = 6     // max длина вводимого числа
    val MAX_DECIMAL_LENGTH = 2    // max длина десятичной части числа

    private var measureUnitPosition = 0
    private val deviceLang = Locale.getDefault().language   // текущий язык устройства

    private var curLiveText = MutableLiveData(keyArray[0])

    private val productManager = ProductManager()

    val productList = MutableLiveData<ArrayList<Product>>()
    val curMeasureUnit = MutableLiveData<Int>()
    val currency = MutableLiveData<Int>()
    val isAmountSelected = MutableLiveData(true)
    val priceLive = MutableLiveData(keyArray[0])
    val amountLive = MutableLiveData(keyArray[0])
    val isKeyboardVisible = MutableLiveData(true)


    init {
        curLiveText = amountLive
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

    fun onHideKeyboardClicked() {
        isKeyboardVisible.value = isKeyboardVisible.value?.not()
    }

    fun onKeyPressed(keyPressed: String) {
        if (curLiveText.value.isNullOrBlank()) return
        // Проверяем длину текущего числа
        if (isLengthTooBig(curLiveText.value.toString())) return
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
                productList.value = oldList as ArrayList<Product>
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
        initCurrency()
    }

    fun onStop() {
        AppPreference.saveMeasureUnit(measureUnitPosition)
        currency.value?.let {
            AppPreference.saveCurrency(it)
        }
    }

    fun setCurrency(selectedItemId: Int) {
        currency.value = currencyArray[selectedItemId]
    }

    private fun addNewProduct() {
//        if (amountLive.value == keyArray[0]) {
//            showToast(getApplication(), getStringFromResource(R.string.toast_no_products_quantity))
//            return
//        }
        try {
            productList.value = productManager.addNewProduct(
                productList.value!!,
                priceLive.value!!.toBigDecimal(),
                amountLive.value!!.toBigDecimal(),
                curMeasureUnit.value!!
            )
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

    // первоначально устанавливает валюту
    private fun initCurrency() {
        val savedCurrency = AppPreference.getCurrency()
        currency.value =
                // если в SharedPref нет сохраненных значений
            if (savedCurrency == AppPreference.NO_VALUE) {
                // если на устройстве установлен русский язык - устанавливаем в качестве валюты рубли
                // если нет - доллары
                if (deviceLang == LANG_RUS) {
                    R.string.ruble_sign
                } else R.string.dollar_sign
            } else savedCurrency
    }

    // Проверяет, возможно ли добавить число к текущему введенному или оно уже максимальной длины
    private fun isLengthTooBig(text: String): Boolean {
        // Проверяем длину числа без учета точки
        val digWithNoDot = text.replace(DOT, "")
        if (digWithNoDot.length >= MAX_NUMBER_LENGTH) return true
        if (text.contains(DOT)) {
            // Если в числе имеется точка - проверяем длину ее десятичной части
            val splitTextList = text.split(DOT)
            if (splitTextList[1].length >= MAX_DECIMAL_LENGTH) return true
        }
        return false
    }

}


