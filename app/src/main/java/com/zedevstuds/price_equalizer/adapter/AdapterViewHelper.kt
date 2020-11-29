package com.zedevstuds.price_equalizer.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.utils.PriceStatus

class AdapterViewHelper(private val context: Context) {

    // Возвращает текст статуса
    fun getStatusText(status: PriceStatus, listSize: Int): String {
        return when (status) {
            PriceStatus.MIN -> {
                if (listSize > 1) context.getString(R.string.item_title_best)
                else context.getString(R.string.item_title_neutral)
            }
            PriceStatus.MAX -> context.getString(R.string.item_title_neutral)
            PriceStatus.NEUT -> context.getString(R.string.item_title_neutral)
        }
    }

    // Возвращает фон статуса как Drawable ресурс
    fun getStatusBackground(status: PriceStatus, listSize: Int): Drawable? {
        return when (status) {
            PriceStatus.MIN -> {
                if (listSize > 1) getMinPriceBackground()
                else getNeutralPriceBackground()
            }
            PriceStatus.MAX -> {
                if (listSize > 2) getMaxPriceBackground()
                else getNeutralPriceBackground()
            }
            PriceStatus.NEUT -> getNeutralPriceBackground()
        }
    }

    private fun getMinPriceBackground(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.min_price_background)
    }

    private fun getMaxPriceBackground(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.max_price_background)
    }

    private fun getNeutralPriceBackground(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.neutral_price_background)
    }

}