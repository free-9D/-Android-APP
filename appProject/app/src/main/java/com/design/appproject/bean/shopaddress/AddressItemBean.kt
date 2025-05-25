package com.design.appproject.bean.shopaddress

data class AddressItemBean(
    var id:Long=0L,
    var address:String="",
    var name:String="",
    var phone:String="",
    var isdefault:String="",
    var userid:Long=0,
    var addtime:String?=null,
)