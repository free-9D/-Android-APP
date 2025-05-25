package com.design.appproject.ui.duanshipin
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.DuanshipinItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 短视频适配器列表
 */
class ListAdapter : LoadMoreAdapter<DuanshipinItemBean>(R.layout.duanshipin_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: DuanshipinItemBean) {
        val img = item.fengmian.split(",")[0]
        holder.getView<ImageView>(R.id.picture_iv).load(context,img, needPrefix = !img.startsWith("http"))
        holder.setText(R.id.biaoti_tv,"标题:"+ item.biaoti.toString())
        holder.setText(R.id.shangjiazhanghao_tv,"商家账号:"+ item.shangjiazhanghao.toString())
        holder.setText(R.id.shangjiaxingming_tv,"商家姓名:"+ item.shangjiaxingming.toString())
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("duanshipin","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("duanshipin","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("duanshipin","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("duanshipin","删除"))
        }
    }
}