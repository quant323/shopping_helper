package com.zedevstuds.price_equalizer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.BuildConfig
import com.zedevstuds.price_equalizer.databinding.ActivityMainBinding
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.adapter.ProductAdapter
import com.zedevstuds.price_equalizer.models.Product
import com.zedevstuds.price_equalizer.utils.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private var price: String? = null
    private var amount: String? = null
    private var measureUnit: Int? = null
    private var currency: Int? = null

    // для хранения предыдущего размера листа продуктов
    // (необходимо для возможности отмены очистки списка)
    private var prevProductListSize = 0

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var mObserverList: Observer<List<Product>>
    private lateinit var mObserverPrice: Observer<String>
    private lateinit var mObserverAmount: Observer<String>
    private lateinit var mObserverMeasureUnit: Observer<Int>
    private lateinit var mObserverCurrency: Observer<Int>
    private lateinit var mObserveIsPriceSelected: Observer<Boolean>
    private lateinit var mObserveIsKeyboardVisible: Observer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        when (item.itemId) {
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
        val currencyArray = arrayOf(
            getString(R.string.d_ruble_txt),
            getString(R.string.d_dollar_txt), getString(R.string.d_euro_txt)
        )
        var selectedId = 0
        // Диалог выбора текущих денежных единиц
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
            .setMessage(getString(R.string.d_about_message) + BuildConfig.VERSION_NAME)
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

    // Инициализирует RecyclerView
    private fun initRecView() {
        adapter = ProductAdapter()
        recyclerView = binding.recyclerVew
        recyclerView.adapter = adapter
        // Поведение при свайпе элемента RecyclerView вправо
        val touchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            // Перемещение элемента
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            // Свайп элемента
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteItemFromProductList(position)
            }
        })
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mObserverList = Observer { productList ->
            adapter.setProductList(productList)
            if (productList.isNotEmpty()) {
                binding.mainHintText.visibility = View.INVISIBLE
                // Если размер нового листа больше предыдущего - значит произошло добавление элемента
                // и необходимо прокрутиться до последнего элемента списка
                if (productList.size > prevProductListSize)
                    recyclerView.smoothScrollToPosition(productList.lastIndex)
            } else binding.mainHintText.visibility = View.VISIBLE
            prevProductListSize = productList.size
        }
        mObserverPrice = Observer { price ->
            this.price = price
            setPrice()
        }
        mObserverAmount = Observer { amount ->
            this.amount = amount
            setAmount()
        }
        mObserverMeasureUnit = Observer { measureUnit ->
            this.measureUnit = measureUnit
            setAmount()
        }
        mObserverCurrency = Observer { currency ->
            this.currency = currency
            adapter.setCurrency(currency)
            setPrice()
        }
        mObserveIsPriceSelected = Observer { isAmountSelected ->
            if (isAmountSelected) {
                increaseAmountLook()
                decreasePriceLook()
            } else {
                decreaseAmountLook()
                increasePriceLook()
            }
            binding.btnMeasureUnit.isEnabled = isAmountSelected
        }
        mObserveIsKeyboardVisible = Observer { isVisible ->
            hideShowKeyboard(isVisible)
            if (isVisible) {
                binding.btnHideKeyboard.setImageResource(R.drawable.ic_keyboard_hide)
            } else binding.btnHideKeyboard.setImageResource(R.drawable.ic_keyboard)
        }

        viewModel.apply {
            productList.observe(this@MainActivity, mObserverList)
            priceLive.observe(this@MainActivity, mObserverPrice)
            amountLive.observe(this@MainActivity, mObserverAmount)
            curMeasureUnit.observe(this@MainActivity, mObserverMeasureUnit)
            currency.observe(this@MainActivity, mObserverCurrency)
            isAmountSelected.observe(this@MainActivity, mObserveIsPriceSelected)
            isKeyboardVisible.observe(this@MainActivity, mObserveIsKeyboardVisible)
        }
    }

    private fun setOnClicks() {
        binding.apply {
            btn0.setOnClickListener(this@MainActivity)
            btn1.setOnClickListener(this@MainActivity)
            btn2.setOnClickListener(this@MainActivity)
            btn3.setOnClickListener(this@MainActivity)
            btn4.setOnClickListener(this@MainActivity)
            btn5.setOnClickListener(this@MainActivity)
            btn6.setOnClickListener(this@MainActivity)
            btn7.setOnClickListener(this@MainActivity)
            btn8.setOnClickListener(this@MainActivity)
            btn9.setOnClickListener(this@MainActivity)
            btnDel.setOnClickListener(this@MainActivity)
            btnDot.setOnClickListener(this@MainActivity)
            btnOk.setOnClickListener { confirmActions() }
            btnMeasureUnit.setOnClickListener { viewModel.onChangeUnitClicked() }
            btnClean.setOnClickListener {
                viewModel.onClean(binding.coordinatorLayout)
            }
            txtPrice.setOnClickListener { viewModel.onPriceClicked() }
            txtAmount.setOnClickListener { viewModel.onAmountClicked() }
            btnHideKeyboard.setOnClickListener { viewModel.onHideKeyboardClicked() }
        }
    }

    private fun increasePriceLook() {
        increaseText(binding.txtPrice)
        binding.txtPrice.background = ContextCompat.getDrawable(this, R.drawable.selected_txt_back)
    }

    private fun decreasePriceLook() {
        decreaseText(binding.txtPrice)
        binding.txtPrice.background =
            ContextCompat.getDrawable(this, R.drawable.unselected_txt_back)
    }

    private fun increaseAmountLook() {
        increaseText(binding.txtAmount)
        binding.txtAmount.background =
            ContextCompat.getDrawable(this, R.drawable.selected_txt_back)
    }

    private fun decreaseAmountLook() {
        decreaseText(binding.txtAmount)
        binding.txtAmount.background =
            ContextCompat.getDrawable(this, R.drawable.unselected_txt_back)
    }

    // Общий слушатель нажатия на кнопки
    override fun onClick(v: View?) {
        var keyPressed = ""
        when (v?.id) {
            R.id.btn_0 -> keyPressed = keyArray[0]
            R.id.btn_1 -> keyPressed = keyArray[1]
            R.id.btn_2 -> keyPressed = keyArray[2]
            R.id.btn_3 -> keyPressed = keyArray[3]
            R.id.btn_4 -> keyPressed = keyArray[4]
            R.id.btn_5 -> keyPressed = keyArray[5]
            R.id.btn_6 -> keyPressed = keyArray[6]
            R.id.btn_7 -> keyPressed = keyArray[7]
            R.id.btn_8 -> keyPressed = keyArray[8]
            R.id.btn_9 -> keyPressed = keyArray[9]
            R.id.btn_dot -> keyPressed = DOT
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

    private fun setPrice() {
        if (price != null && currency != null)
            binding.txtPrice.text = "$price ${getString(currency!!)}"
    }

    private fun setAmount() {
        if (amount != null && measureUnit != null)
            binding.txtAmount.text = "$amount ${getString(measureUnit!!)}"
    }

    private fun hideShowKeyboard(isVisible: Boolean) {
        val gone = if (isVisible) View.VISIBLE
            else View.GONE
        val invisible = if (isVisible) View.VISIBLE
            else View.INVISIBLE
        binding.apply {
            keyboardFirstRow.visibility = gone
            keyboardSecondRow.visibility = gone
            keyboardThirdRow.visibility = gone
            btnDot.visibility = invisible
            btn0.visibility = invisible
            btnDel.visibility = invisible
            txtPrice.visibility = gone
            txtAmount.visibility = gone
        }
    }

    //todo delete
    override fun onDestroy() {
        super.onDestroy()
        viewModel.apply {
            productList.removeObserver(mObserverList)
            priceLive.removeObserver(mObserverPrice)
            amountLive.removeObserver(mObserverAmount)
            curMeasureUnit.removeObserver(mObserverMeasureUnit)
            currency.removeObserver(mObserverCurrency)
            isAmountSelected.removeObserver(mObserveIsPriceSelected)
        }
    }

}

