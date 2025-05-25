package com.design.appproject.ui

import android.content.Context
import com.design.appproject.databinding.DialogChangePasswordLayoutBinding
import com.design.appproject.databinding.DialogShhfLayoutBinding
import com.design.appproject.utils.ArouterUtils
import com.dylanc.viewbinding.inflate
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.CenterPopupView
import com.union.union_basic.ext.showToast

class ShhfDialog(context: Context) : CenterPopupView(context) {

    lateinit var binding: DialogShhfLayoutBinding

    var mUpdateListener: ((String,String) -> Unit)? =null

    override fun addInnerContent() {
        binding = centerPopupContainer.inflate()
    }

    override fun onCreate() {
        super.onCreate()
        binding.apply {
            tvTitle.setOnClickListener {
                XPopup.Builder(context).asBottomList("",
                    arrayListOf("待审核","通过","不通过").toTypedArray()
                ) { position, text ->
                    tvTitle.text = text
                }.show()
            }
            tvCancel.setOnClickListener { dismiss() }
            tvConfirm.setOnClickListener {
                if (etInput.text.toString().isNullOrEmpty()){
                    "请输入审核内容！".showToast()
                    return@setOnClickListener
                }
                if (tvTitle.text.toString().isNullOrEmpty()){
                    "请选择审核状态！".showToast()
                    return@setOnClickListener
                }
                mUpdateListener?.invoke(tvTitle.text.toString(),etInput.text.toString())
                dismiss()
            }
        }
    }
}