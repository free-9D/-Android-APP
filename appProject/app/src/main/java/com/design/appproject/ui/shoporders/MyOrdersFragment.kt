package com.design.appproject.ui.shoporders

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.FragmentShopOrdersLayoutBinding
import com.design.appproject.ext.adapterKTX
import com.design.appproject.widget.MagicIndexCommonNavigator

/**
 * 我的订单界面
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LIST_ORDERS)
class MyOrdersFragment : BaseBindingFragment<FragmentShopOrdersLayoutBinding>() {

    @JvmField
    @Autowired
    var mTitle:String = "全部"

    private val mFragments by lazy {
        var fragmentList = mutableListOf<Fragment>()
        mTitles.forEach {
            fragmentList.add(ARouter.getInstance().build(CommonArouteApi.PATH_FRAGMENT_LIST_ORDER)
                .withString("status",it).navigation() as Fragment)
        }
        fragmentList
    }

    private val mTitles by lazy {
        listOf(
        "全部", "未支付","已支付", "已发货" ,"已完成", "已取消", "已退款"
        )
    }

    override fun initEvent() {
        binding.apply {
            setBarTitle("我的订单")
            indexTab.init(viewPager2.apply {
                adapterKTX(requireActivity(), mFragments)
                offscreenPageLimit = mFragments.size
            },mTitles,customNavigator = MagicIndexCommonNavigator(requireActivity()).apply {
                mNormalSize = 16f
                mIsAdjustMode = false
            })
            viewPager2.currentItem = mTitles.indexOf(mTitle)
        }
    }
}