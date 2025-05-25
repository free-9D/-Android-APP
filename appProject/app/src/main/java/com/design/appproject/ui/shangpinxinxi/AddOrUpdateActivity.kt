package com.design.appproject.ui.shangpinxinxi

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
import com.design.appproject.bean.ShangpinxinxiItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.ext.afterTextChanged
import android.view.ViewGroup
import android.widget.EditText
import com.design.appproject.databinding.ShangpinxinxiaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 商品信息新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_SHANGPINXINXI)
class AddOrUpdateActivity:BaseBindingActivity<ShangpinxinxiaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mShangpinxinxiItemBean = ShangpinxinxiItemBean()

    override fun initEvent() {
        setBarTitle("商品信息")
        setBarColor("#A2B772","white")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mShangpinxinxiItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mShangpinxinxiItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mShangpinxinxiItemBean,mRefid)
                }
            }
            if (mShangpinxinxiItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mShangpinxinxiItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mShangpinxinxiItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mShangpinxinxiItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mShangpinxinxiItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mShangpinxinxiItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun ShangpinxinxiaddorupdateLayoutBinding.initView(){
            shangpinfenleiBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mShangpinxinxiItemBean.shangpinfenlei =content
                }
            })
            spinner.setOnClickListener {
                spinner.options.isNullOrEmpty().yes {
                    UserRepository.option("shangpinfenlei", "shangpinfenlei", "",null,"",false).observeKt{
                        it.getOrNull()?.let {
                            spinner.setOptions(it.data, "请选择商品分类", false)
                            spinner.dialogShow()
                        }
                    }
                }.otherwise {
                    spinner.dialogShow()
                }
            }
        }
             shangpintupianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "shangpintupian").observeKt{
                    it.getOrNull()?.let {
                        shangpintupianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mShangpinxinxiItemBean.shangpintupian = "file/" + it.file
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
                    if (mShangpinxinxiItemBean.shangjiazhanghao.isNullOrEmpty()){
                        mShangpinxinxiItemBean.shangjiazhanghao = it["shangjiazhanghao"]?.toString()?:""
                    }
                    binding.shangjiazhanghaoEt.keyListener = null
                    if (mShangpinxinxiItemBean.shangjiaxingming.isNullOrEmpty()){
                        mShangpinxinxiItemBean.shangjiaxingming = it["shangjiaxingming"]?.toString()?:""
                    }
                    binding.shangjiaxingmingEt.keyListener = null
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<ShangpinxinxiItemBean>("shangpinxinxi",mId).observeKt {
                it.getOrNull()?.let {
                    mShangpinxinxiItemBean = it.data
                    mShangpinxinxiItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun ShangpinxinxiaddorupdateLayoutBinding.submit() {
        mShangpinxinxiItemBean.shangpinmingcheng = shangpinmingchengEt.text.toString()
        mShangpinxinxiItemBean.shangpinpinpai = shangpinpinpaiEt.text.toString()
        mShangpinxinxiItemBean.shangpinguige = shangpinguigeEt.text.toString()
        mShangpinxinxiItemBean.shangpinjianjie = shangpinjianjieEt.text.toString()
        mShangpinxinxiItemBean.shangpinxiangqing = shangpinxiangqingEt.text.toString()
        priceEt.inputType = InputType.TYPE_CLASS_NUMBER
        mShangpinxinxiItemBean.price = priceEt.text.toString().toDoubleOrNull()?:0.0
        mShangpinxinxiItemBean.shangjiazhanghao = shangjiazhanghaoEt.text.toString()
        mShangpinxinxiItemBean.shangjiaxingming = shangjiaxingmingEt.text.toString()
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mShangpinxinxiItemBean.id?:0>0){
            UserRepository.update("shangpinxinxi",mShangpinxinxiItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<ShangpinxinxiItemBean>("shangpinxinxi",mShangpinxinxiItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun ShangpinxinxiaddorupdateLayoutBinding.setData(){
        if (mShangpinxinxiItemBean.shangpinmingcheng.isNotNullOrEmpty()){
            shangpinmingchengEt.setText(mShangpinxinxiItemBean.shangpinmingcheng.toString())
        }
        if (mShangpinxinxiItemBean.shangpinfenlei.isNotNullOrEmpty()){
            shangpinfenleiBs.text =mShangpinxinxiItemBean.shangpinfenlei
        }
        if (mShangpinxinxiItemBean.shangpinpinpai.isNotNullOrEmpty()){
            shangpinpinpaiEt.setText(mShangpinxinxiItemBean.shangpinpinpai.toString())
        }
        if (mShangpinxinxiItemBean.shangpinguige.isNotNullOrEmpty()){
            shangpinguigeEt.setText(mShangpinxinxiItemBean.shangpinguige.toString())
        }
        if (mShangpinxinxiItemBean.shangpintupian.isNotNullOrEmpty()){
            shangpintupianIfv.load(this@AddOrUpdateActivity, mShangpinxinxiItemBean.shangpintupian)
        }
        if (mShangpinxinxiItemBean.shangpinjianjie.isNotNullOrEmpty()){
            shangpinjianjieEt.setText(mShangpinxinxiItemBean.shangpinjianjie.toString())
        }
        if (mShangpinxinxiItemBean.price>=0){
            priceEt.setText(mShangpinxinxiItemBean.price.toString())
        }
        if (mShangpinxinxiItemBean.shangjiazhanghao.isNotNullOrEmpty()){
            shangjiazhanghaoEt.setText(mShangpinxinxiItemBean.shangjiazhanghao.toString())
        }
        if (mShangpinxinxiItemBean.shangjiaxingming.isNotNullOrEmpty()){
            shangjiaxingmingEt.setText(mShangpinxinxiItemBean.shangjiaxingming.toString())
        }
        if (mShangpinxinxiItemBean.shangpinxiangqing.isNotNullOrEmpty()){
            shangpinxiangqingEt.setText(mShangpinxinxiItemBean.shangpinxiangqing.toString())
        }
    }
}