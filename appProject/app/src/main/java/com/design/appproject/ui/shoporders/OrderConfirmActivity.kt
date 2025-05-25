package com.design.appproject.ui.shoporders

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.view.setPadding
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.base.EventBus
import com.design.appproject.bean.shoporders.OrdersItemBean
import com.design.appproject.databinding.ActivityOrderConfirmLayoutBinding
import com.design.appproject.ext.load
import com.design.appproject.ext.twoDecimal
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.utils.Utils
import com.google.gson.internal.LinkedTreeMap
import com.lxj.xpopup.XPopup
import com.union.union_basic.ext.dp
import com.union.union_basic.ext.foregroundColorSpan
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import com.design.appproject.ext.postEvent
import com.design.appproject.utils.ArouterUtils
/**
 * 订单确认界面
 * */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ORDERS_CONFIRM)
class OrderConfirmActivity:BaseBindingActivity<ActivityOrderConfirmLayoutBinding>() {

    @JvmField
    @Autowired
    var mOrderList: List<OrdersItemBean> = listOf()

    @JvmField
    @Autowired
    var mType:Int = 1/*1:余额支付；2：积分支付：3：拼团支付,4沙箱支付*/


    lateinit var mUserBean:LinkedTreeMap<String, Any>/*当前用户数据*/

    var orderStatus = "已支付"
    override fun initEvent() {
        binding.apply {
            setBarTitle("订单确认")
            var priceTotal = 0.0
            ordersListLl.removeAllViews()
            var subTitle  = ""
            mOrderList.forEach {
                subTitle += it.goodname
                priceTotal += it.price *it.buynumber
                ordersListLl.addView(creatOrderView(it))
            }
            countTv.text = "￥"+priceTotal.twoDecimal
            addressLl.setOnClickListener {/*地址*/
                ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_LIST_ADDRESS, activity = this@OrderConfirmActivity, requestCode = 101)
            }
            ensureBtn.setOnClickListener {
                if (addressTv.text.toString().isNullOrEmpty()){
                    "请选择地址".showToast()
                    return@setOnClickListener
                }
                XPopup.Builder(this@OrderConfirmActivity).asConfirm("提示","是否确认支付") {
                    showLoading()
                    when(mType){
                        1->{
                            if (priceTotal>mUserBean["money"]?.toConversion<Double>() ?: 0.0){
                                "余额不足，请先充值".showToast()
                                orderStatus = "未支付"
                            }
                            orderVerify()
                        }
                        2->{
                            if (priceTotal>(mUserBean["jf"]?.toConversion<Double>() ?: 0.0)){
                                "积分不足，兑换商品失败".showToast()
                                return@asConfirm
                            }
                            orderVerify()
                        }
                    }
                }.show()
            }
        }
    }

    val orderFinishLatch = CountDownLatch(mOrderList.size)


    private fun orderVerify(){
        mOrderList.forEach { order->
            addOrder(order)
        }
        thread {
            orderFinishLatch.await(10,TimeUnit.SECONDS)
            runOnUiThread {
                if (orderStatus=="已支付"){
                    "支付成功".showToast()
                    /*减少余额增加积分*/
                    if (mType==1){
                        mUserBean["money"] = mUserBean["money"].toString().toDouble()-binding.countTv.text.toString().substring(1).toDouble()
                    }
                    if (mType==2){
                        if (mUserBean["jf"].toString().toDoubleOrNull()!=null){
                            mUserBean["jf"]=mUserBean["jf"].toString().toDouble()-binding.countTv.text.toString().substring(1).toDouble()
                        }
                    }else{
                        if (mUserBean["jf"].toString().toDoubleOrNull()!=null){
                            mUserBean["jf"]=mUserBean["jf"].toString().toDouble()+binding.countTv.text.toString().substring(1).toDouble()
                        }
                    }
                    UserRepository.update(CommonBean.tableName,mUserBean).observeKt {
                        it.getOrNull()?.let {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun addOrder(ordersItemBean: OrdersItemBean) {
        ordersItemBean.orderid = Utils.genTradeNo()
        ordersItemBean.userid = Utils.getUserId()
        ordersItemBean.type = mType.toString()
        ordersItemBean.total = ordersItemBean.price * ordersItemBean.buynumber
        ordersItemBean.discounttotal = ordersItemBean.price * ordersItemBean.buynumber
        ordersItemBean.remark = binding.remarkEt.text.toString()
        val cartAddress = binding.addressTv.text.toString().split(" ")
        ordersItemBean.address = cartAddress[1].split("\n")[1]
        ordersItemBean.tel = cartAddress[1].split("\n")[0]
        ordersItemBean.consignee = cartAddress[0]
        ordersItemBean.status = orderStatus
        HomeRepository.add<OrdersItemBean>("orders", ordersItemBean).observeKt {
            it.getOrNull()?.let {
                orderFinishLatch.countDown()
            }
        }
    }

    private fun creatOrderView(ordersItemBean:OrdersItemBean)=LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        setPadding(10.dp)
        addView(ImageFilterView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            load(context,ordersItemBean.picture)
        },80.dp,80.dp)
        addView(TextView(context).apply {
            gravity = Gravity.END
            val orderContent = "${ordersItemBean.goodname}\nx${ordersItemBean.buynumber}  ￥${ordersItemBean.price}"
            text = orderContent.foregroundColorSpan(orderContent.length- "￥${ordersItemBean.price}".length..orderContent.length,Color.RED)
        },LinearLayout.LayoutParams(0,-1,1f))
    }

    override fun initData() {
        super.initData()
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mUserBean = it
                }
            }
        }
        HomeRepository.defaultAddress(Utils.getUserId()).observeKt {
            it.getOrNull()?.data?.let {
                binding.addressTv.text = "${it.name} ${it.phone}\n${it.address}"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode==101){
            binding.addressTv.text = data?.getStringExtra("addressInfo")
        }
    }
}