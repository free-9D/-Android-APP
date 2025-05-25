package com.design.appproject.ui.fankuiyijian
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.FankuiyijianItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 反馈意见适配器列表
 */
class ListAdapter : LoadMoreAdapter<FankuiyijianItemBean>(R.layout.fankuiyijian_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: FankuiyijianItemBean) {
        holder.setText(R.id.fankuibiaoti_tv, item.fankuibiaoti.toString())
        holder.setText(R.id.shangjiazhanghao_tv, item.shangjiazhanghao.toString())
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("fankuiyijian","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("fankuiyijian","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("fankuiyijian","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("fankuiyijian","删除"))
        }
    }
}