package com.design.appproject.ui.shopaddress

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.R
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.ActivityAddressListLayoutBinding
import com.design.appproject.logic.viewmodel.shopaddress.AddressModel
import com.design.appproject.utils.ArouterUtils
import com.design.appproject.utils.Utils
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion

/**
 * 地址列表界面
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LIST_ADDRESS)
class AddressListFragment: BaseBindingFragment<ActivityAddressListLayoutBinding>() {

    private val mAddressViewModel by viewModels<AddressModel>()

    private val mAddressListAdapter by lazy {
        AddressListAdapter().apply {
            addChildClickViewIds(R.id.edit_tv,R.id.delete_tv)
            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.edit_tv){/*编辑地址*/
                    ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_DETAILS_ADDRESS, map = mapOf("mId" to data[position].id))
                }else if (view.id == R.id.delete_tv){/*删除地址*/
                    mAddressViewModel.delAddress(data[position].id,position)
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var addressInfo = "${data[position].name} ${data[position].phone}\n${data[position].address}"
                requireActivity().setResult(RESULT_OK,Intent().apply { putExtra("addressInfo",addressInfo) })
                requireActivity().finish()
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            setBarTitle("地址")
            srv.setOnRefreshListener {
                mAddressViewModel.addressList(Utils.getUserId())
            }
            srv.setAdapter(mAddressListAdapter)
            addAddressBtn.setOnClickListener {
                ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_DETAILS_ADDRESS, activity = requireActivity(), requestCode = 101)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode==101){
            mAddressViewModel.addressList(Utils.getUserId())
        }
    }

    override fun initData() {
        super.initData()
        mAddressViewModel.addressList(Utils.getUserId())
        mAddressViewModel.addressListLiveData.observeKt {
            it.getOrNull()?.let {
                binding.srv.setData(it.data.list,it.data.total)
            }
        }
        mAddressViewModel.delAddressLiveData.observeKt {
            it.getOrNull()?.let {
                it.callBackData?.toConversion<Int>()?.let {
                    mAddressListAdapter.removeAt(it)
                }
                "删除成功".showToast()
            }
        }
    }
}