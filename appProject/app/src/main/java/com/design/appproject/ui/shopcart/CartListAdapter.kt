package com.design.appproject.ui.shopcart

import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.shopcart.CartItemBean
import com.design.appproject.ext.load
import com.design.appproject.widget.LoadMoreAdapter

/**
 * 购物车列表适配器
 */
class CartListAdapter: LoadMoreAdapter<CartItemBean>(R.layout.item_cart_list_layout) {

    override fun convert(holder: BaseViewHolder, item: CartItemBean) {
        holder.getView<ImageView>(R.id.item_cart_iv).load(context,item.picture)
        holder.setText(R.id.item_name_price_tv,item.goodname+"\n￥"+item.price)
        holder.setText(R.id.item_count_tv,item.buynumber.toString())
        holder.getView<CheckBox>(R.id.item_cart_cb).let {
            it.isChecked = item.isSelected
        }
    }
}