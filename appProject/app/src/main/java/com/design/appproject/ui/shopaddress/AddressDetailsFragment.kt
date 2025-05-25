package com.design.appproject.ui.shopaddress

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.RegexUtils
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.bean.shopaddress.AddressItemBean
import com.design.appproject.databinding.ActivityAddressDetailsLayoutBinding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.union.union_basic.ext.no
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.yes
import com.union.union_basic.permission.PermissionUtil

/**
 * 地址详情界面
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_DETAILS_ADDRESS)
class AddressDetailsFragment: BaseBindingFragment<ActivityAddressDetailsLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0 /*地址id*/

    private var mAddressItemBean = AddressItemBean()

    override fun initEvent() {
        setBarTitle(if (mId>0) "编辑收货地址" else "新增收货地址")
        binding.apply {
            switchLl.setOnClickListener {
                defaultSc.isChecked = !defaultSc.isChecked
            }
            submitBtn.setOnClickListener {
                verify().yes {
                    if (mId>0){
                        UserRepository.update("address",mAddressItemBean).observeKt {
                            it.getOrNull()?.let {
                                "操作成功".showToast()
                                requireActivity().setResult(RESULT_OK)
                                requireActivity().finish()
                            }
                        }
                    }else{
                        HomeRepository.add<AddressItemBean>("address",mAddressItemBean).observeKt {
                            it.getOrNull()?.let {
                                "操作成功".showToast()
                                requireActivity().setResult(RESULT_OK)
                                requireActivity().finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ActivityAddressDetailsLayoutBinding.verify():Boolean{
        if (consigneeEt.text.toString().isNullOrEmpty()){
            "请填写收货人姓名".showToast()
            return false
        }
        mAddressItemBean.name = consigneeEt.text.toString()
        RegexUtils.isMobileExact(phoneEt.text.toString()).no {
            "请输入正确的手机号码".showToast()
            return false
        }
        mAddressItemBean.phone= phoneEt.text.toString()
        if (addressEt.text.toString().isNullOrEmpty()){
            "请输入地址".showToast()
            return false
        }
        mAddressItemBean.address = addressEt.text.toString()
        mAddressItemBean.isdefault = if (defaultSc.isChecked) "是" else "否"
        return true
    }

    override fun initData() {
        super.initData()
        if (mId>0){
            HomeRepository.info<AddressItemBean>("address",mId).observeKt {
                it.getOrNull()?.data?.let {
                    binding.apply {
                        mAddressItemBean = it
                        consigneeEt.setText(it.name)
                        phoneEt.setText(it.phone)
                        addressEt.setText(it.address)
                        defaultSc.isChecked = it.isdefault=="是"
                    }
                }
            }
        }
    }

}