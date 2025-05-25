package com.design.appproject.bean.shopcart

data class CartItemBean(
    var id:Long=0L,
    var isSelected:Boolean= false,
    var tablename:String="",
    var goodid:Long=0,
    var goodname:String="",
    var picture:String="",
    var buynumber:Int=0,
    var price:Double=0.0,
    var discountprice:Double=0.0,
    var userid:Long=0,
    var shangjiazhanghao:String="",
    var addtime:String?=null,
)