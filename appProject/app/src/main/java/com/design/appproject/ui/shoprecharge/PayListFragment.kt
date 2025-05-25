package com.design.appproject.ui.shoprecharge

import android.app.Activity
import android.content.Intent
import android.widget.RadioButton
import androidx.core.view.children
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.databinding.FragmentPaylistLayoutBinding
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.utils.ArouterUtils
import com.google.gson.internal.LinkedTreeMap
import com.lxj.xpopup.XPopup
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion
import com.union.union_basic.ext.yes
import com.union.union_basic.utils.StorageUtil

/**
 * 充值支付界面
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_PAY_LIST)
class PayListFragment : BaseBindingFragment<FragmentPaylistLayoutBinding>() {

    @JvmField
    @Autowired
    var mTitle: String = ""/*标题*/

    override fun initEvent() {
        setBarTitle(mTitle)
        binding.apply {
            ensureBtn.setOnClickListener {
                ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_PAY_RECHARGE,
                    mapOf("mType" to payRg.children.indexOfFirst {
                        it.toConversion<RadioButton>()?.isChecked ?: false
                    }),activity = requireActivity(),101
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK &&requestCode==101){
            requireActivity().finish()
        }
    }
}