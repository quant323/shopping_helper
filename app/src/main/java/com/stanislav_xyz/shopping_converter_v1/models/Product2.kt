package com.stanislav_xyz.shopping_converter_v1.models

import com.stanislav_xyz.shopping_converter_v1.utils.NEUT
import java.math.BigDecimal

data class Product2(
    val curPrice: String = "",
    val curAmount: String = "",
    val curMeasureUnit: Int,
    val currency: Int,
    val pricePerOne: BigDecimal = BigDecimal(0),
    val amountPerOne: BigDecimal = BigDecimal(0),
    val price1: BigDecimal = BigDecimal(0),
    val price2: BigDecimal = BigDecimal(0),
    val price3: BigDecimal = BigDecimal(0),
    var difference1: BigDecimal = BigDecimal(0),
    var difference2: BigDecimal = BigDecimal(0),
    var difference3: BigDecimal = BigDecimal(0),
    var unit1: String = "",
    var unit2: String = "",
    var unit3: String = "",
    var status: String = NEUT

//    val measureUnit: Int,


//    var profit: BigDecimal = BigDecimal(0),

)