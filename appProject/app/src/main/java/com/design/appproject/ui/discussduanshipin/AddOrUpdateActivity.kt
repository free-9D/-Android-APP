package com.design.appproject.ui.discussduanshipin

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
import com.design.appproject.bean.DiscussduanshipinItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.ext.afterTextChanged
import android.view.ViewGroup
import android.widget.EditText
import com.design.appproject.databinding.DiscussduanshipinaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 短视频评论表新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_DISCUSSDUANSHIPIN)
class AddOrUpdateActivity:BaseBindingActivity<DiscussduanshipinaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mDiscussduanshipinItemBean = DiscussduanshipinItemBean()

    override fun initEvent() {
        setBarTitle("短视频评论表")
        setBarColor("#A2B772","white")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mDiscussduanshipinItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mDiscussduanshipinItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mDiscussduanshipinItemBean,mRefid)
                }
            }
            if (mDiscussduanshipinItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mDiscussduanshipinItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mDiscussduanshipinItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mDiscussduanshipinItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mDiscussduanshipinItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mDiscussduanshipinItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun DiscussduanshipinaddorupdateLayoutBinding.initView(){
        contentEt.setPlaceholder("评论内容")
        contentEt.setPadding(5.dp,5.dp,5.dp,5.dp)
        imageTv.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path),"").observeKt {
                    it.getOrNull()?.let {
                        if (contentEt.html.isNullOrEmpty()){
                        contentEt.html = "<img src=\""+UrlPrefix.URL_PREFIX+"file/"+ it.file+"\" alt=\"图像\" width=\"100\">"
                    }else{
                        contentEt.html = contentEt.html+"<img src=\""+UrlPrefix.URL_PREFIX+"file/"+ it.file+"\" alt=\"图像\" width=\"100\">"
                    }
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
            HomeRepository.info<DiscussduanshipinItemBean>("discussduanshipin",mId).observeKt {
                it.getOrNull()?.let {
                    mDiscussduanshipinItemBean = it.data
                    mDiscussduanshipinItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun DiscussduanshipinaddorupdateLayoutBinding.submit() {
        mDiscussduanshipinItemBean.content = contentEt.html
        mDiscussduanshipinItemBean.avatarurl = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY)?:""
        if(mDiscussduanshipinItemBean.refid<=0){
            "关联表id不能为空".showToast()
            return
        }
        if(mDiscussduanshipinItemBean.userid<=0){
            "用户id不能为空".showToast()
            return
        }
        if(mDiscussduanshipinItemBean.content.isNullOrEmpty()){
            "评论内容不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mDiscussduanshipinItemBean.id?:0>0){
            UserRepository.update("discussduanshipin",mDiscussduanshipinItemBean).observeKt{
            it.getOrNull()?.let {
                mDiscussduanshipinItemBean.avatarurl = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY)?:""
                setResult(RESULT_OK)
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<DiscussduanshipinItemBean>("discussduanshipin",mDiscussduanshipinItemBean).observeKt{
            it.getOrNull()?.let {
                mDiscussduanshipinItemBean.avatarurl = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY)?:""
                setResult(RESULT_OK)
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun DiscussduanshipinaddorupdateLayoutBinding.setData(){
    }
}