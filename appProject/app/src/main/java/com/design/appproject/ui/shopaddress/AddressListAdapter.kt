package com.design.appproject.ui.shopaddress

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.shopaddress.AddressItemBean
import com.design.appproject.widget.LoadMoreAdapter

/**
 * 地址列表适配器
 */
class AddressListAdapter:LoadMoreAdapter<AddressItemBean>(R.layout.item_address_list_layout) {

    override fun convert(holder: BaseViewHolder, item: AddressItemBean) {
        holder.setGone(R.id.default_tv,item.isdefault != "是")
        holder.setText(R.id.address_tv,item.address)
        holder.setText(R.id.name_phone_tv,"${item.name}  ${item.phone}")
    }
}