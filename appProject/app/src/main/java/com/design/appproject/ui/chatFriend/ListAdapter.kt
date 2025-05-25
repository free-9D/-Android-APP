package com.design.appproject.ui.chatFriend
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.ChatFriendItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 好友表适配器列表
 */
class ListAdapter : LoadMoreAdapter<ChatFriendItemBean>(R.layout.chatfriend_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: ChatFriendItemBean) {
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("chat_friend","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("chat_friend","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("chat_friend","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("chat_friend","删除"))
        }
    }
}