package com.zedevstuds.price_equalizer.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.databinding.ProductItem4Binding
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = emptyList<Product2>()
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
    fun setProductList(list: List<Product2>) {
        productList = list
        notifyDataSetChanged()
    }

    // Устанавливаем денежную единицу
    fun setCurrency(currency: Int) {
        this.currency = currency
    }

    class ProductViewHolder(private val binding: ProductItem4Binding) : RecyclerView.ViewHolder(binding.root) {
        private val adapterHelper = AdapterHelper(itemView.context)

        fun bind(product: Product2, position: Int, currency: Int?, listSize: Int) {
            // Первоначально инициализируем текущий продукт и денежные единицы
            adapterHelper.setProduct(product)
            adapterHelper.setCurrency(currency)
            // Устанавливаем значения во view-объектах
            setTextViews(position)
            // Устанавливаем внешний вид элемента в зависимости от статуса продукта
            setStatus(product.status, listSize)
        }

        // Устанавливает значения в TextViews
        private fun setTextViews(position: Int) {
            binding.apply {
                itemCurPrice.text = adapterHelper.getCurPriceString(position)
                itemPricePerOne.text = adapterHelper.getPriceString(1000)
                itemDifPerOne.text = adapterHelper.getDifString(1000)
                itemPricePerHalf.text = adapterHelper.getPriceString(500)
                itemDifPerHalf.text = adapterHelper.getDifString(500)
                itemPricePerOneTenth.text = adapterHelper.getPriceString(100)
                itemDifPerOneTenth.text = adapterHelper.getDifString(100)
            }
        }

        // Устанавливает внешний вид элемента в зависимости от статуса продукта
        private fun setStatus(status: PriceStatus, listSize: Int) {
            when (status) {
                PriceStatus.MIN -> {
                    if (listSize > 1) {
                        binding.itemDifTitle.text = itemView.context.getString(R.string.item_title_best)
                        binding.highlightingView.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.min_price_background)
                    } else {
                        binding.itemDifTitle.text =
                            itemView.context.getString(R.string.item_title_neutral)
                        binding.highlightingView.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.neutral_price_background)
                    }
                }
                PriceStatus.MAX -> {
                    binding.itemDifTitle.text = itemView.context.getString(R.string.item_title_neutral)
                    if (listSize > 2) {
                        binding.highlightingView.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.max_price_background)
                    } else {
                        binding.highlightingView.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.neutral_price_background)
                    }
                }
                PriceStatus.NEUT -> {
                    binding.itemDifTitle.text = itemView.context.getString(R.string.item_title_neutral)
                    binding.highlightingView.background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.neutral_price_background)
                }
            }
        }
    }

}