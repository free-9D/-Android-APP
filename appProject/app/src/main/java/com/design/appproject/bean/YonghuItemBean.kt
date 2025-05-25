package com.design.appproject.bean

/**
 * 用户实体类
 */
data class YonghuItemBean(
    var id:Long?=null,
    var yonghuzhanghao:String="",
    var mima:String="",
    var yonghuxingming:String="",
    var touxiang:String="",
    var xingbie:String="",
    var lianxidianhua:String="",
    var pquestion:String="",
    var panswer:String="",
    var money:Double=0.0,
    var addtime:String?=null,
)