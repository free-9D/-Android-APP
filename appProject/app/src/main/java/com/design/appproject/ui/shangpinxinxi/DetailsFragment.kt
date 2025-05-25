package com.design.appproject.ui.shangpinxinxi

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
import com.design.appproject.logic.viewmodel.shangpinxinxi.DetailsViewModel
import androidx.activity.viewModels
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.databinding.ShangpinxinxicommonDetailsLayoutBinding
import com.design.appproject.bean.*
import com.design.appproject.bean.shopcart.CartItemBean
import com.design.appproject.bean.shoporders.OrdersItemBean
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
import com.design.appproject.bean.ShangjiaItemBean

/**
 * 商品信息详情页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_DETAILS_SHANGPINXINXI)
class DetailsFragment : BaseBindingFragment<ShangpinxinxicommonDetailsLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0 /*id*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    private val mDetailsViewModel by viewModels<DetailsViewModel>()

    private var mShangpinxinxiItemBean=ShangpinxinxiItemBean()/*详情内容*/


    @SuppressLint("SuspiciousIndentation")
    override fun initEvent() {
        setBarTitle("商品信息详情页")
        setBarColor("#A2B772","white")
        binding.apply{
            srv.setOnRefreshListener {
                loadData()
            }
            notFriendBtn.setOnClickListener {
                if (mShangpinxinxiItemBean.shangjiazhanghao == Utils.getUserName()){
                    "不能给自己发信息！".showToast()
                    return@setOnClickListener
                }
                HomeRepository.query<ShangjiaItemBean>("shangjia", mapOf("shangjiazhanghao" to mShangpinxinxiItemBean.shangjiazhanghao)).observeKt {
                    it.getOrNull()?.let {
                        XPopup.Builder(requireActivity())
                            .autoOpenSoftInput(false)
                            .asCustom(ChatMessageDialog(requireActivity(),it.data.touxiang,it.data.id!!,"shangjia",it.data.shangjiazhanghao))
                            .show()
                    }
                }
            }

            addCartBtn.setOnClickListener {
                HomeRepository.list<CartItemBean>("cart", mapOf("userid" to Utils.getUserId().toString())).observeKt {
                    it.getOrNull()?.let {
                        val cartItem = it.data.list.find { it.goodid == mId }
                        if (cartItem != null) {
                            "该商品已添加到购物车".showToast()
                        } else {
                            HomeRepository.add<Any>("cart", CartItemBean(
                                tablename = "shangpinxinxi",
                                goodid=mId,
                                goodname=mShangpinxinxiItemBean.shangpinmingcheng,
                                shangjiazhanghao=mShangpinxinxiItemBean.shangjiazhanghao,
                                picture=mShangpinxinxiItemBean.shangpintupian.split(",")[0],
                                buynumber=1,
                                userid=Utils.getUserId(),
                                price=mShangpinxinxiItemBean.price,
                            )).observeKt {
                                it.getOrNull()?.let {
                                    postEvent(EventBus.UPDATE_CART_LIST,true)
                                    "添加到购物车成功".showToast()
                                }
                            }
                        }
                    }
                }
            }
            virtualPayBtn.setOnClickListener {
              val ordersItemBean = OrdersItemBean(
                    tablename="shangpinxinxi",
                    goodid = mId,
                    goodname=mShangpinxinxiItemBean.shangpinmingcheng,
                    shangjiazhanghao=mShangpinxinxiItemBean.shangjiazhanghao,
                    picture= binding.banner.adapter.getData(0).toString(),
                    buynumber=1,
                    price=mShangpinxinxiItemBean.price,
                )
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ORDERS_CONFIRM)
                    .withInt("mType",1)
                    .withObject("mOrderList", listOf(ordersItemBean)).navigation()
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
                mShangpinxinxiItemBean = info.data
                 binding.setInfo()
            }
        }
        mDetailsViewModel.updateLiveData.observeKt {
            it.getOrNull()?.let {
            }

        }
    }

    private fun loadData(){
        mDetailsViewModel.info("shangpinxinxi",mId.toString())
    }


    private fun ShangpinxinxicommonDetailsLayoutBinding.setInfo(){
        shangpinmingchengTv.text = "${mShangpinxinxiItemBean.shangpinmingcheng}"
        shangpinfenleiTv.text = "${mShangpinxinxiItemBean.shangpinfenlei}"
        shangpinpinpaiTv.text = "${mShangpinxinxiItemBean.shangpinpinpai}"
        shangpinguigeTv.text = "${mShangpinxinxiItemBean.shangpinguige}"
        banner.setAdapter(DetailBannerAdapter(mShangpinxinxiItemBean.shangpintupian.split(","))).setOnBannerListener { data, position ->
            data.toConversion<String>()?.let {
                val imgList  = arrayListOf<LocalMedia>()
                mShangpinxinxiItemBean.shangpintupian.split(",").forEach {imgurl->
                    imgList.add(LocalMedia().apply {
                        path = NetRetrofitClient.BASE_URL+imgurl
                    })
                }
                PictureSelector.create(this@DetailsFragment)
                    .openPreview()
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        override fun onPreviewDelete(position: Int) {
                        }

                        override fun onLongPressDownload(media: LocalMedia?): Boolean {
                            return true
                        }
                    }).startActivityPreview(position, true, imgList)
            }
        }
        shangpinjianjieTv.text = "${mShangpinxinxiItemBean.shangpinjianjie}"
        shangpinxiangqingTv.text = "${mShangpinxinxiItemBean.shangpinxiangqing}"
        priceTv.text = "${mShangpinxinxiItemBean.price}"
        shangjiazhanghaoTv.text = "${mShangpinxinxiItemBean.shangjiazhanghao}"
        shangjiaxingmingTv.text = "${mShangpinxinxiItemBean.shangjiaxingming}"

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode==101){
            loadData()
        }
    }


}