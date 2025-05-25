package com.design.appproject.bean

/**
 * 消息表实体类
 */
data class ChatMessageItemBean(
    var id:Long?=null,
    var uid:Long=0,
    var fid:Long=0,
    var content:String="",
    var format:Int=0,
    var isRead:Int=0,
    var addtime:String?=null,
)