package com.zedevstuds.price_equalizer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.databinding.ProductItem4Binding
import com.zedevstuds.price_equalizer.models.Product
import com.zedevstuds.price_equalizer.utils.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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
            // Инициализируем текущий продукт и денежные единицы  в AdapterCalcHelper
            calcHelper.setProduct(product)
            calcHelper.setCurrency(currency)
            // Устанавливаем значения во view-объектах
            setTextViews(product, position, listSize)
        }

        // Устанавливает значения во View элементы
        private fun setTextViews(product: Product, position: Int, listSize: Int) {
            binding.apply {
                itemCurPrice.text = calcHelper.getCurPriceString(position)
                itemPricePerOne.text = calcHelper.getPriceString(1000)
                itemDifPerOne.text = calcHelper.getDifString(1000)
                itemPricePerHalf.text = calcHelper.getPriceString(500)
                itemDifPerHalf.text = calcHelper.getDifString(500)
                itemPricePerOneTenth.text = calcHelper.getPriceString(100)
                itemDifPerOneTenth.text = calcHelper.getDifString(100)
                // Текст статуса
                itemDifTitle.text = viewHelper.getStatusText(product.status, listSize)
                // Фон текста статуса
                highlightingView.background = viewHelper.getStatusBackground(product.status, listSize)
            }
        }
    }

}