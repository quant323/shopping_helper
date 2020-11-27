package com.zedevstuds.price_equalizer.models

import com.zedevstuds.price_equalizer.utils.PriceStatus
import java.math.BigDecimal

data class Product2(
    val curPrice: BigDecimal,
    val curAmount: BigDecimal,
    val curUnit: Int,
    val priceForGram: BigDecimal,   // цена за грамм, миллилитр, миллиметр, 0,001 шт.
    var difInGrams: BigDecimal = BigDecimal(0),
    var status: PriceStatus = PriceStatus.NEUT
)