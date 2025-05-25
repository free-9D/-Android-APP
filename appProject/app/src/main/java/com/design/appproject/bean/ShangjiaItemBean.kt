package com.design.appproject.bean

/**
 * 商家实体类
 */
data class ShangjiaItemBean(
    var id:Long?=null,
    var shangjiazhanghao:String="",
    var mima:String="",
    var shangjiaxingming:String="",
    var touxiang:String="",
    var xingbie:String="",
    var lianxidianhua:String="",
    var zizhileixing:String="",
    var zizhizhengming:String="",
    var sfsh:String="待审核",
    var shhf:String="",
    var pquestion:String="",
    var panswer:String="",
    var addtime:String?=null,
)