package com.design.appproject.ui.chatFriend

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
import com.design.appproject.bean.ChatFriendItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.ext.afterTextChanged
import android.view.ViewGroup
import android.widget.EditText
import com.design.appproject.databinding.ChatfriendaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 好友表新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_CHATFRIEND)
class AddOrUpdateActivity:BaseBindingActivity<ChatfriendaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mChatFriendItemBean = ChatFriendItemBean()

    override fun initEvent() {
        setBarTitle("好友表")
        setBarColor("#A2B772","white")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mChatFriendItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mChatFriendItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mChatFriendItemBean,mRefid)
                }
            }
            if (mChatFriendItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mChatFriendItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mChatFriendItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mChatFriendItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mChatFriendItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mChatFriendItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun ChatfriendaddorupdateLayoutBinding.initView(){
             pictureLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "picture").observeKt{
                    it.getOrNull()?.let {
                        pictureIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mChatFriendItemBean.picture = "file/" + it.file
                    }
                }
            }
        }
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
            HomeRepository.info<ChatFriendItemBean>("chatfriend",mId).observeKt {
                it.getOrNull()?.let {
                    mChatFriendItemBean = it.data
                    mChatFriendItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun ChatfriendaddorupdateLayoutBinding.submit() {
        uidEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatFriendItemBean.uid = uidEt.text.toString().toLong()
        fidEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatFriendItemBean.fid = fidEt.text.toString().toLong()
        mChatFriendItemBean.name = nameEt.text.toString()
        mChatFriendItemBean.role = roleEt.text.toString()
        mChatFriendItemBean.tablename = tablenameEt.text.toString()
        mChatFriendItemBean.alias = aliasEt.text.toString()
        typeEt.inputType = InputType.TYPE_CLASS_NUMBER
        mChatFriendItemBean.type = typeEt.text.toString().toDouble().toInt()
        if(mChatFriendItemBean.uid<=0){
            "用户id不能为空".showToast()
            return
        }
        if(mChatFriendItemBean.fid<=0){
            "好友id不能为空".showToast()
            return
        }
        if(mChatFriendItemBean.name.isNullOrEmpty()){
            "名称不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mChatFriendItemBean.id?:0>0){
            UserRepository.update("chatfriend",mChatFriendItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<ChatFriendItemBean>("chatfriend",mChatFriendItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun ChatfriendaddorupdateLayoutBinding.setData(){
        if (mChatFriendItemBean.uid>=0){
            uidEt.setText(mChatFriendItemBean.uid.toString())
        }
        if (mChatFriendItemBean.fid>=0){
            fidEt.setText(mChatFriendItemBean.fid.toString())
        }
        if (mChatFriendItemBean.name.isNotNullOrEmpty()){
            nameEt.setText(mChatFriendItemBean.name.toString())
        }
        if (mChatFriendItemBean.picture.isNotNullOrEmpty()){
            pictureIfv.load(this@AddOrUpdateActivity, mChatFriendItemBean.picture)
        }
        if (mChatFriendItemBean.role.isNotNullOrEmpty()){
            roleEt.setText(mChatFriendItemBean.role.toString())
        }
        if (mChatFriendItemBean.tablename.isNotNullOrEmpty()){
            tablenameEt.setText(mChatFriendItemBean.tablename.toString())
        }
        if (mChatFriendItemBean.alias.isNotNullOrEmpty()){
            aliasEt.setText(mChatFriendItemBean.alias.toString())
        }
        if (mChatFriendItemBean.type>=0){
            typeEt.setText(mChatFriendItemBean.type.toString())
        }
    }
}