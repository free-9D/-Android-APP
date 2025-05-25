package com.design.appproject.bean

/**
 * 短视频评论表实体类
 */
data class DiscussduanshipinItemBean(
    var id:Long?=null,
    var refid:Long=0,
    var userid:Long=0,
    var avatarurl:String="",
    var nickname:String="",
    var content:String="",
    var reply:String="",
    var addtime:String?=null,
)