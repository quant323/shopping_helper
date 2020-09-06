package com.stanislav_xyz.shopping_converter_v1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.stanislav_xyz.shopping_converter_v1.R
import com.stanislav_xyz.shopping_converter_v1.databinding.ActivityMainBinding
import com.stanislav_xyz.shopping_converter_v1.models.Product
import com.stanislav_xyz.shopping_converter_v1.utils.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    private var curTextViewId: Int = R.id.txt_price

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var mObserverList: Observer<List<Product>>
    private lateinit var mObserverPrice: Observer<String>
    private lateinit var mObserverAmount: Observer<String>
    private lateinit var mObserverMeasureUnit: Observer<Int>
    private lateinit var mObserverCurrency: Observer<Int>
    private lateinit var mObserveIsPriceSelected: Observer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initialSettings()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_currency -> {
                selectCurrency()
                return true
            }
            R.id.action_about -> {
                callAbout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun selectCurrency() {
        val currencyArray = arrayOf(getString(R.string.d_ruble_txt),
            getString(R.string.d_dollar_txt), getString(R.string.d_euro_txt))
        var selectedId = 0
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.d_currency_title))
            .setSingleChoiceItems(currencyArray, viewModel.getCurrencyPos()) { dialog, id ->
                selectedId = id
            }
            .setPositiveButton(getString(R.string.d_ok_button)) { dialog, id ->
                viewModel.setCurrency(selectedId)
            }
            .setNegativeButton(getString(R.string.d_cancel_button), null)
            .show()
    }

    private fun callAbout() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.d_about_title))
            .setMessage(getString(R.string.d_about_message))
            .setPositiveButton(R.string.d_ok_button, null)
            .show()
    }

    private fun initialSettings() {
        APP_ACTIVITY = this
        AppPreference.initPreferences(this)
        initRecView()
        initViewModel()
        setOnClicks()
    }

    private fun initRecView() {
        adapter = ProductAdapter()
        recyclerView = mBinding.recyclerVew
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mObserverList = Observer { productList ->
            adapter.setProductList(productList)
            if(productList.isNotEmpty())
                recyclerView.smoothScrollToPosition(productList.lastIndex)
        }
        mObserverPrice = Observer { price ->
            mBinding.txtPrice.text = price
        }
        mObserverAmount = Observer { amount ->
            mBinding.txtAmount.text = amount
        }
        mObserverMeasureUnit = Observer { measureUnit ->
            mBinding.txtMeasureUnit.text = getString(measureUnit)
        }
        mObserverCurrency = Observer { currency ->
            mBinding.txtCurrency.text = getString(currency)
        }
        mObserveIsPriceSelected = Observer { isStageOne ->
            if (isStageOne) {
                increasePriceLook()
                decreaseAmountLook()
            } else {
                decreasePriceLook()
                increaseAmountLook()
            }
            mBinding.btnMeasureUnit.isEnabled = !isStageOne
        }
        viewModel.productList.observe(this, mObserverList)
        viewModel.priceString.observe(this, mObserverPrice)
        viewModel.amountString.observe(this, mObserverAmount)
        viewModel.curMeasureUnit.observe(this, mObserverMeasureUnit)
        viewModel.currency.observe(this, mObserverCurrency)
        viewModel.isPriceSelected.observe(this, mObserveIsPriceSelected)
    }

    private fun setOnClicks() {
        mBinding.btn0.setOnClickListener(this)
        mBinding.btn1.setOnClickListener(this)
        mBinding.btn2.setOnClickListener(this)
        mBinding.btn3.setOnClickListener(this)
        mBinding.btn4.setOnClickListener(this)
        mBinding.btn5.setOnClickListener(this)
        mBinding.btn6.setOnClickListener(this)
        mBinding.btn7.setOnClickListener(this)
        mBinding.btn8.setOnClickListener(this)
        mBinding.btn9.setOnClickListener(this)
        mBinding.btnDel.setOnClickListener(this)
        mBinding.btnDot.setOnClickListener(this)

        mBinding.btnOk.setOnClickListener {
            confirmActions()
        }
        mBinding.btnMeasureUnit.setOnClickListener {
            viewModel.onChangeUnitClicked()
        }
        mBinding.btnClean.setOnClickListener {
            viewModel.onClean()
        }
        mBinding.txtPrice.setOnClickListener {
            curTextViewId = R.id.txt_price
            viewModel.onPriceClicked()
        }
        mBinding.txtAmount.setOnClickListener {
            curTextViewId = R.id.txt_amount
            viewModel.onAmountClicked()
        }
    }

    private fun increasePriceLook() {
        increaseText(mBinding.txtPrice)
        increaseText(mBinding.txtCurrency)
    }

    private fun decreasePriceLook() {
        decreaseText(mBinding.txtPrice)
        decreaseText(mBinding.txtCurrency)
    }

    private fun increaseAmountLook() {
        increaseText(mBinding.txtAmount)
        increaseText(mBinding.txtMeasureUnit)
    }

    private fun decreaseAmountLook() {
        decreaseText(mBinding.txtAmount)
        decreaseText(mBinding.txtMeasureUnit)
    }

    override fun onClick(v: View?) {
        var keyPressed = ""
        when (v?.id) {
            R.id.btn_0 -> {
                keyPressed = "0"
            }
            R.id.btn_1 -> {
                keyPressed = "1"
            }
            R.id.btn_2 -> {
                keyPressed = "2"
            }
            R.id.btn_3 -> {
                keyPressed = "3"
            }
            R.id.btn_4 -> {
                keyPressed = "4"
            }
            R.id.btn_5 -> {
                keyPressed = "5"
            }
            R.id.btn_6 -> {
                keyPressed = "6"
            }
            R.id.btn_7 -> {
                keyPressed = "7"
            }
            R.id.btn_8 -> {
                keyPressed = "8"
            }
            R.id.btn_9 -> {
                keyPressed = "9"
            }
            R.id.btn_dot -> {
                keyPressed = "."
            }
            R.id.btn_del -> {
                viewModel.onDel()
                return
            }
        }
        viewModel.onKeyPressed(keyPressed)
    }

    private fun confirmActions() {
        viewModel.onOkClicked()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.productList.removeObserver(mObserverList)
        viewModel.priceString.removeObserver(mObserverPrice)
        viewModel.amountString.removeObserver(mObserverAmount)
        viewModel.curMeasureUnit.removeObserver(mObserverMeasureUnit)
        viewModel.currency.removeObserver(mObserverCurrency)
        viewModel.isPriceSelected.removeObserver(mObserveIsPriceSelected)
    }

}
