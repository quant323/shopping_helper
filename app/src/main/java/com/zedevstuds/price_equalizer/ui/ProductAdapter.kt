package com.zedevstuds.price_equalizer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.databinding.ProductItem2Binding
import com.zedevstuds.price_equalizer.databinding.ProductItem4Binding
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = emptyList<Product2>()

    private var currency: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductItem4Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product, position, currency)
//        when(product.status) {
//            MIN -> {
//  //              holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.min_price_background)
//                holder.thumb.setImageResource(R.drawable.ic_thumb_up)
//                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_best)
//            }
//            MAX -> {
//   //             holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.neutral_price_background)
//                holder.thumb.setImageResource(R.drawable.ic_thumb_down)
//                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_worst)
//            }
//            NEUT -> {
//  //              holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.neutral_price_background)
//                holder.thumb.setImageResource(0)
//                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_neutral)
//            }
//        }
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
        fun bind(product: Product2, position: Int, currency: Int?) {
            // Первоначально инициализируем текущий продукт и денежные единицы
            adapterHelper.setProduct(product)
            adapterHelper.setCurrency(currency)
            // Устанавливаем значения во view объектах
            binding.itemCurPrice.text = adapterHelper.getCurPriceString(position)
            binding.itemPricePerOne.text = adapterHelper.getPriceString(1000)
            binding.itemDifPerOne.text = adapterHelper.getDifString(1000)
            binding.itemPricePerHalf.text = adapterHelper.getPriceString(500)
            binding.itemDifPerHalf.text = adapterHelper.getDifString(500)
            binding.itemPricePerOneTenth.text = adapterHelper.getPriceString(100)
            binding.itemDifPerOneTenth.text = adapterHelper.getDifString(100)

//            binding.itemThumb.visibility = View.INVISIBLE

            when(product.status) {
                PriceStatus.MIN -> {
                    binding.itemDifTitle.text = itemView.context.getString(R.string.item_title_best)
                    binding.highlightingView.visibility = View.VISIBLE
//                    binding.itemDifTitle.background = ContextCompat.getDrawable(itemView.context, R.drawable.min_price_background)
                }
                PriceStatus.MAX -> {
                    binding.itemDifTitle.text = itemView.context.getString(R.string.item_title_neutral)
                    binding.highlightingView.visibility = View.INVISIBLE
//                    binding.itemDifTitle.background = ContextCompat.getDrawable(itemView.context, R.drawable.neutral_price_background)
                }
                PriceStatus.NEUT -> {
                    binding.itemDifTitle.text = itemView.context.getString(R.string.item_title_neutral)
                    binding.highlightingView.visibility = View.INVISIBLE
//                    binding.itemDifTitle.background = ContextCompat.getDrawable(itemView.context, R.drawable.neutral_price_background)
                }
            }
//            when(product.status) {
//                PriceStatus.MIN -> binding.itemThumb.setImageResource(R.drawable.ic_thumb_up)
//                PriceStatus.MAX -> binding.itemThumb.setImageResource(R.drawable.ic_thumb_down)
//                PriceStatus.NEUT -> binding.itemThumb.setImageResource(0)
//            }
        }
    }

}