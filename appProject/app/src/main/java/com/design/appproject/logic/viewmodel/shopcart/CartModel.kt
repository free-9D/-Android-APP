package com.design.appproject.logic.viewmodel.shopcart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.bean.shopcart.CartItemBean
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository

/**
 * 购物车
 */
class CartModel: ViewModel() {

    private val cartListData = MutableLiveData<Long>()

    val cartListLiveData = Transformations.switchMap(cartListData) { request ->
        cartListData.value?.let {
            HomeRepository.list<CartItemBean>("cart", mapOf("userid" to it.toString()))
        }
    }

    /**购物车列表*/
    fun cartList(userId:Long) {
        cartListData.value = userId
    }

    private val delcartData = MutableLiveData<List<Any>>()

    val delcartLiveData = Transformations.switchMap(delcartData) { request ->
        delcartData.value?.let {
            HomeRepository.delete<Any>("cart", listOf(it[0]),it[1] as Int)
        }
    }

    /*删除购物车*/
    fun delcart(id:Long,position:Int) {
        delcartData.value = listOf(id,position)
    }

    private val updateCartData = MutableLiveData<CartItemBean>()

    val updateCartLiveData = Transformations.switchMap(updateCartData) { request ->
        updateCartData.value?.let {
            UserRepository.update("cart",it)
        }
    }

    /*更新购物车*/
    fun updateCart(cartItemBean:CartItemBean) {
        updateCartData.value = cartItemBean
    }
}