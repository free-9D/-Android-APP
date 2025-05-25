package com.design.appproject.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.design.appproject.base.CommonBean
import com.design.appproject.bean.ChatFriendItemBean
import com.design.appproject.bean.ChatMessageItemBean
import com.design.appproject.databinding.DialogChatMessageLayoutBinding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.utils.Utils
import com.dylanc.viewbinding.inflate
import com.lxj.xpopup.core.BottomPopupView
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.yes
import com.union.union_basic.image.selector.SmartPictureSelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

//聊天消息弹窗
class ChatMessageDialog(context: Context, avatarUrl:String="",fid:Long,tableName:String="",name:String="") : BottomPopupView(context) {

    lateinit var binding: DialogChatMessageLayoutBinding
    private lateinit var job: Job

    private val mAvatarUrl = avatarUrl
    private val mFid = fid
    private val mTableName = tableName
    private val mName = name
    private var isFirst = true
    override fun addInnerContent() {
        binding = bottomPopupContainer.inflate()
    }

    override fun onCreate() {
        super.onCreate()
        binding.apply {
            chatMessageList.isEnabled =false
            inputEt.isFocusable = false
            inputEt.isFocusableInTouchMode = false
            chatMessageList.setAdapter(ChatMessageListAdapter(mAvatarUrl))
            sendBtn.setOnClickListener {
                if (inputEt.text.toString().isNullOrEmpty()){
                    "请输入内容".showToast()
                    return@setOnClickListener
                }
                //1.查询好友列表
                HomeRepository.page<ChatFriendItemBean>("chatfriend",mapOf("uid" to Utils.getUserId().toString(),"fid" to mFid.toString())).observeForever {
                    it.getOrNull()?.let {
                        if (it.data.list.find { it.fid == mFid && it.type==2}!=null){//在好友列表
                            addContent(inputEt.text.toString())
                        }else{//先添加好友列表
                            HomeRepository.add<Any>("chatfriend",ChatFriendItemBean(fid = mFid,name=mName, picture = mAvatarUrl, tablename = mTableName, type = 2, uid = Utils.getUserId())).observeForever {}
                            HomeRepository.add<Any>("chatfriend",ChatFriendItemBean(fid = Utils.getUserId(),name=Utils.getUserName()?:"", picture = Utils.getAvatarUrl()?:"", tablename = CommonBean.tableName, type = 2, uid = mFid)).observeForever {}
                            addContent(inputEt.text.toString())
                        }
                    }
                }
            }
            imageBtn.setOnClickListener {
                //发送图片
                SmartPictureSelector.openPicture(context as AppCompatActivity){
                    val path = it[0]
                    UserRepository.upload(File(path),"").observeForever {
                        it.getOrNull()?.let {
                            addContent("file/" + it.file, fortmat = 2)
                        }
                    }
                }
            }
            returnBtn.setOnClickListener {
                dismiss()
            }
        }

        // 启动协程
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) { // 循环直到协程被取消
                withContext(Dispatchers.Main) {
                    getMList(isFirst)
                    isFirst=false
                }
                delay(3000) // 延迟3秒
            }
        }
    }

    override fun dismiss() {
        job.cancel()
        super.dismiss()
    }
    private fun getMList(isSend:Boolean=false){
        HomeRepository.mlist<ChatMessageItemBean>(mapOf("page" to "1","limit" to "1000","uid" to Utils.getUserId().toString(),"fid" to mFid.toString())).observeForever {
            it.getOrNull()?.let {
                binding.chatMessageList.setData(it.data.list,it.data.total)
                isSend.yes {
                    binding.inputEt.text?.clear()
                    KeyboardUtils.hideSoftInput(binding.inputEt)
                    binding.chatMessageList.mRecyclerView.postDelayed({ binding.chatMessageList.mRecyclerView.scrollToPosition(it.data.total-1) },200)
                }
            }
        }
    }
    private fun addContent(content:String,fortmat:Int=1){
        HomeRepository.add<Any>("chatmessage",ChatMessageItemBean(content = content, fid = mFid, uid = Utils.getUserId(), format = fortmat)).observeForever {
            getMList(true)
        }
    }
    override fun getPopupHeight(): Int {
        return (com.blankj.utilcode.util.ScreenUtils.getScreenHeight()*0.8).toInt()
    }
}