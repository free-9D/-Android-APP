package com.design.appproject.utils

import android.graphics.Color
import com.design.appproject.base.CommonBean
import com.design.appproject.bean.roleMenusList
import com.union.union_basic.ext.isNotNullOrEmpty
import com.union.union_basic.logger.LoggerManager
import com.union.union_basic.utils.StorageUtil
import com.blankj.utilcode.util.ColorUtils
import java.text.SimpleDateFormat
import java.util.*
import com.design.appproject.R

object Utils {

    fun isLogin() = StorageUtil.decodeString(CommonBean.TOKEN_KEY, "").isNotNullOrEmpty()

    fun isVip() = StorageUtil.decodeBool(CommonBean.VIP_KEY, false)

    fun getUserId() = StorageUtil.decodeLong(CommonBean.USER_ID_KEY, 0)

    fun getAvatarUrl() = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY, "")
    fun getUserName() = StorageUtil.decodeString(CommonBean.USERNAME_KEY, "")

    /**
     * 是否有权限(前台权限),tableName：表名，key:权限名
     */
    fun isAuthFront(tableName:String,key:String):Boolean {
        roleMenusList.firstOrNull { it.tableName == CommonBean.tableName }?.let {
            it.frontMenu.forEach {
                it.child.firstOrNull{ it.tableName==tableName }?.let {
                    return it.buttons.contains(key)
                }
            }
        }
        return false
    }
    /**
     * 是否有权限(后台权限),tableName：表名，key:权限名
     */
    fun isAuthBack(tableName:String,key:String):Boolean {
        roleMenusList.firstOrNull { it.tableName == CommonBean.tableName }?.let {
            it.backMenu.forEach {
                it.child.firstOrNull{ it.tableName==tableName }?.let {
                    return it.buttons.contains(key)
                }
            }
        }
        return false
    }

    fun genTradeNo():String{
        return System.currentTimeMillis().toString()
    }

    fun string2int(str:String):Int{
        if (str.length==4){
            var strList = str.split("")
            strList = strList.subList(1,strList.size-1)
            val newColor = strList[0]+strList[1]+strList[1]+strList[2]+strList[2]+strList[3]+strList[3]
            return Color.parseColor(newColor)
        }
        return Color.parseColor(str)
    }
    fun getRandomColor(index:Int): Int {
        return when(index){
            0-> R.color.common_red
            1-> R.color.common_green_color
            2-> com.qmuiteam.qmui.R.color.qmui_config_color_blue
            3-> R.color.common_yellow_color
            4-> R.color.common_tip_orange
            5-> R.color.common_fans_level_9
            6-> R.color.common_fans_level_7
            7-> R.color.common_fans_level_6
            8-> R.color.common_fans_level_5
            9-> R.color.common_fans_level_4
            10-> R.color.common_fans_level_3
            else-> R.color.common_fans_level_5
        }
    }
    fun getRandomLightColor(index:Int): Int {
        return when(index){
            0-> ColorUtils.string2Int("#FFB6C1")
            1-> ColorUtils.string2Int("#FFDAB9")
            2-> ColorUtils.string2Int("#FFFACD")
            3-> ColorUtils.string2Int("#E0FFFF")
            4-> ColorUtils.string2Int("#98FB98")
            5-> ColorUtils.string2Int("#ADD8E6")
            6-> ColorUtils.string2Int("#DDA0DD")
            7-> ColorUtils.string2Int("#D3D3D3")
            8-> ColorUtils.string2Int("#F5DEB3")
            9-> ColorUtils.string2Int("#FFE4C4")
            else-> ColorUtils.string2Int("#FFE4E1")
        }
    }
}