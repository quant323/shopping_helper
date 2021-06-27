package com.zedevstuds.price_equalizer.models

import com.zedevstuds.price_equalizer.utils.PriceStatus
import java.math.BigDecimal

data class Product2(
    val curPrice: BigDecimal,
    val curAmount: BigDecimal,
    val priceForSmallestValue: BigDecimal,   // цена за грамм, миллилитр, миллиметр, 0,001 шт.
    val unit: Int,
    var difForSmallestValue: BigDecimal = BigDecimal(0),     // разница за грамм, миллилитр, миллиметр, 0,001 шт.
    var amountList: ArrayList<String> = ArrayList(),
    var priceList: ArrayList<String> = ArrayList(),
    var difList: ArrayList<String> = ArrayList(),
    var status: PriceStatus = PriceStatus.NEUT,
    var title: String = ""
)