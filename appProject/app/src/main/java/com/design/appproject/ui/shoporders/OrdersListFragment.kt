package com.design.appproject.ui.shoporders

import android.widget.Button
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.design.appproject.R
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.utils.ArouterUtils
import com.design.appproject.bean.shoporders.OrdersItemBean
import com.design.appproject.databinding.BaseListLayoutBinding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.logic.viewmodel.shoporders.OrdersModel
import com.google.gson.internal.LinkedTreeMap
import com.lxj.xpopup.XPopup
import com.design.appproject.utils.Utils
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion
/**
 * 订单列表
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LIST_ORDER)
class OrdersListFragment: BaseBindingFragment<BaseListLayoutBinding>() {

    @JvmField
    @Autowired
    var status: String = "" /*订单状态*/

    private val mOrdersModel by viewModels<OrdersModel>()

    override fun initEvent() {
        binding.apply {
            srv.setOnRefreshListener {
                mOrdersModel.ordersList(1,Utils.getUserId(),status)
            }

            srv.setAdapter(OrdersAdapter().apply {
                addChildClickViewIds(R.id.left_btn,R.id.right_btn,R.id.delete_btn)
                pageLoadMoreListener {
                    mOrdersModel.ordersList(it,Utils.getUserId(),status)
                }

                setOnItemChildClickListener { adapter, view, position ->
                    if (view.id ==R.id.left_btn || view.id==R.id.right_btn){
                        options(view.toConversion<Button>()?.text.toString()?:"",data[position])
                    }else if (view.id==R.id.delete_btn){
                        XPopup.Builder(context).asConfirm("提示","是否确认删除") {
                            mOrdersModel.delOrder(data[position].id)
                        }.show()
                    }
                }
            })
        }
    }

    private fun options(status:String,item:OrdersItemBean){
        when(status){
            "支付"->{
                XPopup.Builder(context).asConfirm("提示","是否支付订单") {
                    if (item.type=="1"){/*现金*/
                        if (mUserBean["money"].toString().toDouble()<item.total){
                            "余额不足，请先充值".showToast()
                            return@asConfirm
                        }
                        mUserBean["money"] = mUserBean["money"].toString().toDouble()-item.total
                        mUserBean["jf"] = mUserBean["money"].toString().toDouble()+item.total
                        UserRepository.update(CommonBean.tableName,mUserBean).observeForever {
                            "支付成功".showToast()
                        }
                    }else if(item.type=="2"){/*积分*/
                        if (mUserBean["jf"].toString().toDouble()<item.total){
                            "积分不足，兑换商品失败".showToast()
                            return@asConfirm
                        }
                        mUserBean["jf"] = mUserBean["jf"].toString().toDouble()-item.total
                        UserRepository.update(CommonBean.tableName,mUserBean).observeForever {
                            "兑换成功".showToast()
                        }
                    }
                    /*修改订单状态*/
                    item.status = "已支付"
                    mOrdersModel.updateOrder(item)
                }.show()
            }
            "取消订单"->{
                XPopup.Builder(context).asConfirm("提示","是否取消订单") {
                    item.status = "已取消"
                    mOrdersModel.updateOrder(item)
                }.show()
            }
            "退款"->{
                XPopup.Builder(context).asConfirm("提示","是否确认退款") {
                    item.status = "已退款"
                    mOrdersModel.updateOrder(item)
                    /*退回余额或积分*/
                    if (item.type!="2"){
                        mUserBean["money"] = mUserBean["money"].toString().toDouble()+item.total
                        if (mUserBean["jf"]?.toString()?:"" != "") {
                            mUserBean["jf"] = mUserBean["jf"].toString().toDouble()-item.total
                        }
                    }else{
                        mUserBean["jf"] = mUserBean["jf"].toString().toDouble()+item.total
                    }
                    UserRepository.update(CommonBean.tableName,mUserBean).observeForever { }
                }.show()
            }
            "退货"->{
                XPopup.Builder(context).asConfirm("提示","是否确认退货") {
                    item.status = "已退款"
                    mOrdersModel.updateOrder(item)
                    /*退回余额或积分*/
                    if (item.type!="1"){
                        mUserBean["money"] = mUserBean["money"].toString().toDouble()+item.total
                        mUserBean["jf"] = mUserBean["jf"].toString().toDouble()-item.total
                    }else{
                        mUserBean["jf"] = mUserBean["jf"].toString().toDouble()+item.total
                    }
                    UserRepository.update(CommonBean.tableName,mUserBean).observeForever { }
                }.show()
            }
            "确认收货"->{
                XPopup.Builder(context).asConfirm("提示","是否确认收货") {
                    item.status = "已完成"
                    mOrdersModel.updateOrder(item)
                }.show()
            }
        }
    }

    lateinit var mUserBean:LinkedTreeMap<String, Any>/*当前用户数据*/

    override fun initData() {
        super.initData()
        mOrdersModel.ordersList(1,Utils.getUserId(),status)
        mOrdersModel.ordersListLiveData.observeKt(errorBlock = {binding.srv.isRefreshing =false}) {
            it.getOrNull()?.let {
                binding.srv.setData(it.data.list,it.data.total)
            }
        }

        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mUserBean = it
                }
            }
        }

        mOrdersModel.updateOrderLiveData.observeKt {
            it.getOrNull()?.let {
                binding.srv.reload()
                mOrdersModel.ordersList(1,Utils.getUserId(),status)
            }
        }
        mOrdersModel.delOrderLiveData.observeKt {
            it.getOrNull()?.let {
                "删除成功".showToast()
                binding.srv.reload()
                mOrdersModel.ordersList(1,Utils.getUserId(),status)
            }
        }
    }
}