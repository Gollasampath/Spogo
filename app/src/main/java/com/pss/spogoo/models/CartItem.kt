package com.pss.spogoo.models

data class CartItem(
    var products_id: Int,
    var productname: String,
    var productimage: String,
    var mainprice: String,
    var offerprice: String,
    var size: String,
    var discount: String,
    var item_qty: String, var quantity: Int = 0
)