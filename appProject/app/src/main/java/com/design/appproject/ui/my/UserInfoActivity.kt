package com.design.appproject.ui.my

import android.content.Intent
import android.widget.CheckBox
import android.widget.RadioButton
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.UriUtils
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.ActivityUserInfoLayoutBinding
import com.design.appproject.logic.repository.UserRepository
import com.google.gson.internal.LinkedTreeMap
import com.design.appproject.ext.load
import androidx.core.view.isVisible
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.RegexUtils
import androidx.core.view.children
import com.blankj.utilcode.util.TimeUtils
import com.design.appproject.base.CommonBean
import com.design.appproject.base.EventBus
import com.design.appproject.ext.postEvent
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.widget.BottomSpinner
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import com.union.union_basic.ext.*
import com.union.union_basic.image.selector.SmartPictureSelector
import com.union.union_basic.utils.StorageUtil
import java.io.File
import java.text.SimpleDateFormat

/**
 *用户信息界面
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_USER_INFO)
class UserInfoActivity: BaseBindingActivity<ActivityUserInfoLayoutBinding>() {

    override fun initEvent() {
        setBarTitle("用户信息")
        setBarColor("#A2B772","white")
        binding.apply {
            usersusernameLl.isVisible = "users" == CommonBean.tableName
            userspasswordLl.isVisible = "users" == CommonBean.tableName
            usersroleLl.isVisible = "users" == CommonBean.tableName
            yonghuyonghuzhanghaoLl.isVisible = "yonghu" == CommonBean.tableName
            yonghuyonghuxingmingLl.isVisible = "yonghu" == CommonBean.tableName
            yonghutouxiangLl.isVisible = "yonghu" == CommonBean.tableName
            yonghutouxiangLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@UserInfoActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "touxiang").observeKt{
                    it.getOrNull()?.let {
                        yonghutouxiangIfv.load(this@UserInfoActivity, "file/"+it.file)
                        mSessionInfo["touxiang"] = "file/" + it.file
                    }
                }
            }
        }
            yonghuxingbieLl.isVisible = "yonghu" == CommonBean.tableName
            yonghuxingbieBs.setOptions("男,女".split(","),"请选择性别",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    yonghuxingbieBs.text = content
                    mSessionInfo["xingbie"] = content
                }
            })
            yonghulianxidianhuaLl.isVisible = "yonghu" == CommonBean.tableName
            yonghupquestionLl.isVisible = "yonghu" == CommonBean.tableName
            yonghupanswerLl.isVisible = "yonghu" == CommonBean.tableName
            shangjiashangjiazhanghaoLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiashangjiaxingmingLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiatouxiangLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiatouxiangLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@UserInfoActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "touxiang").observeKt{
                    it.getOrNull()?.let {
                        shangjiatouxiangIfv.load(this@UserInfoActivity, "file/"+it.file)
                        mSessionInfo["touxiang"] = "file/" + it.file
                    }
                }
            }
        }
            shangjiaxingbieLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiaxingbieBs.setOptions("男,女".split(","),"请选择性别",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    shangjiaxingbieBs.text = content
                    mSessionInfo["xingbie"] = content
                }
            })
            shangjialianxidianhuaLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiazizhileixingLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiazizhileixingBs.setOptions("实人认证,企业认证,采购认证,实力认证".split(","),"请选择资质类型",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    shangjiazizhileixingBs.text = content
                    mSessionInfo["zizhileixing"] = content
                }
            })
            shangjiazizhizhengmingLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiazizhizhengmingLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@UserInfoActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "zizhizhengming").observeKt{
                    it.getOrNull()?.let {
                        shangjiazizhizhengmingIfv.load(this@UserInfoActivity, "file/"+it.file)
                        mSessionInfo["zizhizhengming"] = "file/" + it.file
                    }
                }
            }
        }
            shangjiapquestionLl.isVisible = "shangjia" == CommonBean.tableName
            shangjiapanswerLl.isVisible = "shangjia" == CommonBean.tableName
            saveBtn.setOnClickListener { /*保存*/
                verify().yes {
                    UserRepository.update(CommonBean.tableName,mSessionInfo).observeKt {
                        it.getOrNull()?.let {
                            StorageUtil.encode(CommonBean.HEAD_URL_KEY,mSessionInfo["touxiang"].toString())
                            if(CommonBean.tableName == "yonghu"){
                                StorageUtil.encode(CommonBean.USERNAME_KEY,mSessionInfo["yonghuzhanghao"].toString())
                            }
                            if(CommonBean.tableName == "shangjia"){
                                StorageUtil.encode(CommonBean.USERNAME_KEY,mSessionInfo["shangjiazhanghao"].toString())
                            }
                            postEvent(EventBus.USER_INFO_UPDATED,true)
                            "修改成功".showToast()
                        }
                    }
                }
            }
            logoutBtn.setOnClickListener { /*退出登录*/
                StorageUtil.encode(CommonBean.USER_ID_KEY, 0)
                StorageUtil.encode(CommonBean.VIP_KEY, false)
                StorageUtil.encode(CommonBean.HEAD_URL_KEY, "")
                StorageUtil.encode(CommonBean.LOGIN_USER_OPTIONS, "")
                StorageUtil.encode(CommonBean.USERNAME_KEY,"")
                StorageUtil.encode(CommonBean.TOKEN_KEY, "")
                StorageUtil.encode(CommonBean.TABLE_NAME_KEY, "")
                CommonBean.tableName = ""
                postEvent(EventBus.USER_INFO_UPDATED,false)
                finish()
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_MAIN).navigation()
            }
        }
    }

    private fun ActivityUserInfoLayoutBinding.verify():Boolean{
    if ("users" == CommonBean.tableName){
            mSessionInfo["username"] = usersusernameEt.text.toString()
            mSessionInfo["password"] = userspasswordEt.text.toString()
            mSessionInfo["role"] = usersroleEt.text.toString()
            mSessionInfo["role"].toString().isNullOrEmpty().yes {
                "角色不能为空".showToast()
                return false
            }
        }
    if ("yonghu" == CommonBean.tableName){
            mSessionInfo["yonghuzhanghao"] = yonghuyonghuzhanghaoEt.text.toString()
            mSessionInfo["yonghuxingming"] = yonghuyonghuxingmingEt.text.toString()
            mSessionInfo["lianxidianhua"] = yonghulianxidianhuaEt.text.toString()
            mSessionInfo["lianxidianhua"].toString().isNullOrEmpty().yes {
                "联系电话不能为空".showToast()
                return false
            }
            mSessionInfo["lianxidianhua"] = yonghulianxidianhuaEt.text.toString()
            RegexUtils.isMobileExact(mSessionInfo["lianxidianhua"].toString()).no {
                mSessionInfo["lianxidianhua"].toString().isNotNullOrEmpty().yes {
                    "联系电话应输入手机格式".showToast()
                    return false
                }
            }
            mSessionInfo["pquestion"] = yonghupquestionEt.text.toString()
            mSessionInfo["pquestion"].toString().isNullOrEmpty().yes {
                "密保问题不能为空".showToast()
                return false
            }
            mSessionInfo["panswer"] = yonghupanswerEt.text.toString()
            mSessionInfo["panswer"].toString().isNullOrEmpty().yes {
                "密保答案不能为空".showToast()
                return false
            }
        }
    if ("shangjia" == CommonBean.tableName){
            mSessionInfo["shangjiazhanghao"] = shangjiashangjiazhanghaoEt.text.toString()
            mSessionInfo["shangjiaxingming"] = shangjiashangjiaxingmingEt.text.toString()
            mSessionInfo["lianxidianhua"] = shangjialianxidianhuaEt.text.toString()
            mSessionInfo["lianxidianhua"].toString().isNullOrEmpty().yes {
                "联系电话不能为空".showToast()
                return false
            }
            mSessionInfo["lianxidianhua"] = shangjialianxidianhuaEt.text.toString()
            RegexUtils.isMobileExact(mSessionInfo["lianxidianhua"].toString()).no {
                mSessionInfo["lianxidianhua"].toString().isNotNullOrEmpty().yes {
                    "联系电话应输入手机格式".showToast()
                    return false
                }
            }
            mSessionInfo["pquestion"] = shangjiapquestionEt.text.toString()
            mSessionInfo["pquestion"].toString().isNullOrEmpty().yes {
                "密保问题不能为空".showToast()
                return false
            }
            mSessionInfo["panswer"] = shangjiapanswerEt.text.toString()
            mSessionInfo["panswer"].toString().isNullOrEmpty().yes {
                "密保答案不能为空".showToast()
                return false
            }
        }
        return true
    }

    lateinit var mSessionInfo:LinkedTreeMap<String, Any>

    override fun initData() {
        super.initData()
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mSessionInfo= it
                    binding.setData(it)
                }
            }
        }
    }

    private fun ActivityUserInfoLayoutBinding.setData(session:LinkedTreeMap<String, Any>){
        usersusernameEt.setText(session["username"].toString())
        userspasswordEt.setText(session["password"].toString())
        usersroleEt.setText(session["role"].toString())
        yonghuyonghuzhanghaoEt.setText(session["yonghuzhanghao"].toString())
        yonghuyonghuzhanghaoEt.keyListener = null
        yonghuyonghuxingmingEt.setText(session["yonghuxingming"].toString())
        yonghutouxiangIfv.load(this@UserInfoActivity, session["touxiang"].toString())
        yonghuxingbieBs.text = session["xingbie"].toString()
        yonghulianxidianhuaEt.setText(session["lianxidianhua"].toString())
        yonghupquestionEt.setText(session["pquestion"].toString())
        yonghupanswerEt.setText(session["panswer"].toString())
        shangjiashangjiazhanghaoEt.setText(session["shangjiazhanghao"].toString())
        shangjiashangjiazhanghaoEt.keyListener = null
        shangjiashangjiaxingmingEt.setText(session["shangjiaxingming"].toString())
        shangjiatouxiangIfv.load(this@UserInfoActivity, session["touxiang"].toString())
        shangjiaxingbieBs.text = session["xingbie"].toString()
        shangjialianxidianhuaEt.setText(session["lianxidianhua"].toString())
        shangjiazizhileixingBs.text = session["zizhileixing"].toString()
        shangjiazizhizhengmingIfv.load(this@UserInfoActivity, session["zizhizhengming"].toString())
        shangjiapquestionEt.setText(session["pquestion"].toString())
        shangjiapanswerEt.setText(session["panswer"].toString())
    }

}