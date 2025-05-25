package com.design.appproject.ui

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.design.appproject.MainApplication
import com.design.appproject.databinding.DialogCommentLayoutBinding
import com.design.appproject.ext.UrlPrefix
import com.design.appproject.logic.repository.UserRepository
import com.dylanc.viewbinding.inflate
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.SmartGlideImageLoader
import com.union.union_basic.ext.showToast
import com.union.union_basic.image.selector.SmartPictureSelector
import com.union.union_basic.logger.LoggerManager
import java.io.File

/**
 * 评论弹窗
 */
class CommentDialog(context:Context,submitListener:(content:String,pid:Long)->Unit) :BottomPopupView(context) {

    private val mSubmitListener = submitListener

    lateinit var binding: DialogCommentLayoutBinding

    var mPid  = 0L

    var mReplyUserName  = ""

    override fun addInnerContent() {
        binding = bottomPopupContainer.inflate()
    }

    private val mLoading by lazy {
        XPopup.Builder(context).asLoading("上传图片中")
    }
    override fun onCreate() {
        super.onCreate()
        binding.apply {
            closeIv.setOnClickListener {
                dismiss()
            }
            commentEt.setPadding(10, 10, 10, 10);
            commentBtn.setOnClickListener {/*提交*/
                var content = commentEt.html
                "操,死,傻".split(",").forEach {/*替换敏感词*/
                    if (content.contains(it)) {
                        content = content.replace(it, "**")
                    }
                }
                mSubmitListener.invoke(content,mPid)
            }

            imgIv.setOnClickListener {
                //发送图片
                SmartPictureSelector.openPicture(context as AppCompatActivity){
                    val path = it[0]
                    mLoading.show()
                    UserRepository.upload(File(path),"").observeForever {
                        mLoading.dismiss()
                        it.getOrNull()?.let {
                            if (commentEt.html.isNullOrEmpty()){
                                commentEt.html = "<img src=\""+UrlPrefix.URL_PREFIX+"file/"+ it.file+"\" alt=\"图像\">"
                            }else{
                                commentEt.html = commentEt.html+"<img src=\""+UrlPrefix.URL_PREFIX+"file/"+ it.file+"\" alt=\"图像\">"
                            }
                        }
                    }
                }
            }
        }
    }

    override fun doAfterShow() {
        super.doAfterShow()
        binding.commentEt.setPlaceholder(if (mReplyUserName.isNullOrEmpty()) "" else "回复：${mReplyUserName}")
    }

    fun cancleAndDismiss(){
        mPid =0L
        binding.commentEt.html =""
        dismiss()
    }
}