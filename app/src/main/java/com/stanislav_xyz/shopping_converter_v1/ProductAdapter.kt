package com.stanislav_xyz.shopping_converter_v1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stanislav_xyz.shopping_converter_v1.models.Product
import com.stanislav_xyz.shopping_converter_v1.utils.*
import kotlinx.android.synthetic.main.product_item.view.*
import java.math.RoundingMode

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = emptyList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val curProduct = productList[position]
        val normalUnit = setNormalUnit(curProduct.curMeasureUnit)
        val smallUnit = setSmallUnit(curProduct.curMeasureUnit)

        holder.number.text = "#${position + 1}"
        holder.curPrice.text = curProduct.curPrice.toString() + RUBLE + "/" + curProduct.curAmount + holder.cardView.context.getString(curProduct.curMeasureUnit)
        holder.pricePerOne.text = curProduct.pricePerOne.toString() + RUBLE + "/1" + normalUnit
        holder.difPerOne.text = curProduct.difference.toString() + RUBLE
        holder.pricePerHalf.text = (curProduct.pricePerOne.divide(2.toBigDecimal())).setScale(2, RoundingMode.HALF_EVEN).toString() + RUBLE + "/0.5" + normalUnit
        holder.difPerHalf.text = (curProduct.difference.divide(2.toBigDecimal())).toString() + RUBLE
        holder.pricePerOneTenth.text = (curProduct.pricePerOne.divide(10.toBigDecimal())).setScale(2, RoundingMode.HALF_EVEN).toString() + RUBLE + "/100" + smallUnit
        holder.difPerOneTenth.text = (curProduct.difference.divide(10.toBigDecimal())).toString() + RUBLE

        when(curProduct.status) {
            MIN -> {
 //               holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.keyboard_background)
                holder.thumb.setImageResource(R.drawable.ic_thumb_up)
                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_best)
            }
            MAX -> {
  //              holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.keyboard_background)
                holder.thumb.setImageResource(R.drawable.ic_thumb_down)
                holder.difTitle.text = APP_ACTIVITY.getString(R.string.item_title_worst)
            }
            NEUT -> {
   //             holder.itemHolder.background = ContextCompat.getDrawable(holder.cardView.context, R.drawable.keyboard_background)
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
        showToast(APP_ACTIVITY, "list set!")
    }

    private fun setNormalUnit(measureUnit: Int): String {
        return if (measureUnit == R.string.gram || measureUnit == R.string.kilogram) APP_ACTIVITY.getString(R.string.kilogram)
        else APP_ACTIVITY.getString(R.string.liter)
    }

    private fun setSmallUnit(measureUnit: Int): String {
        return if (measureUnit == R.string.gram || measureUnit == R.string.kilogram) APP_ACTIVITY.getString(R.string.gram)
        else APP_ACTIVITY.getString(R.string.milliliter)
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