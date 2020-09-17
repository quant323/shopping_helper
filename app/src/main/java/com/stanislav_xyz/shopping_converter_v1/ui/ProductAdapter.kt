package com.stanislav_xyz.shopping_converter_v1.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stanislav_xyz.shopping_converter_v1.R
import com.stanislav_xyz.shopping_converter_v1.models.Product
import com.stanislav_xyz.shopping_converter_v1.utils.*
import kotlinx.android.synthetic.main.product_item.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val divider = "/"

    private var productList = emptyList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item2, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        val currency = APP_ACTIVITY.getString(product.currency)
        val unit = APP_ACTIVITY.getString(product.curUnit)

        holder.number.text = "#${position + 1}"
        holder.curPrice.text = (position + 1).toString() + ") " + product.curPrice + currency + divider +  product.curAmount + unit
        holder.pricePerOne.text = product.pricePerOne.toString() + currency + divider + product.unit1
        holder.difPerOne.text = "+" + product.difference1 + currency
        holder.pricePerHalf.text = product.price2.toString() + currency + divider + product.unit2
        holder.difPerHalf.text = "+" + product.difference2 + currency
        holder.pricePerOneTenth.text = product.price3.toString() + currency + divider + product.unit3
        holder.difPerOneTenth.text = "+" + product.difference3 + currency

        when(product.status) {
            MIN -> {
  //              holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.min_price_background)
                holder.thumb.setImageResource(R.drawable.ic_thumb_up)
                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_best)
            }
            MAX -> {
   //             holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.neutral_price_background)
                holder.thumb.setImageResource(R.drawable.ic_thumb_down)
                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_worst)
            }
            NEUT -> {
  //              holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.neutral_price_background)
                holder.thumb.setImageResource(0)
                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_neutral)
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProductList(list: List<Product>) {
        productList = list
        notifyDataSetChanged()
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.item_number
        val curPrice: TextView = view.item_cur_price
        val pricePerOne: TextView = view.item_price_per_one
        val pricePerHalf: TextView = view.item_price_per_half
        val pricePerOneTenth: TextView = view.item_price_per_one_tenth
        val difPerOne: TextView = view.item_dif_per_one
        val difPerHalf: TextView = view.item_dif_per_half
        val difPerOneTenth: TextView = view.item_dif_per_one_tenth
        val itemHolder: View = view.item_holder
        val cardView: View = view.card_view
        val thumb: ImageView = view.item_thumb
        val difTitle: TextView = view.item_dif_title
    }

}