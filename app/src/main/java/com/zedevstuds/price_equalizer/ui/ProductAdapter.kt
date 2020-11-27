package com.zedevstuds.price_equalizer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.databinding.ProductItem2Binding
import com.zedevstuds.price_equalizer.models.Product2
import com.zedevstuds.price_equalizer.utils.*
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = emptyList<Product2>()

    private var currency: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product, position, currency)


//        val currency = APP_ACTIVITY.getString(product.currency)
//        val unit = APP_ACTIVITY.getString(product.curUnit)
//
//        holder.number.text = "#${position + 1}"
//        holder.curPrice.text = (position + 1).toString() + ") " + product.curPrice + currency + divider +  product.curAmount + unit
//        holder.pricePerOne.text = product.pricePerOne.toString() + currency + divider + product.unit1
//        holder.difPerOne.text = "+" + product.difference1 + currency
//        holder.pricePerHalf.text = product.price2.toString() + currency + divider + product.unit2
//        holder.difPerHalf.text = "+" + product.difference2 + currency
//        holder.pricePerOneTenth.text = product.price3.toString() + currency + divider + product.unit3
//        holder.difPerOneTenth.text = "+" + product.difference3 + currency
//
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

    class ProductViewHolder(private val binding: ProductItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        private val adapterHelper = AdapterHelper(itemView.context)
        fun bind(product: Product2, position: Int, currency: Int?) {
            // Первоначально инициализируем текущий продукт и денежные единицы
            adapterHelper.setProduct(product)
            adapterHelper.setCurrency(currency)
            // Устанавливаем значения во view объектах
            binding.itemCurPrice.text = adapterHelper.getCurPriceString(position + 1)
            binding.itemPricePerOne.text = adapterHelper.getPriceString(1000)
            binding.itemDifPerOne.text = adapterHelper.getDifString(1000)
            binding.itemPricePerHalf.text = adapterHelper.getPriceString(500)
            binding.itemDifPerHalf.text = adapterHelper.getDifString(500)
            binding.itemPricePerOneTenth.text = adapterHelper.getPriceString(100)
            binding.itemDifPerOneTenth.text = adapterHelper.getDifString(100)
            when(product.status) {
                PriceStatus.MIN -> binding.itemThumb.setImageResource(R.drawable.ic_thumb_up)
                PriceStatus.MAX -> binding.itemThumb.setImageResource(R.drawable.ic_thumb_down)
                PriceStatus.NEUT -> binding.itemThumb.setImageResource(0)
            }
        }
    }

}