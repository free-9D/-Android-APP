package com.design.appproject.bean

/**
 * 商品信息实体类
 */
data class ShangpinxinxiItemBean(
    var id:Long?=null,
    var shangpinmingcheng:String="",
    var shangpinfenlei:String="",
    var shangpinpinpai:String="",
    var shangpinguige:String="",
    var shangpintupian:String="",
    var shangpinjianjie:String="",
    var shangpinxiangqing:String="",
    var price:Double=0.0,
    var shangjiazhanghao:String="",
    var shangjiaxingming:String="",
    var addtime:String?=null,
)