package com.design.appproject.bean

/**
 * 短视频实体类
 */
data class DuanshipinItemBean(
    var id:Long?=null,
    var fengmian:String="",
    var biaoti:String="",
    var shipin:String="",
    var fabushijian:String="",
    var jianjie:String="",
    var shangjiazhanghao:String="",
    var shangjiaxingming:String="",
    var discussNumber:Int=0,
    var thumbsupNumber:Int=0,
    var crazilyNumber:Int=0,
    var addtime:String?=null,
)