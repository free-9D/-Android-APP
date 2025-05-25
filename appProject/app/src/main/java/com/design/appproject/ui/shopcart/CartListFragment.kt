package com.design.appproject.ui.shopcart

import android.content.Intent
import android.widget.CheckBox
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.design.appproject.R
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.EventBus
import com.design.appproject.bean.shoporders.OrdersItemBean
import com.design.appproject.databinding.FragmentCartListLayoutBinding
import com.design.appproject.ext.observeEvent
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.viewmodel.shopcart.CartModel
import com.design.appproject.utils.Utils
import com.design.appproject.ext.twoDecimal
import com.google.gson.internal.LinkedTreeMap
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion
import com.union.union_basic.ext.yes
import androidx.appcompat.app.AppCompatActivity


/**
 * 购物车
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LIST_CART)
class CartListFragment : BaseBindingFragment<FragmentCartListLayoutBinding>() {

    @JvmField
    @Autowired
    var mHasBack: Boolean = true /*是否有返回按钮*/

    private val mCartModel by viewModels<CartModel>()

    private val mCartListAdapter by lazy {
        CartListAdapter().apply {
            addChildClickViewIds(R.id.item_add_btn, R.id.reduce_btn,R.id.item_cart_cb)
            setOnItemClickListener { adapter, view, position ->
                data[position].isSelected = !data[position].isSelected
                updateSelectedInfo()
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_cart_cb->{
                        data[position].isSelected = view.toConversion<CheckBox>()?.isChecked == true
                        updateSelectedInfo()
                    }
                    R.id.item_add_btn -> {
                        HomeRepository.info<Any>(data[position].tablename, data[position].goodid)
                            .observeKt {
                                it.getOrNull()?.let {
                                    it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                                        var onelimittimes =
                                            it["onelimittimes"].toString().toDoubleOrNull()/*限量件数*/
                                        var alllimittimes =
                                            it["alllimittimes"].toString().toDoubleOrNull()/*库存*/
                                        if (onelimittimes != null && data[position].buynumber + 1 > onelimittimes!!) {
                                            "每人单次只能购买${onelimittimes!!.toInt()}件".showToast()
                                        } else if (alllimittimes != null && data[position].buynumber + 1 > alllimittimes!!) {
                                            "库存不足".showToast()
                                        } else {
                                            data[position].buynumber++
                                            data[position].isSelected = true
                                            updateSelectedInfo()
                                            notifyDataSetChanged()
                                            mCartModel.updateCart(data[position])
                                        }
                                    }
                                }
                            }

                    }
                    R.id.reduce_btn -> {
                        data[position].buynumber--
                        if (data[position].buynumber <= 0) {
                            /**购买数量为0时从购物车移除*/
                            mCartModel.delcart(data[position].id, position)
                        } else {
                            updateSelectedInfo()
                            notifyDataSetChanged()
                            mCartModel.updateCart(data[position])
                        }
                    }
                }
            }
        }
    }
    var ordersList = mutableListOf<OrdersItemBean>()

    override fun initEvent() {
        binding.apply {
            setBarTitle("购物车",mHasBack)
            cartSrv.setOnRefreshListener {
                mCartModel.cartList(Utils.getUserId())
            }
            observeEvent<Boolean>(EventBus.UPDATE_CART_LIST) {
                mCartModel.cartList(Utils.getUserId())
            }
            cartSrv.setAdapter(mCartListAdapter)
            allCb.setOnClickListener {
                allCb.isChecked!=allCb.isChecked
                allCb.isChecked.yes { /*全选*/
                    mCartListAdapter.data.forEach { it.isSelected = true }
                }.otherwise { /*全不选*/
                    mCartListAdapter.data.forEach { it.isSelected = false }
                }
                mCartListAdapter.notifyDataSetChanged()
                updateSelectedInfo()
            }
            orderBtn.setOnClickListener {
                if (mCartListAdapter.data.count { it.isSelected }<=0){
                    "请选择下单的商品".showToast()
                }else{
                    ordersList.clear()
                    mCartListAdapter.data.filter { it.isSelected }.forEach {
                        ordersList.add(OrdersItemBean(
                            id = it.id,
                            goodid = it.goodid,
                            price = it.price,
                            buynumber = it.buynumber,
                            goodname = it.goodname,
                            tablename = it.tablename,
                            picture = it.picture,
                            shangjiazhanghao=it.shangjiazhanghao,
                        ))
                    }
                    ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ORDERS_CONFIRM)
                        .withInt("mType",1)
                        .withObject("mOrderList", ordersList).navigation()}
            }
        }
    }

    private fun updateSelectedInfo() {
        /**更新购物车选择信息*/
        val selectedCartList = mCartListAdapter.data.filter { it.isSelected }
        binding.allCb.isChecked = selectedCartList.size >= mCartListAdapter.data.size
        binding.cartCountTv.text = "已选:(${selectedCartList.size})"
        binding.cartPriceTv.text = "￥" + selectedCartList.sumOf { it.price * it.buynumber.toDouble() }.twoDecimal
    }

    override fun initData() {
        super.initData()
        mCartModel.cartList(Utils.getUserId())
        mCartModel.cartListLiveData.observeKt(errorBlock = {
            binding.cartSrv.isRefreshing = false
        }) {
            it.getOrNull()?.let {
                it.data.list.forEach { it.isSelected = true }
                binding.cartSrv.setData(it.data.list, it.data.total)
                updateSelectedInfo()
            }
        }
        mCartModel.updateCartLiveData.observeKt { }
        mCartModel.delcartLiveData.observeKt {
            it.getOrNull()?.let {
                updateSelectedInfo()
                it.callBackData?.toConversion<Int>()?.let {
                    mCartListAdapter.removeAt(it)
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode==101){//支付成功，清空购物车商品
            HomeRepository.delete<Any>("cart",ordersList.map { it.id }.toList()).observeForever {  }
            binding.cartSrv.reload()
        }
    }
}