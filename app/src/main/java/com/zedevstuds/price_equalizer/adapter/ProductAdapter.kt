package com.zedevstuds.price_equalizer.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.R
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
        private val adapterHelper = AdapterCalcHelper(context)

        fun bind(product: Product, position: Int, currency: Int?, listSize: Int) {
            // Инициализируем текущий продукт и денежные единицы  в AdapterHelper
            adapterHelper.setProduct(product)
            adapterHelper.setCurrency(currency)
            // Устанавливаем значения во view-объектах
            setTextViews(product, position, listSize)
        }

        // Устанавливает значения во View элементы
        private fun setTextViews(product: Product, position: Int, listSize: Int) {
            binding.apply {
                itemCurPrice.text = adapterHelper.getCurPriceString(position)
                itemPricePerOne.text = adapterHelper.getPriceString(1000)
                itemDifPerOne.text = adapterHelper.getDifString(1000)
                itemPricePerHalf.text = adapterHelper.getPriceString(500)
                itemDifPerHalf.text = adapterHelper.getDifString(500)
                itemPricePerOneTenth.text = adapterHelper.getPriceString(100)
                itemDifPerOneTenth.text = adapterHelper.getDifString(100)
                // Текст статуса
                itemDifTitle.text = getStatusText(context, product.status, listSize)
                // Фон текста статуса
                highlightingView.background = getStatusBackground(context, product.status, listSize)
            }
        }

        // Возвращает текст статуса
        private fun getStatusText(context: Context, status: PriceStatus, listSize: Int): String {
            return when (status) {
                PriceStatus.MIN -> {
                    if (listSize > 1) context.getString(R.string.item_title_best)
                    else context.getString(R.string.item_title_neutral)
                }
                PriceStatus.MAX -> context.getString(R.string.item_title_neutral)
                PriceStatus.NEUT -> context.getString(R.string.item_title_neutral)
            }
        }

        // Фозвращает фон статуса как Drawable ресурс
        private fun getStatusBackground(context: Context, status: PriceStatus, listSize: Int): Drawable? {
            return when (status) {
                PriceStatus.MIN -> {
                    if (listSize > 1) getMinPriceBackground(context)
                    else getNeutralPriceBackground(context)
                }
                PriceStatus.MAX -> {
                    if (listSize > 2) getMaxPriceBackground(context)
                    else getNeutralPriceBackground(context)
                }
                PriceStatus.NEUT -> getNeutralPriceBackground(context)
            }
        }

        private fun getMinPriceBackground(context: Context): Drawable? {
            return ContextCompat.getDrawable(context, R.drawable.min_price_background)
        }

        private fun getMaxPriceBackground(context: Context): Drawable? {
            return ContextCompat.getDrawable(context, R.drawable.max_price_background)
        }

        private fun getNeutralPriceBackground(context: Context): Drawable? {
           return ContextCompat.getDrawable(context, R.drawable.neutral_price_background)
        }
    }

}