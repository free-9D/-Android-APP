package com.design.appproject.logic.viewmodel.shoporders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.bean.shoporders.OrdersItemBean
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.union.union_basic.ext.isNotNullOrEmpty

/**
 * 订单
 */
class OrdersModel: ViewModel() {

    private val ordersListData = MutableLiveData<List<String>>()

    val ordersListLiveData = Transformations.switchMap(ordersListData) { request ->
        ordersListData.value?.let {
            val map = mutableMapOf(
                "page" to it[0],
                "userid" to it[1],
                "limit" to "20",
                "sort" to "addtime",
                "order" to "desc"
            )
            if (it[2].isNotNullOrEmpty()&&it[2]!="全部"){
                map["status"] = it[2]
            }
            HomeRepository.list<OrdersItemBean>("orders", map)
        }
    }

    /*订单列表*/
    fun ordersList(page:Int,userid:Long,status: String) {
        ordersListData.value = listOf(page.toString(),userid.toString(),status)
    }

    private val updateOrderData = MutableLiveData<OrdersItemBean>()

    val updateOrderLiveData = Transformations.switchMap(updateOrderData) { request ->
        updateOrderData.value?.let {
            UserRepository.update("orders", it)
        }
    }

    /*更新订单*/
    fun updateOrder(order:OrdersItemBean) {
        updateOrderData.value = order
    }

    private val delOrderData = MutableLiveData<Long>()

    val delOrderLiveData = Transformations.switchMap(delOrderData) { request ->
        delOrderData.value?.let {
            HomeRepository.delete<OrdersItemBean>("orders", listOf(it))
        }
    }

    /*删除订单*/
    fun delOrder(id:Long) {
        delOrderData.value = id
    }

}