package com.design.appproject.ui.yijianfankui
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.YijianfankuiItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 意见反馈适配器列表
 */
class ListAdapter : LoadMoreAdapter<YijianfankuiItemBean>(R.layout.yijianfankui_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: YijianfankuiItemBean) {
        holder.setText(R.id.fankuibiaoti_tv, item.fankuibiaoti.toString())
        holder.setText(R.id.yonghuzhanghao_tv, item.yonghuzhanghao.toString())
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("yijianfankui","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("yijianfankui","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("yijianfankui","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("yijianfankui","删除"))
        }
    }
}