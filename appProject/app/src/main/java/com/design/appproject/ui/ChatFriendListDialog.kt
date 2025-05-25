package com.design.appproject.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.design.appproject.R
import com.design.appproject.bean.ChatFriendItemBean
import com.design.appproject.databinding.BaseListLayoutBinding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.utils.Utils
import com.dylanc.viewbinding.inflate
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.union.union_basic.ext.showToast

//好友列表弹窗  type :1为好友列表，2为聊天记录
class ChatFriendListDialog(context: Context,type:Int=1) : BottomPopupView(context) {

    lateinit var binding:BaseListLayoutBinding

    private val mType = type
    override fun addInnerContent() {
        binding = bottomPopupContainer.inflate()
    }

    override fun onCreate() {
        super.onCreate()
        binding.apply {
            val chatFriendListAdapter = ChatFriendListAdapter(mType)
            srv.setAdapter(chatFriendListAdapter)
            chatFriendListAdapter.setOnItemClickListener { adapter, view, position ->
                (adapter.data[position] as ChatFriendItemBean).let {
                    //聊天
                    dismiss()
                    XPopup.Builder(context).asCustom(ChatMessageDialog(context,it.picture,it.fid,it.tablename,it.name)).show()
                }
            }
            chatFriendListAdapter.setOnItemChildClickListener{ adapter, view, position ->
                if (view.id==R.id.ensure_btn){
                    (adapter.data[position] as ChatFriendItemBean).let {chat->
                        XPopup.Builder(context).asConfirm("提示","是否同意好友申请？") {
                            HomeRepository.add<Any>("chatfriend",ChatFriendItemBean(fid = chat.uid, uid = chat.fid, type = 1, tablename = chat.tablename, name = Utils.getUserName().toString(), picture = Utils.getAvatarUrl()?:"")).observeForever {
                                it.getOrNull()?.let {
                                }
                            }
                            HomeRepository.add<Any>("chatfriend",ChatFriendItemBean(fid = chat.fid, uid = chat.uid, type = 1, tablename = chat.tablename, name = chat.name, picture = chat.picture)).observeForever {
                                it.getOrNull()?.let {
                                    HomeRepository.delete<Any>("chatfriend", listOf(chat.id)).observeForever {
                                        it.getOrNull()?.let {
                                            "添加好友成功".showToast()
                                            getChatFriendList()
                                        }
                                    }
                                }
                            }
                        }.show()
                    }
                }
                if (view.id == R.id.refuse_btn){
                    (adapter.data[position] as ChatFriendItemBean).let {
                        XPopup.Builder(context).asConfirm("提示","是否拒绝此好友申请？？") {
                            HomeRepository.delete<Any>("chatfriend", listOf(it.id)).observeForever {
                                it.getOrNull()?.let {
                                    "操作成功".showToast()
                                    getChatFriendList()
                                }
                            }
                        }.show()
                    }
                }
            }
            chatFriendListAdapter.setEmptyView(TextView(context).apply {
                text = if (mType ==1) "暂无好友" else "暂无聊天记录"
                gravity = Gravity.CENTER
            })
            srv.setBackgroundColor(ColorUtils.getColor(R.color.common_bg_color))
        }
        getChatFriendList()
    }

    private fun getChatFriendList(){
        if (mType==2){
            HomeRepository.page2<ChatFriendItemBean>("chatfriend", mapOf("uid" to Utils.getUserId().toString(),"type" to "2")).observeForever {
                it.getOrNull()?.let {
                    binding.srv.setData(it.data.list,it.data.list.size)
                }
            }
        }else{
            HomeRepository.page<ChatFriendItemBean>("chatfriend", mapOf("uid" to Utils.getUserId().toString())).observeForever {
                it.getOrNull()?.let {
                    val sortedList = it.data.list.sortedBy { it.type }
                    val arr = sortedList.filter { it.type != 2 }
                    binding.srv.setData(arr,arr.size)
                }
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun getPopupHeight(): Int {
        return (com.blankj.utilcode.util.ScreenUtils.getScreenHeight()*0.6).toInt()
    }
}