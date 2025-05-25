package com.design.appproject.bean

/**
 * 我的收藏实体类
 */
data class StoreupItemBean(
    var id:Long?=null,
    var refid:Long=0,
    var tablename:String="",
    var name:String="",
    var picture:String="",
    var type:String="1",
    var inteltype:String="",
    var remark:String="",
    var userid:Long=0,
    var addtime:String?=null,
)