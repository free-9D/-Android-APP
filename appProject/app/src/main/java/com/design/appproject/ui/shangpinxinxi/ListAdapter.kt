package com.design.appproject.ui.shangpinxinxi
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.ShangpinxinxiItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 商品信息适配器列表
 */
class ListAdapter : LoadMoreAdapter<ShangpinxinxiItemBean>(R.layout.shangpinxinxi_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: ShangpinxinxiItemBean) {
        holder.setText(R.id.shangpinmingcheng_tv,"商品名称:"+ item.shangpinmingcheng.toString())
        val img = item.shangpintupian.split(",")[0]
        holder.getView<ImageView>(R.id.picture_iv).load(context,img, needPrefix = !img.startsWith("http"))
        holder.setText(R.id.price_tv,"￥${item.price}")
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("shangpinxinxi","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("shangpinxinxi","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("shangpinxinxi","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("shangpinxinxi","删除"))
        }
    }
}