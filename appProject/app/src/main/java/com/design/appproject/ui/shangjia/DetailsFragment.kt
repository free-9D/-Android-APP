package com.design.appproject.ui.shangjia

import com.design.appproject.ui.ShhfDialog
import com.design.appproject.logic.repository.UserRepository
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import android.annotation.SuppressLint
import com.union.union_basic.utils.StorageUtil
import com.design.appproject.utils.ArouterUtils
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.design.appproject.base.*
import com.design.appproject.ext.postEvent
import com.google.gson.internal.LinkedTreeMap
import android.media.MediaPlayer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import android.view.Gravity
import android.view.ViewGroup
import com.design.appproject.widget.DetailBannerAdapter
import android.widget.*
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.ColorUtils
import com.design.appproject.R
import com.luck.picture.lib.basic.PictureSelector
import com.qmuiteam.qmui.layout.QMUILinearLayout
import com.union.union_basic.ext.*
import androidx.core.view.setMargins
import com.union.union_basic.image.selector.GlideEngine
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.XPopup
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.constant.TimeConstants
import kotlinx.coroutines.*
import com.union.union_basic.network.DownloadListener
import com.union.union_basic.network.DownloadUtil
import java.io.File
import androidx.core.view.setPadding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.utils.Utils
import java.util.*
import kotlin.concurrent.timerTask
import com.design.appproject.ext.load
import com.design.appproject.logic.viewmodel.shangjia.DetailsViewModel
import androidx.activity.viewModels
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.databinding.ShangjiacommonDetailsLayoutBinding
import com.design.appproject.bean.*
import com.design.appproject.ui.CommentsAdatper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.view.isVisible
import android.text.Html
import com.design.appproject.widget.MyTextView
import com.design.appproject.widget.MyFlexBoxLayout
import com.design.appproject.widget.MyImageView
import android.view.ContextThemeWrapper
import com.google.android.flexbox.FlexWrap
import com.union.union_basic.image.loader.GlideLoader.load
import com.design.appproject.ui.ChatMessageDialog

/**
 * 商家详情页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_DETAILS_SHANGJIA)
class DetailsFragment : BaseBindingFragment<ShangjiacommonDetailsLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0 /*id*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    private val mDetailsViewModel by viewModels<DetailsViewModel>()

    private var mShangjiaItemBean=ShangjiaItemBean()/*详情内容*/


    @SuppressLint("SuspiciousIndentation")
    override fun initEvent() {
        setBarTitle("商家详情页")
        setBarColor("#A2B772","white")
        binding.apply{
            srv.setOnRefreshListener {
                loadData()
            }
            notFriendBtn.setOnClickListener {
                if (mShangjiaItemBean.id!! == Utils.getUserId()){
                    "不能给自己发信息！".showToast()
                    return@setOnClickListener
                }
                XPopup.Builder(requireActivity())
                    .autoOpenSoftInput(false)
                    .asCustom(ChatMessageDialog(requireActivity(),mShangjiaItemBean.touxiang,mShangjiaItemBean.id!!,"shangjia",mShangjiaItemBean.shangjiazhanghao))
                    .show()
            }

            mIsBack.yes {
                sfshBtn.isVisible = Utils.isAuthBack("shangjia","审核")
            }.otherwise {
                sfshBtn.isVisible = Utils.isAuthFront("shangjia","审核")
            }
            sfshBtn.setOnClickListener {
                XPopup.Builder(context).asCustom(context?.let { it1 ->
                    ShhfDialog(it1).apply {
                        mUpdateListener={status,content->
                            when(status){
                                "通过"-> mShangjiaItemBean.sfsh = "是"
                                "不通过"->mShangjiaItemBean.sfsh = "否"
                                else->mShangjiaItemBean.sfsh = status
                            }
                            mShangjiaItemBean.shhf=content
                            mDetailsViewModel.update("shujijiaohuanht",mShangjiaItemBean,"shhf")
                        }
                    }
                }).show()
            }
    }
    }

    override fun initData() {
        super.initData()
        showLoading()
        loadData()
        mDetailsViewModel.infoLiveData.observeKt(errorBlock = {binding.srv.isRefreshing =false}) {
            it.getOrNull()?.let { info->
                binding.srv.isRefreshing =false
                mShangjiaItemBean = info.data
                 binding.setInfo()
            }
        }
        mDetailsViewModel.updateLiveData.observeKt {
            it.getOrNull()?.let {
                if (it.callBackData=="shhf"){
                    "审核成功".showToast()
                }
            }

        }
    }

    private fun loadData(){
        mDetailsViewModel.info("shangjia",mId.toString())
    }


    private fun ShangjiacommonDetailsLayoutBinding.setInfo(){
        shangjiazhanghaoTv.text = "${mShangjiaItemBean.shangjiazhanghao}"
        shangjiaxingmingTv.text = "${mShangjiaItemBean.shangjiaxingming}"
        touxiangIfv.load(requireActivity(),"${mShangjiaItemBean.touxiang}")
        xingbieTv.text = "${mShangjiaItemBean.xingbie}"
        lianxidianhuaTv.text = "${mShangjiaItemBean.lianxidianhua}"
        zizhileixingTv.text = "${mShangjiaItemBean.zizhileixing}"
        zizhizhengmingIfv.load(requireActivity(),"${mShangjiaItemBean.zizhizhengming}")
        pquestionTv.text = "${mShangjiaItemBean.pquestion}"
        panswerTv.text = "${mShangjiaItemBean.panswer}"
        var sfshStatus = if(mShangjiaItemBean.sfsh =="是"){
            "通过"
        }else if(mShangjiaItemBean.sfsh =="否"){
            "不通过"
        }else{
            "待审核"
        }
        shztTv.isVisible = mIsBack
        sfshFbl.isVisible = mIsBack
        sfshContentTv.isVisible = mIsBack
        sfshContentFbl.isVisible = mIsBack
        shztTv.text = "${sfshStatus}"
        sfshContentTv.text =mShangjiaItemBean.shhf

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode==101){
            loadData()
        }
    }


}