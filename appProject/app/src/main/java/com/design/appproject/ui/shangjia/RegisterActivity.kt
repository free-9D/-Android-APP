package com.design.appproject.ui.shangjia
import com.union.union_basic.ext.*
import android.text.InputType
import android.annotation.SuppressLint
import android.text.method.PasswordTransformationMethod
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.RegexUtils
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.ShangjiaactivityRegisterLayoutBinding
import com.design.appproject.ext.load
import androidx.core.view.children
import com.design.appproject.widget.BottomSpinner
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.blankj.utilcode.util.TimeUtils
import com.design.appproject.bean.ShangjiaItemBean
import com.design.appproject.logic.viewmodel.shangjia.RegisterViewModel
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import java.text.SimpleDateFormat
import com.union.union_basic.image.selector.SmartPictureSelector
import java.io.File
import java.util.*

/**
 * Shangjia注册界面
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_REGISTER_SHANGJIA)
class RegisterActivity : BaseBindingActivity<ShangjiaactivityRegisterLayoutBinding>() {

    private val mRegisterViewModel by viewModels<RegisterViewModel>()

    /**注册请求参数*/
    var reqisterBean = ShangjiaItemBean()

    @SuppressLint("SimpleDateFormat")
    override fun initEvent() {
        binding.apply {
            setBarTitle("注册")
            setBarColor("#A2B772","white")
            initView()
        }
    }

    /**初始化控件状态*/
    private fun ShangjiaactivityRegisterLayoutBinding.initView() {
        titleIv.load(this@RegisterActivity,"http://clfile.zggen.cn/20250321/0f82b55b94b64fb39b403233b48cb02f.jpg",needPrefix=false)
        registerLl.findViewWithTag<EditText>("shangjiazhanghao")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.shangjiazhanghao = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<EditText>("mima")?.let {
            it.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            it.transformationMethod= PasswordTransformationMethod.getInstance()
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.mima = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<EditText>("shangjiaxingming")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.shangjiaxingming = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<LinearLayout>("touxiang").let {
            it.setOnClickListener {
                SmartPictureSelector.openPicture(this@RegisterActivity) {
                    val path = it[0]
                    showLoading("上传中...")
                    mRegisterViewModel.upload(File(path), "touxiang")
                }
            }
        }
        registerLl.findViewWithTag<BottomSpinner>("xingbie")
            .let { spinner ->

                spinner.setOptions(
                    "男,女".split(","),
                    "请选择性别",
                    listener = object : BottomSpinner.OnItemSelectedListener {
                        override fun onItemSelected(position: Int, content: String) {
                            super.onItemSelected(position, content)
                            spinner.text = content
                            reqisterBean.xingbie = content
                        }
                    })
            }
        registerLl.findViewWithTag<EditText>("lianxidianhua")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.lianxidianhua = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<BottomSpinner>("zizhileixing")
            .let { spinner ->

                spinner.setOptions(
                    "实人认证,企业认证,采购认证,实力认证".split(","),
                    "请选择资质类型",
                    listener = object : BottomSpinner.OnItemSelectedListener {
                        override fun onItemSelected(position: Int, content: String) {
                            super.onItemSelected(position, content)
                            spinner.text = content
                            reqisterBean.zizhileixing = content
                        }
                    })
            }
        registerLl.findViewWithTag<LinearLayout>("zizhizhengming").let {
            it.setOnClickListener {
                SmartPictureSelector.openPicture(this@RegisterActivity) {
                    val path = it[0]
                    showLoading("上传中...")
                    mRegisterViewModel.upload(File(path), "zizhizhengming")
                }
            }
        }
        registerLl.findViewWithTag<EditText>("pquestion")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.pquestion = et?.toString() ?: ""
                }
            })
        }
        registerLl.findViewWithTag<EditText>("panswer")?.let {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(et: Editable?) {
                    reqisterBean.panswer = et?.toString() ?: ""
                }
            })
        }
        registerBtn.setOnClickListener {
            verify().yes {
                showLoading("注册中...")
                mRegisterViewModel.register("shangjia", reqisterBean)
            }
        }
    }

    /**验证*/
    private fun verify(): Boolean {
        binding.registerLl.findViewWithTag<EditText>("shangjiazhanghao").let {
            if (it.text.toString().isNullOrEmpty()) {
                "商家账号不能为空".showToast()
                return@verify false
            } else {
                reqisterBean.shangjiazhanghao = it.text.toString()
            }
        }
        binding.registerLl.findViewWithTag<EditText>("mima").let {
            if (it.text.toString().isNullOrEmpty()) {
                "密码不能为空".showToast()
                return@verify false
            } else {
                reqisterBean.mima = it.text.toString()
            }
        }
        if ((reqisterBean.mima != binding.registerLl.findViewWithTag<EditText>("mima2").text.toString())) {
            "密码两次密码输入不一致".showToast()
            return false
        }
        binding.registerLl.findViewWithTag<EditText>("shangjiaxingming").let {
            if (it.text.toString().isNullOrEmpty()) {
                "商家姓名不能为空".showToast()
                return@verify false
            } else {
                reqisterBean.shangjiaxingming = it.text.toString()
            }
        }
        reqisterBean.touxiang.isNullOrEmpty().yes{
            "头像不能为空".showToast()
            return@verify false
        }
        if (!RegexUtils.isMobileExact(reqisterBean.lianxidianhua)) {
            "联系电话应输入手机格式".showToast()
            return false
        }
        return true
    }

    override fun initData() {
        super.initData()
        mRegisterViewModel.optionLiveData.observeKt {
            it.getOrNull()?.let {
                it.callBackData?.toConversion<Pair<String, Boolean>>()?.let { callData ->
                    val spinnerView =
                        binding.registerLl.findViewWithTag<BottomSpinner>(callData.first)
                    spinnerView.setOptions(it.data, spinnerView.hint.toString(), callData.second)
                    spinnerView.dialogShow()
                }
            }
        }
        mRegisterViewModel.uploadLiveData.observeKt {
            it.getOrNull()?.let {
                it.callBackData?.let { tag ->
                    val imageView =
                        binding.registerLl.findViewWithTag<LinearLayout>(tag).getChildAt(1)
                            .toConversion<ImageView>()
                    imageView?.load(this, "file/"+it.file)
                    if (tag.toString()=="touxiang") {
                        reqisterBean.touxiang = "file/" + it.file
                    }
                    if (tag.toString()=="zizhizhengming") {
                        reqisterBean.zizhizhengming = "file/" + it.file
                    }
                }
            }
        }

        mRegisterViewModel.registerLiveData.observeKt {
            it.getOrNull()?.let {
                "注册成功".showToast()
                finish()
            }
        }
    }
}