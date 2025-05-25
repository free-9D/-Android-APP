package com.design.appproject.ui.shoporders

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.FragmentLogisticsLayoutBinding

/**
 * 物流界面
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LOGISTICS)
class LogisticsFragment: BaseBindingFragment<FragmentLogisticsLayoutBinding>() {

    @JvmField
    @Autowired
    var logistics: String = "" /*物流信息*/

    override fun initEvent() {
        binding.apply {
            setBarTitle("物流信息")
            webview.setHtml(logistics)
        }
    }
}