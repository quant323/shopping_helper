package com.stanislav_xyz.shopping_converter_v1.models

import com.stanislav_xyz.shopping_converter_v1.utils.NEUT
import java.math.BigDecimal

data class Product(
    val curPrice: Double? = 0.0,
    val pricePerOne: BigDecimal = BigDecimal(0),
    val curAmount: Double? = 0.0,
    val amountPerOne: Double? = 0.0,
    val curMeasureUnit: Int,
    val measureUnit: Int,
    var difference: BigDecimal = BigDecimal(0),
    var profit: BigDecimal = BigDecimal(0),
    var status: String = NEUT
)