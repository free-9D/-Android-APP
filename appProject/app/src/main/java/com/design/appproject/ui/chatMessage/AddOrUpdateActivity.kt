package com.design.appproject.ui.chatMessage

import android.Manifest
import com.union.union_basic.permission.PermissionUtil
import com.design.appproject.ext.UrlPrefix
import androidx.core.widget.addTextChangedListener
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.core.view.children
import com.design.appproject.utils.Utils
import com.design.appproject.bean.BaiKeBean
import androidx.core.app.ActivityCompat.startActivityForResult
import com.blankj.utilcode.util.UriUtils
import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.internal.LinkedTreeMap
import com.union.union_basic.ext.*
import com.blankj.utilcode.util.RegexUtils
import com.union.union_basic.utils.StorageUtil
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.design.appproject.widget.BottomSpinner
import com.design.appproject.base.CommonBean
import com.blankj.utilcode.util.TimeUtils
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.lxj.xpopup.XPopup
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import java.text.SimpleDateFormat
import android.widget.RatingBar
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.union.union_basic.image.selector.SmartPictureSelector
import java.io.File
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.bean.ChatMessageItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.ext.afterTextChanged
import android.view.ViewGroup
import android.widget.EditText
import com.design.appproject.databinding.ChatmessageaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 消息表新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_CHATMESSAGE)
class AddOrUpdateActivity:BaseBindingActivity<ChatmessageaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mChatMessageItemBean = ChatMessageItemBean()

    override fun initEvent() {
        setBarTitle("消息表")
        setBarColor("#A2B772","white")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mChatMessageItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mChatMessageItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mChatMessageItemBean,mRefid)
                }
            }
            if (mChatMessageItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mChatMessageItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mChatMessageItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mChatMessageItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mChatMessageItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mChatMessageItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun ChatmessageaddorupdateLayoutBinding.initView(){
            submitBtn.setOnClickListener{/*提交*/
                submit()
            }
            setData()
    }

    lateinit var mUserBean:LinkedTreeMap<String, Any>/*当前用户数据*/

    override fun initData() {
        super.initData()
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mUserBean = it
                    it["touxiang"]?.let { it1 -> StorageUtil.encode(CommonBean.HEAD_URL_KEY, it1) }
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<ChatMessageItemBean>("chatmessage",mId).observeKt {
                it.getOrNull()?.let {
                    mChatMessageItemBean = it.data
                    mChatMessageItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun ChatmessageaddorupdateLayoutBinding.submit() {
        uidEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatMessageItemBean.uid = uidEt.text.toString().toLong()
        fidEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatMessageItemBean.fid = fidEt.text.toString().toLong()
        mChatMessageItemBean.content = contentEt.text.toString()
        formatEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatMessageItemBean.format = formatEt.text.toString().toDouble().toInt()
        isReadEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatMessageItemBean.isRead = isReadEt.text.toString().toDouble().toInt()
        if(mChatMessageItemBean.uid<=0){
            "用户id不能为空".showToast()
            return
        }
        if(mChatMessageItemBean.fid<=0){
            "好友id不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mChatMessageItemBean.id?:0>0){
            UserRepository.update("chatmessage",mChatMessageItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<ChatMessageItemBean>("chatmessage",mChatMessageItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun ChatmessageaddorupdateLayoutBinding.setData(){
        if (mChatMessageItemBean.uid>=0){
            uidEt.setText(mChatMessageItemBean.uid.toString())
        }
        if (mChatMessageItemBean.fid>=0){
            fidEt.setText(mChatMessageItemBean.fid.toString())
        }
        if (mChatMessageItemBean.content.isNotNullOrEmpty()){
            contentEt.setText(mChatMessageItemBean.content.toString())
        }
        if (mChatMessageItemBean.format>=0){
            formatEt.setText(mChatMessageItemBean.format.toString())
        }
        if (mChatMessageItemBean.isRead>=0){
            isReadEt.setText(mChatMessageItemBean.isRead.toString())
        }
    }
}