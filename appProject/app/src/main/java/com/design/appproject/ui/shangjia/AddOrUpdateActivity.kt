package com.design.appproject.ui.shangjia

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
import com.design.appproject.bean.ShangjiaItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.ext.afterTextChanged
import android.view.ViewGroup
import android.widget.EditText
import com.design.appproject.databinding.ShangjiaaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 商家新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_SHANGJIA)
class AddOrUpdateActivity:BaseBindingActivity<ShangjiaaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mShangjiaItemBean = ShangjiaItemBean()

    override fun initEvent() {
        setBarTitle("商家")
        setBarColor("#A2B772","white")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mShangjiaItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mShangjiaItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mShangjiaItemBean,mRefid)
                }
            }
            if (mShangjiaItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mShangjiaItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mShangjiaItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mShangjiaItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mShangjiaItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mShangjiaItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun ShangjiaaddorupdateLayoutBinding.initView(){
             touxiangLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "touxiang").observeKt{
                    it.getOrNull()?.let {
                        touxiangIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mShangjiaItemBean.touxiang = "file/" + it.file
                    }
                }
            }
        }
            xingbieBs.setOptions("男,女".split(","),"请选择性别",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    xingbieBs.text = content
                    mShangjiaItemBean.xingbie =content
                }
            })
            zizhileixingBs.setOptions("实人认证,企业认证,采购认证,实力认证".split(","),"请选择资质类型",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    zizhileixingBs.text = content
                    mShangjiaItemBean.zizhileixing =content
                }
            })
             zizhizhengmingLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "zizhizhengming").observeKt{
                    it.getOrNull()?.let {
                        zizhizhengmingIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mShangjiaItemBean.zizhizhengming = "file/" + it.file
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
                    /**ss读取*/
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<ShangjiaItemBean>("shangjia",mId).observeKt {
                it.getOrNull()?.let {
                    mShangjiaItemBean = it.data
                    mShangjiaItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun ShangjiaaddorupdateLayoutBinding.submit() {
        mShangjiaItemBean.shangjiazhanghao = shangjiazhanghaoEt.text.toString()
        mShangjiaItemBean.mima = mimaEt.text.toString()
        mShangjiaItemBean.shangjiaxingming = shangjiaxingmingEt.text.toString()
        mShangjiaItemBean.lianxidianhua = lianxidianhuaEt.text.toString()
        if(mShangjiaItemBean.shangjiazhanghao.isNullOrEmpty()){
            "商家账号不能为空".showToast()
            return
        }
        if(mShangjiaItemBean.mima.isNullOrEmpty()){
            "密码不能为空".showToast()
            return
        }
        if(mShangjiaItemBean.shangjiaxingming.isNullOrEmpty()){
            "商家姓名不能为空".showToast()
            return
        }
        if(mShangjiaItemBean.touxiang.isNullOrEmpty()){
            "头像不能为空".showToast()
            return
        }
        RegexUtils.isMobileExact(mShangjiaItemBean.lianxidianhua).no {
            "联系电话应输入手机格式".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mShangjiaItemBean.id?:0>0){
            UserRepository.update("shangjia",mShangjiaItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<ShangjiaItemBean>("shangjia",mShangjiaItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun ShangjiaaddorupdateLayoutBinding.setData(){
        if (mShangjiaItemBean.shangjiazhanghao.isNotNullOrEmpty()){
            shangjiazhanghaoEt.setText(mShangjiaItemBean.shangjiazhanghao.toString())
        }
        if (mShangjiaItemBean.mima.isNotNullOrEmpty()){
            mimaEt.setText(mShangjiaItemBean.mima.toString())
        }
        if (mShangjiaItemBean.shangjiaxingming.isNotNullOrEmpty()){
            shangjiaxingmingEt.setText(mShangjiaItemBean.shangjiaxingming.toString())
        }
        if (mShangjiaItemBean.touxiang.isNotNullOrEmpty()){
            touxiangIfv.load(this@AddOrUpdateActivity, mShangjiaItemBean.touxiang)
        }
        if (mShangjiaItemBean.xingbie.isNotNullOrEmpty()){
            xingbieBs.text =mShangjiaItemBean.xingbie
        }
        if (mShangjiaItemBean.lianxidianhua.isNotNullOrEmpty()){
            lianxidianhuaEt.setText(mShangjiaItemBean.lianxidianhua.toString())
        }
        if (mShangjiaItemBean.zizhileixing.isNotNullOrEmpty()){
            zizhileixingBs.text =mShangjiaItemBean.zizhileixing
        }
        if (mShangjiaItemBean.zizhizhengming.isNotNullOrEmpty()){
            zizhizhengmingIfv.load(this@AddOrUpdateActivity, mShangjiaItemBean.zizhizhengming)
        }
    }
}