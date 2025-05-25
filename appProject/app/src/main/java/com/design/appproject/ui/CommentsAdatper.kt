package com.design.appproject.ui

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.CommentsBean
import com.design.appproject.ext.load
import androidx.core.view.isVisible
import android.widget.TextView
import com.union.union_basic.ext.isNotNullOrEmpty
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.widget.CustomWebView
import android.widget.RatingBar
import android.widget.ImageView

/**
 * 评论适配器
 */
class CommentsAdatper: LoadMoreAdapter<CommentsBean>(R.layout.item_comment_layout) {

    override fun convert(holder: BaseViewHolder, item: CommentsBean) {
        holder.getView<ImageView>(R.id.avatar_ifv).load(context,item.avatarurl)
        holder.setText(R.id.name_tv,item.nickname)
        holder.getView<CustomWebView>(R.id.content_tv).setHtml(item.content)
        if (item.score==null){
            holder.setGone(R.id.start_rb,true)
        }else{
            holder.setGone(R.id.start_rb,false)
            holder.getView<RatingBar>(R.id.start_rb).rating = item.score
        }
        holder.setText(R.id.time_tv,item.addtime?:"")
        holder.getView<CustomWebView>(R.id.reply_tv).let {
            it.setHtml("回复："+item.reply)
            it.isVisible = item.reply.isNotNullOrEmpty()
        }
    }
}