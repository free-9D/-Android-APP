package com.design.appproject.ui

import android.widget.ImageView
import androidx.core.view.isVisible
import com.blankj.utilcode.util.FileUtils
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.base.CommonBean
import com.design.appproject.base.CommonBean.USER_ID_KEY
import com.design.appproject.bean.ChatMessageItemBean
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils
import com.design.appproject.widget.LoadMoreAdapter
import com.union.union_basic.logger.LoggerManager
import com.union.union_basic.utils.StorageUtil

/**
 * 聊天列表适配器
 * */
class ChatMessageListAdapter(avatarUrl:String =""): LoadMoreAdapter<ChatMessageItemBean>(R.layout.item_chat_message_list_layout) {

    val mAvatarUrl = avatarUrl

    override fun convert(holder: BaseViewHolder, item: ChatMessageItemBean) {
        holder.setGone(R.id.reply_llc,item.uid== Utils.getUserId())
        holder.setGone(R.id.ask_llc,item.uid!= Utils.getUserId())
        holder.getView<ImageView>(R.id.reply_avatar_ifv).load(context,mAvatarUrl)
        holder.getView<ImageView>(R.id.ask_avatar_ifv).load(context,Utils.getAvatarUrl())
        if (item.content.startsWith("file/")){
            holder.getView<ImageView>(R.id.ask_iv).isVisible =item.uid== Utils.getUserId()
            holder.getView<ImageView>(R.id.reply_iv).isVisible =item.uid!= Utils.getUserId()
            holder.getView<ImageView>(R.id.ask_iv).load(context,item.content)
            holder.getView<ImageView>(R.id.reply_iv).load(context,item.content)
            holder.setText(R.id.ask_tv,"")
            holder.setText(R.id.reply_tv,"")
        }else{
            holder.getView<ImageView>(R.id.ask_iv).isVisible =item.uid!= Utils.getUserId()
            holder.getView<ImageView>(R.id.reply_iv).isVisible =item.uid== Utils.getUserId()
            holder.setText(R.id.ask_tv,item.content)
            holder.setText(R.id.reply_tv,item.content)
        }
    }
}