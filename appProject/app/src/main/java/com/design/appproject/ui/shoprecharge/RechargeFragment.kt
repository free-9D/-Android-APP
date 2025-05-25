package com.design.appproject.ui.shoprecharge

import android.app.Activity
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.databinding.FragmentRechargeLayoutBinding
import com.design.appproject.logic.repository.UserRepository
import com.google.gson.internal.LinkedTreeMap
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion
import com.union.union_basic.ext.yes

/**
 * 充值界面
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_PAY_RECHARGE)
class RechargeFragment : BaseBindingFragment<FragmentRechargeLayoutBinding>() {

    @JvmField
    @Autowired
    var mType: Int = 0/*支付类型*/

    override fun initEvent() {
        setBarTitle("用户充值")
        binding.apply {
            rechargeBtn.setOnClickListener {
                if ((moneyEt.text.toString().toDoubleOrNull() ?: 0.0) <= 0.0) {
                    "请输入正确的充值数目".showToast()
                    return@setOnClickListener
                }
//                if (mType==1){/*支付宝*/
//                    HomeRepository.alipay(moneyEt.text.toString().toDouble(),"充值").observeKt {
//                        it.getOrNull()?.let {
//                            AliPayHelper(requireActivity(),it.data,paySuccessListener = object : AliPayHelper.PaySuccessListener{
//                                override fun paySuccess() {
//
//                                }
//                            }).aliPay()
//                        }
//                    }
//                }

                UserRepository.session<Any>().observeKt {
                    it.getOrNull()?.let {
                        it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                            it["money"] = it["money"].toString().toDouble()+moneyEt.text.toString().toDouble()
                            UserRepository.update(CommonBean.tableName,it).observeKt {
                                it.getOrNull()?.let {
                                    rechargeSuccess()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun rechargeSuccess(){
        binding.apply {
            moneyEt.isVisible =false
            rechargeTv.isVisible =true
            rechargeBtn.text = "返回"
            rechargeBtn.setOnClickListener {
                requireActivity().setResult(Activity.RESULT_OK)
                requireActivity().finish()
            }
        }
    }
}