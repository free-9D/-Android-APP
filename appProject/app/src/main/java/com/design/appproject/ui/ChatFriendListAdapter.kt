package com.design.appproject.ui

import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.ChatFriendItemBean
import com.design.appproject.ext.load
import com.design.appproject.widget.LoadMoreAdapter

/**
 * 好友列表适配器
 * */
class ChatFriendListAdapter : LoadMoreAdapter<ChatFriendItemBean> {

    private var mType=1
    constructor(type:Int=1) : super(R.layout.item_chat_friend_list_layout) {
        mType=type
        addChildClickViewIds(R.id.refuse_btn,R.id.ensure_btn)
    }

    override fun convert(holder: BaseViewHolder, item: ChatFriendItemBean) {
        holder.getView<ImageView>(R.id.avatar_ifv).load(context,item.picture)
        holder.setGone(R.id.add_ll,item.type!=0 || mType==2)
        holder.setGone(R.id.content_ll,mType!=2)
        holder.setGone(R.id.num_tv,item.notreadnum<=0)
        holder.setText(R.id.num_tv,item.notreadnum.toString())
        holder.setText(R.id.content_tv, if (item.content.startsWith("file/")) "[图片]" else item.content)
        holder.setText(R.id.name_tv,item.name + if (item.type==0) "申请添加您为好友" else "")
    }
}