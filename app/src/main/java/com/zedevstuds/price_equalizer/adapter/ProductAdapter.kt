package com.zedevstuds.price_equalizer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.databinding.ProductItem4Binding
import com.zedevstuds.price_equalizer.models.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    companion object {
        val quantityArray = arrayOf(1000, 500, 100)         // массив кол-ва обычных единиц (1кг, 500 и 100г)
        val pcsQuantityArray = arrayOf(1000, 10000, 100000) // массив кол-ва для штук (1, 10, 100 шт.)
    }

    private var productList = emptyList<Product>()
    private var currency: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItem4Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product, position, currency, productList.size)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    // Устанавливаем лист продуктов
    fun setProductList(list: List<Product>) {
        productList = list
        notifyDataSetChanged()
    }

    // Устанавливаем денежную единицу
    fun setCurrency(currency: Int) {
        this.currency = currency
    }


    class ProductViewHolder(private val binding: ProductItem4Binding) : RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context
        private val calcHelper = AdapterCalcHelper(context)
        private val viewHelper = AdapterViewHelper(context)
        fun bind(product: Product, position: Int, currency: Int?, listSize: Int) {
            var quantityArray = ProductAdapter.quantityArray    // массив кол-ва товара для расчета
            // Если ед. измерения шт. - меняем массив
            if (product.curUnit == R.string.piece)
                quantityArray = ProductAdapter.pcsQuantityArray
            // Инициализируем денежные единицы  в AdapterCalcHelper
            calcHelper.setCurrency(currency)
            // Устанавливаем значения во view-объектах
            setTextViews(product, position, listSize, quantityArray)
        }

        // Устанавливает значения во View элементы
        private fun setTextViews(product: Product, position: Int, listSize: Int, quantityArray: Array<Int>) {
            binding.apply {
                itemCurPrice.text = calcHelper.getCurPriceString(product, position)
                itemPricePerOne.text = calcHelper.getCalcPriceString(product, quantityArray[0])
                itemDifPerOne.text = calcHelper.getDifString(product, quantityArray[0])
                itemPricePerHalf.text = calcHelper.getCalcPriceString(product, quantityArray[1])
                itemDifPerHalf.text = calcHelper.getDifString(product, quantityArray[1])
                itemPricePerOneTenth.text = calcHelper.getCalcPriceString(product, quantityArray[2])
                itemDifPerOneTenth.text = calcHelper.getDifString(product,quantityArray[2])
                // Текст статуса
                itemDifTitle.text = viewHelper.getStatusText(product.status, listSize)
                // Фон текста статуса
                highlightingView.background = viewHelper.getStatusBackground(product.status, listSize)
            }
        }
    }

}