package com.zedevstuds.price_equalizer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.databinding.ProductItem5Binding
import com.zedevstuds.price_equalizer.models.Product2
import java.lang.StringBuilder
import java.math.BigDecimal

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    companion object {
        val quantityArray = arrayOf(1000, 500, 100)         // массив кол-ва обычных единиц (1кг, 500 и 100г)
        val pcsQuantityArray = arrayOf(1000, 10000, 100000) // массив кол-ва для штук (1, 10, 100 шт.)
    }

    private var productList = emptyList<Product2>()
    private var currency: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItem5Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    // Устанавливаем лист продуктов
    fun setProductList(list: List<Product2>) {
        productList = list
        notifyDataSetChanged()
    }

    // Устанавливаем денежную единицу
    fun setCurrency(currency: Int) {
        this.currency = currency
    }


    inner class ProductViewHolder(private val binding: ProductItem5Binding) : RecyclerView.ViewHolder(binding.root) {
        private val DIVIDER = "-"
        private val NEW_LINE = "\n"
        private val context = itemView.context
        private val viewHelper = AdapterViewHelper(context)

        fun bind(position: Int) {
            val product = productList[position]
            // Устанавливаем значения во view-объектах
            setTextViews(product, position)
        }

        // Устанавливает значения во View элементы
        @SuppressLint("SetTextI18n")
        private fun setTextViews(product: Product2, position: Int) {
            binding.apply {
                itemSerNumber.text = (position + 1).toString()
                itemAmount.text = getAmountString(product)
                itemPrice.text = getPriceString(product)
                itemDif.text = getDifString(product)
            }
        }

        private fun getAmountString(product: Product2): String {
            val curAmountWithUnit = "${product.curAmount} ${context.getString(product.unit)}"
            val curAmountString = convertListToText(listOf(context.getString(R.string.amount_pattern, curAmountWithUnit)))
            return curAmountString + convertListToText(product.amountList.map {
                context.getString(R.string.amount_pattern, it)
            })
        }

        private fun getPriceString(product: Product2): String {
            val curPrice = convertListToText(listOf(context.getString(R.string.price_pattern,
                product.curPrice, context.getString(currency!!))))
            return curPrice + convertListToText(product.priceList.map {
                context.getString(R.string.price_pattern, it, context.getString(currency!!))
            })
        }

        private fun getDifString(product: Product2): String {
            return NEW_LINE + convertListToText(product.difList.map {
                context.getString(R.string.dif_pattern, it)
            })
        }

//        private fun formatCurAmount(product: Product2): String {
//            val amountWithUnit = product.curAmount.toString() + context.getString(product.unit)
//            return convertListToText(listOf(context.getString(R.string.amount_pattern, amountWithUnit)))
//        }
//
//        private fun formatCurPrice(product: Product2): String {
//            return convertListToText(listOf(context.getString(R.string.price_pattern,
//                product.curPrice, context.getString(currency!!))))
//        }

        private fun convertListToText(list: List<String>): String {
            val builder = StringBuilder()
            list.forEach {
                builder.append(it)
                builder.append(NEW_LINE)
            }
            return builder.toString()
        }

    }











//    companion object {
//        val quantityArray = arrayOf(1000, 500, 100)         // массив кол-ва обычных единиц (1кг, 500 и 100г)
//        val pcsQuantityArray = arrayOf(1000, 10000, 100000) // массив кол-ва для штук (1, 10, 100 шт.)
//    }
//
//    private var productList = emptyList<Product>()
//    private var currency: Int? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        return ProductViewHolder(
//            ProductItem4Binding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = productList[position]
//        holder.bind(product, position, currency, productList.size)
//    }
//
//    override fun getItemCount(): Int {
//        return productList.size
//    }
//
//    // Устанавливаем лист продуктов
//    fun setProductList(list: List<Product>) {
//        productList = list
//        notifyDataSetChanged()
//    }
//
//    // Устанавливаем денежную единицу
//    fun setCurrency(currency: Int) {
//        this.currency = currency
//    }
//
//
//    class ProductViewHolder(private val binding: ProductItem4Binding) : RecyclerView.ViewHolder(binding.root) {
//        private val context = itemView.context
//        private val calcHelper = AdapterCalcHelper(context)
//        private val viewHelper = AdapterViewHelper(context)
//        fun bind(product: Product, position: Int, currency: Int?, listSize: Int) {
//            var quantityArray = ProductAdapter.quantityArray    // массив кол-ва товара для расчета
//            // Если ед. измерения шт. - меняем массив
//            if (product.curUnit == R.string.piece) {
//                quantityArray = ProductAdapter.pcsQuantityArray
//            }
//            // Инициализируем денежные единицы  в AdapterCalcHelper
//            calcHelper.setCurrency(currency)
//            // Устанавливаем значения во view-объектах
//            setTextViews(product, position, listSize, quantityArray)
//        }
//
//        // Устанавливает значения во View элементы
//        private fun setTextViews(product: Product, position: Int, listSize: Int, quantityArray: Array<Int>) {
//            binding.apply {
//                itemCurPrice.text = calcHelper.getCurPriceString(product, position)
//                itemPricePerOne.text = calcHelper.getCalcPriceString(product, quantityArray[0])
//                itemDifPerOne.text = calcHelper.getDifString(product, quantityArray[0])
//                itemPricePerHalf.text = calcHelper.getCalcPriceString(product, quantityArray[1])
//                itemDifPerHalf.text = calcHelper.getDifString(product, quantityArray[1])
//                itemPricePerOneTenth.text = calcHelper.getCalcPriceString(product, quantityArray[2])
//                itemDifPerOneTenth.text = calcHelper.getDifString(product,quantityArray[2])
//                // Текст статуса
//                itemDifTitle.text = viewHelper.getStatusText(product.status, listSize)
//                // Фон текста статуса
//                highlightingView.background = viewHelper.getStatusBackground(product.status, listSize)
//            }
//        }
//    }



}