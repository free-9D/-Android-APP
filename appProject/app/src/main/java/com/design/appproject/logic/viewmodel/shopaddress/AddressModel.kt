package com.design.appproject.logic.viewmodel.shopaddress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.bean.shopaddress.AddressItemBean
import com.design.appproject.logic.repository.HomeRepository

/**
 * 地址相关viewmodel
 */
class AddressModel: ViewModel() {

    private val addressListData = MutableLiveData<Long>()

    val addressListLiveData = Transformations.switchMap(addressListData) { request ->
        addressListData.value?.let {
            HomeRepository.list<AddressItemBean>("address", mapOf("userid" to it.toString()))
        }
    }

    /**地址列表*/
    fun addressList(userId:Long) {
        addressListData.value = userId
    }

    private val delAddressData = MutableLiveData<List<Any>>()

    val delAddressLiveData = Transformations.switchMap(delAddressData) { request ->
        delAddressData.value?.let {
            HomeRepository.delete<Any>("address", listOf(it[0]),it[1] as Int)
        }
    }

    /**删除地址*/
    fun delAddress(id:Long,position:Int) {
        delAddressData.value = listOf(id,position)
    }
}