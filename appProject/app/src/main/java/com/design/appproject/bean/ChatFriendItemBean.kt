package com.design.appproject.bean

/**
 * 好友表实体类
 */
data class ChatFriendItemBean(
    var id:Long?=null,
    var uid:Long=0,
    var fid:Long=0,
    var name:String="",
    var picture:String="",
    var role:String="",
    var tablename:String="",
    var alias:String="",
    var type:Int=0,
    var notreadnum:Int=0,
    var content:String="",
    var addtime:String?=null,
)