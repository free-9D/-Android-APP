package com.design.appproject.ui

import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.base.CommonBean.TABLE_LIST
import com.design.appproject.base.EventBus
import com.design.appproject.bean.UserInfoBean
import com.design.appproject.bean.roleMenusList
import com.design.appproject.databinding.ActivityLoginLayoutBinding
import com.design.appproject.ext.load
import com.design.appproject.ext.postEvent
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.logic.viewmodel.UserViewModel
import com.design.appproject.widget.BottomSpinner
import com.union.union_basic.ext.*
import com.union.union_basic.image.selector.SmartPictureSelector
import com.union.union_basic.utils.StorageUtil
import java.io.File
import com.google.gson.internal.LinkedTreeMap
import java.math.BigDecimal
/**登录页
 * */
@Route(path = CommonArouteApi.PATH_ACTIVITY_LOGIN)
class LoginActivity : BaseBindingActivity<ActivityLoginLayoutBinding>() {

    private val mUserViewModel by viewModels<UserViewModel>()

    override fun initEvent() {
        binding.apply {
            setBarTitle("登录")
            setBarColor("#A2B772","white")
            initLoginBtn()
            initImageTitle()
            initForgetBtn()
            initRegisterBtn()
            initUsertypeTv()
        }
    }

    private fun ActivityLoginLayoutBinding.initLoginBtn() { /*登录*/
        loginBtn.setOnClickListener {
            verify().yes {
                showLoading("登录中...")
                mUserViewModel.login(
                    usernameEt.text.toString().trim(),
                    passwordEt.text.toString().trim()
                )
            }
        }
    }

    /**验证*/
    private fun ActivityLoginLayoutBinding.verify(): Boolean {
        return when {
            usernameEt.text.toString().isNullOrEmpty() -> {
                "请输入账号".showToast()
                false
            }

            passwordEt.text.toString().isNullOrEmpty()-> {
                "请输入密码".showToast()
                false
            }
            usertypeBs.text.toString().trim() == "请选择登录的用户类型" -> {
                "请选择登录的用户类型".showToast()
                false
            }
            else -> true
        }
    }

    /**
     * 图像
     */
    private fun ActivityLoginLayoutBinding.initImageTitle() {
        titleIv.load(this@LoginActivity,"http://clfile.zggen.cn/20250321/ca6eb316a26e43b9a00b91da9006011f.jpg",needPrefix=false)
    }

    /**
     * 注册
     */
    private fun ActivityLoginLayoutBinding.initRegisterBtn() {
        yonghuTv.setOnClickListener {
            ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_REGISTER_YONGHU).navigation()
        }
    }
    /**
     * 忘记密码
     */
    private fun ActivityLoginLayoutBinding.initForgetBtn() {
        forgetBtn.setOnClickListener {
            ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_FORGET).navigation()
        }
    }
    /**
     * 用户登录类型
     */
    val roleNameList by lazy {
        roleMenusList?.filter { it.hasFrontLogin == "是" }?.map { it.roleName } ?: listOf("")
    }

    private fun ActivityLoginLayoutBinding.initUsertypeTv() {
        usertypeMfl.isVisible = roleNameList.size>1
        usertypeBs.setOptions(roleNameList, "请选择登录用户类型", listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    usertypeBs.text = content
                    CommonBean.tableName = TABLE_LIST[position]
                }
            })
        if (roleNameList.size == 1) {
            usertypeBs.text = roleNameList[0]
            CommonBean.tableName = TABLE_LIST[0]
        }
    }
    override fun initData() {
        super.initData()
        mUserViewModel.loginLiveData.observeKt {
            it.getOrNull()?.let {
                it.token?.isNotNullOrEmpty()?.yes {
                    loginSuccess(it.token?:"")
                }
            }
        }
    }

    private fun loginSuccess(token:String){
        StorageUtil.encode(CommonBean.TOKEN_KEY, token)
        StorageUtil.encode(CommonBean.TABLE_NAME_KEY, CommonBean.tableName)
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                "登录成功".showToast()

                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    var id = it["id"].toString()
                    if (it["id"].toString().endsWith(".0")){
                        id = id.split(".0")[0]
                    }
                    StorageUtil.encode(CommonBean.USER_ID_KEY, BigDecimal(id).toPlainString().toString().toLong())
                    StorageUtil.encode("USER_JSON",it.toString())
                    if ("users" == CommonBean.tableName){
                        StorageUtil.encode(CommonBean.VIP_KEY, if(it["vip"].toString()=="是") "(VIP)" else "")
                    }
                    if ("yonghu" == CommonBean.tableName){
                        StorageUtil.encode(CommonBean.VIP_KEY, if(it["vip"].toString()=="是") "(VIP)" else "")
                        StorageUtil.encode(CommonBean.HEAD_URL_KEY, it["touxiang"].toString()?:"")
                    }
                    if ("shangjia" == CommonBean.tableName){
                        StorageUtil.encode(CommonBean.VIP_KEY, if(it["vip"].toString()=="是") "(VIP)" else "")
                    }
                }
                StorageUtil.encode(CommonBean.LOGIN_USER_OPTIONS, binding.usertypeBs.text.toString())
                StorageUtil.encode(CommonBean.USERNAME_KEY,binding.usernameEt.text.toString().trim())
                postEvent(EventBus.LOGIN_SUCCESS,true)
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_MAIN).navigation()
            }
        }
    }
}


