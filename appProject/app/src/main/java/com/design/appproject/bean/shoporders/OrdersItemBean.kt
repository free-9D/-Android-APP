package com.design.appproject.bean.shoporders

data class OrdersItemBean(
    var id:Long=0L,
    var orderid:String="",
    var tablename:String="",
    var goodid:Long=0,
    var goodname:String="",
    var picture:String="",
    var buynumber:Int=0,
    var price:Double=0.0,
    var discountprice:Double=0.0,
    var total:Double=0.0,
    var discounttotal:Double=0.0,
    var type:String="",
    var status:String="",
    var address:String="",
    var tel:String="",
    var consignee:String="",
    var remark:String="",
    var logistics:String="",
    var role:String="",
    var userid:Long=0,
    var shangjiazhanghao:String="",
    var addtime:String?=null,
    var selectedSeat:String = ""/*已被预定座位*/
)