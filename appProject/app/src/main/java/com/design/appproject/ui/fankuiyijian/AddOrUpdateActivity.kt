package com.design.appproject.ui.fankuiyijian

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
import com.design.appproject.bean.FankuiyijianItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.ext.afterTextChanged
import android.view.ViewGroup
import android.widget.EditText
import com.design.appproject.databinding.FankuiyijianaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 反馈意见新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_FANKUIYIJIAN)
class AddOrUpdateActivity:BaseBindingActivity<FankuiyijianaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mFankuiyijianItemBean = FankuiyijianItemBean()

    override fun initEvent() {
        setBarTitle("反馈意见")
        setBarColor("#A2B772","white")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mFankuiyijianItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mFankuiyijianItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mFankuiyijianItemBean,mRefid)
                }
            }
            if (mFankuiyijianItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mFankuiyijianItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mFankuiyijianItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mFankuiyijianItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mFankuiyijianItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mFankuiyijianItemBean,Utils.getUserId())
            }
        }
        binding.initView()

        binding.fankuineirongRichLayout.apply{
            actionBold.setOnClickListener {
                richEt.setBold()
            }
            actionItalic.setOnClickListener {
                richEt.setItalic()
            }
            actionStrikethrough.setOnClickListener {
                richEt.setStrikeThrough()
            }
            actionUnderline.setOnClickListener {
                richEt.setUnderline()
            }
            actionHeading1.setOnClickListener {
                richEt.setHeading(1)
            }
            actionHeading2.setOnClickListener {
                richEt.setHeading(2)
            }
            actionHeading3.setOnClickListener {
                richEt.setHeading(3)
            }
            actionHeading4.setOnClickListener {
                richEt.setHeading(4)
            }
            actionHeading5.setOnClickListener {
                richEt.setHeading(5)
            }
            actionIndent.setOnClickListener {
                richEt.setIndent()
            }
            actionOutdent.setOnClickListener {
                richEt.setOutdent()
            }
            actionAlignCenter.setOnClickListener {
                richEt.setAlignCenter()
            }
            actionAlignLeft.setOnClickListener {
                richEt.setAlignLeft()
            }
            actionAlignRight.setOnClickListener {
                richEt.setAlignRight()
            }
            actionInsertBullets.setOnClickListener {
                richEt.setBullets()
            }
            actionInsertNumbers.setOnClickListener {
                richEt.setNumbers()
            }
            actionInsertImage.setOnClickListener {
                SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                    val path = it[0]
                    UserRepository.upload(File(path),"").observeKt {
                        it.getOrNull()?.let {
                            richEt.insertImage(UrlPrefix.URL_PREFIX+"file/" + it.file, "dachshund", 320)
                        }
                    }
                }
            }
        }
    }

    fun FankuiyijianaddorupdateLayoutBinding.initView(){
            mFankuiyijianItemBean.fankuishijian = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
            fankuishijianTv.text = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
            val mfankuishijianPicker = DatimePicker(this@AddOrUpdateActivity).apply {
            wheelLayout.setDateFormatter(BirthdayFormatter())
            wheelLayout.setTimeFormatter(UnitTimeFormatter())
            wheelLayout.setRange(DatimeEntity.yearOnFuture(-100), DatimeEntity.yearOnFuture(50), DatimeEntity.now())
            setOnDatimePickedListener { year, month, day, hour, minute, second ->
                fankuishijianTv.text = "$year-$month-$day $hour:$minute:$second"
                mFankuiyijianItemBean.fankuishijian="$year-$month-$day $hour:$minute:$second"
            }
        }
            fankuishijianTv.setOnClickListener {
            mfankuishijianPicker.show()
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
                    /**ss读取*/
                    if (mFankuiyijianItemBean.shangjiazhanghao.isNullOrEmpty()){
                        mFankuiyijianItemBean.shangjiazhanghao = it["shangjiazhanghao"]?.toString()?:""
                    }
                    binding.shangjiazhanghaoEt.keyListener = null
                    if (mFankuiyijianItemBean.shangjiaxingming.isNullOrEmpty()){
                        mFankuiyijianItemBean.shangjiaxingming = it["shangjiaxingming"]?.toString()?:""
                    }
                    binding.shangjiaxingmingEt.keyListener = null
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<FankuiyijianItemBean>("fankuiyijian",mId).observeKt {
                it.getOrNull()?.let {
                    mFankuiyijianItemBean = it.data
                    mFankuiyijianItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun FankuiyijianaddorupdateLayoutBinding.submit() {
        mFankuiyijianItemBean.fankuibiaoti = fankuibiaotiEt.text.toString()
        mFankuiyijianItemBean.fankuineirong = fankuineirongRichLayout.richEt.html
        mFankuiyijianItemBean.shangjiazhanghao = shangjiazhanghaoEt.text.toString()
        mFankuiyijianItemBean.shangjiaxingming = shangjiaxingmingEt.text.toString()
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mFankuiyijianItemBean.id?:0>0){
            UserRepository.update("fankuiyijian",mFankuiyijianItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<FankuiyijianItemBean>("fankuiyijian",mFankuiyijianItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun FankuiyijianaddorupdateLayoutBinding.setData(){
        if (mFankuiyijianItemBean.fankuibiaoti.isNotNullOrEmpty()){
            fankuibiaotiEt.setText(mFankuiyijianItemBean.fankuibiaoti.toString())
        }
        if (mFankuiyijianItemBean.shangjiazhanghao.isNotNullOrEmpty()){
            shangjiazhanghaoEt.setText(mFankuiyijianItemBean.shangjiazhanghao.toString())
        }
        if (mFankuiyijianItemBean.shangjiaxingming.isNotNullOrEmpty()){
            shangjiaxingmingEt.setText(mFankuiyijianItemBean.shangjiaxingming.toString())
        }
        fankuineirongRichLayout.richEt.setHtml(mFankuiyijianItemBean.fankuineirong)
    }
}