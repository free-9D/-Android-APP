package com.design.appproject.ui.duanshipin

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
import xyz.doikki.videocontroller.StandardVideoController
import java.util.*
import kotlin.concurrent.timerTask
import com.design.appproject.ext.load
import com.design.appproject.logic.viewmodel.duanshipin.DetailsViewModel
import androidx.activity.viewModels
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.databinding.DuanshipincommonDetailsLayoutBinding
import com.design.appproject.databinding.DuanshipincommonDetailsHeaderLayoutBinding
import com.dylanc.viewbinding.getBinding
import android.widget.RatingBar
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

/**
 * 短视频详情页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_DETAILS_DUANSHIPIN)
class DetailsFragment : BaseBindingFragment<DuanshipincommonDetailsLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0 /*id*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    private val mDetailsViewModel by viewModels<DetailsViewModel>()

    private var mDuanshipinItemBean=DuanshipinItemBean()/*详情内容*/

    private val mCommentsAdatper by lazy {
        CommentsAdatper().apply {
            pageLoadMoreListener {
                mDetailsViewModel.comments("discussduanshipin", mapOf("page" to it.toString(),"limit" to "10","refid" to mId.toString()))
            }
        }
    }

    private val mHeader by lazy {
        LayoutInflater.from(requireActivity()).inflate(R.layout.duanshipincommon_details_header_layout, null,false)
    }
    private val mHeaderBinding:DuanshipincommonDetailsHeaderLayoutBinding by lazy {
        mHeader.getBinding()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun initEvent() {
        setBarTitle("短视频详情页")
        setBarColor("#A2B772","white")
        binding.apply{
            shareBtn.setOnClickListener {
                val wechatIntent = Intent(Intent.ACTION_SEND)
                wechatIntent.type = "text/plain"
                wechatIntent.putExtra(Intent.EXTRA_TEXT, "<a href=\"appproject://share:8888/duanshipin?id=${mId}\">${mDuanshipinItemBean.shangjiaxingming}</a>")
                startActivity(wechatIntent)
            }
            mCommentsAdatper.addHeaderView(mHeader)
            srv.setAdapter(mCommentsAdatper)
            srv.setOnRefreshListener {
                loadData()
                mDetailsViewModel.comments("discussduanshipin", mapOf("page" to "1","limit" to "10","refid" to mId.toString()))
            }
            mHeaderBinding.addCommentBtn.setOnClickListener {
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_DISCUSSDUANSHIPIN)
                .withLong("mRefid",mId)
                .navigation(requireActivity(),101)
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
                mDuanshipinItemBean = info.data
                mHeaderBinding.setInfo()
            }
        }
        mDetailsViewModel.comments("discussduanshipin", mapOf("page" to "1","limit" to "10","refid" to mId.toString()))
        mDetailsViewModel.commentsLiveData.observeKt {
            it.getOrNull()?.let {
                binding.srv.setData(it.data.list,it.data.total)
            }
        }
        mDetailsViewModel.updateLiveData.observeKt {
            it.getOrNull()?.let {
            }

        }
    }

    private fun loadData(){
        mDetailsViewModel.info("duanshipin",mId.toString())
    }


    private fun DuanshipincommonDetailsHeaderLayoutBinding.setInfo(){
        banner.setAdapter(DetailBannerAdapter(mDuanshipinItemBean.fengmian.split(","))).setOnBannerListener { data, position ->
            data.toConversion<String>()?.let {
                val imgList  = arrayListOf<LocalMedia>()
                mDuanshipinItemBean.fengmian.split(",").forEach {imgurl->
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
        biaotiTv.text = "${mDuanshipinItemBean.biaoti}"
        videoview.isFocusable = false
        videoview.isFocusableInTouchMode = false
        videoview.clearFocus()
        videoview.setUrl("${NetRetrofitClient.BASE_URL}"+mDuanshipinItemBean.shipin)
        videoview.setVideoController(StandardVideoController(requireActivity()).apply {
            addDefaultControlComponent("", false)
        })//设置控制器
        videoview.setOnClickListener {
            videoview.start()
            videoFl.foreground = null
        }
        fabushijianTv.text = "${mDuanshipinItemBean.fabushijian}"
        jianjieCw.setHtml(mDuanshipinItemBean.jianjie.trim())
        shangjiazhanghaoTv.text = "${mDuanshipinItemBean.shangjiazhanghao}"
        shangjiaxingmingTv.text = "${mDuanshipinItemBean.shangjiaxingming}"
        discussNumberTv.text = "${mDuanshipinItemBean.discussNumber}"
        initThumbsUp()
        initCollection()

    }



    private fun DuanshipincommonDetailsHeaderLayoutBinding.initCollection(){/*收藏关注*/
        HomeRepository.list<StoreupItemBean>("storeup", mapOf("page" to "1","limit" to "1","refid" to mId.toString(),
            "tablename" to "duanshipin","userid" to Utils.getUserId().toString(),"type" to "41")).observeKt {
            it.getOrNull()?.let {
                collectionIbtn.isSelected = it.data.list.isNotEmpty()/*true为已收藏*/
                collectionIbtn.text = Html.fromHtml(if (collectionIbtn.isSelected) "&#xe668;" else "&#xe870;")
            }
        }
        collectionIbtn.setOnClickListener {
            XPopup.Builder(requireActivity()).asConfirm("提示",if (collectionIbtn.isSelected)"是否取消" else "是否关注") {
                if (collectionIbtn.isSelected){/*取消收藏或关注*/
                    HomeRepository.list<StoreupItemBean>("storeup", mapOf("page" to "1","limit" to "1",
                        "refid" to mId.toString(), "tablename" to "duanshipin", "userid" to Utils.getUserId().toString(), "type" to "41" )).observeKt {
                        it.getOrNull()?.let {
                            if (it.data.list.isNotEmpty()){
                                HomeRepository.delete<StoreupItemBean>("storeup", listOf(it.data.list[0].id!!)).observeKt {
                                    it.getOrNull()?.let {
                                        UserRepository.update("duanshipin",mDuanshipinItemBean).observeKt {
                                            it.getOrNull()?.let {
                                            }
                                         }
                                        "取消成功".showToast()
                                        collectionIbtn.isSelected =false
                                        collectionIbtn.text = Html.fromHtml("&#xe870;")
                                    }
                                }
                            }
                        }
                    }
                }else{/*收藏或关注*/
                    HomeRepository.add<StoreupItemBean>("storeup",StoreupItemBean(
                        userid = Utils.getUserId(),
                        name = mDuanshipinItemBean.biaoti.toString(),
                        picture=mDuanshipinItemBean.fengmian.split(",")[0],
                        refid = mDuanshipinItemBean.id!!,
                        tablename="duanshipin",
                        type="41"                    )).observeKt {
                        it.getOrNull()?.let {
                            UserRepository.update("duanshipin",mDuanshipinItemBean).observeKt {
                                it.getOrNull()?.let {
                                }
                            }
                            "关注成功".showToast()
                            collectionIbtn.isSelected = true
                            collectionIbtn.text = Html.fromHtml("&#xe668;")
                        }
                    }
                }
            }.show()
        }
    }

    private fun DuanshipincommonDetailsHeaderLayoutBinding.initThumbsUp(){/*赞和踩*/
        HomeRepository.list<StoreupItemBean>("storeup", mapOf("page" to "1","limit" to "1","refid" to mId.toString(),
            "tablename" to "duanshipin","userid" to Utils.getUserId().toString(),"type" to "%2%")).observeKt {
            it.getOrNull()?.let {
                if (it.data.list.isNotEmpty()){
                    if (it.data.list[0].type=="21"){
                        zanTv.isSelected = true
                        caiFbl.isVisible = false
                        zanTv.text = "已赞"
                        zanIconTv.text = Html.fromHtml("&#xef82;")
                    }else{
                        caiTv.isSelected = true
                        zanFbl.isVisible = false
                        caiTv.text = "已踩"
                        caiIconTv.text = Html.fromHtml("&#xef80;")
                    }
                }
            }
        }
        zanFbl.setOnClickListener {
            XPopup.Builder(requireActivity()).asConfirm("提示",if (zanTv.isSelected)"是否取消点赞" else "是否点赞") {
                if (zanTv.isSelected){/*取消点赞*/
                    HomeRepository.list<StoreupItemBean>("storeup", mapOf("page" to "1","limit" to "1","refid" to mId.toString(),
                        "tablename" to "duanshipin","userid" to Utils.getUserId().toString(),"type" to "%2%")).observeKt {
                        it.getOrNull()?.let {
                            if (it.data.list.isNotEmpty()){
                                HomeRepository.delete<Any>("storeup",listOf(it.data.list[0].id!!)).observeKt {
                                    it.getOrNull()?.let {
                                        zanTv.isSelected = false
                                        caiFbl.isVisible = true
                                        mDuanshipinItemBean.thumbsupNumber -=1
                                        zanTv.text = "赞:"
                                        zanIconTv.text = Html.fromHtml("&#xef83;")
                                        zanNumTv.text = "${mDuanshipinItemBean.thumbsupNumber}"
                                        mDetailsViewModel.update("duanshipin",mDuanshipinItemBean)
                                        "取消成功".showToast()
                                    }
                                }
                            }
                        }
                    }
                }else{/*点赞*/
                    HomeRepository.add<Any>("storeup", StoreupItemBean(
                        userid = Utils.getUserId(),
                        name = mDuanshipinItemBean.biaoti,
                        picture =mDuanshipinItemBean.fengmian.split(",")[0],
                        refid = mId,
                        tablename="duanshipin",
                        type= "21"
                    )).observeKt {
                        it.getOrNull()?.let {
                            zanTv.isSelected = true
                            caiFbl.isVisible = false
                            mDuanshipinItemBean.thumbsupNumber +=1
                            zanTv.text = "已赞:"
                            zanIconTv.text = Html.fromHtml("&#xef82;")
                            zanNumTv.text = "${mDuanshipinItemBean.thumbsupNumber}"
                            mDetailsViewModel.update("duanshipin",mDuanshipinItemBean)
                            "点赞成功".showToast()
                        }
                    }
                }
            }.show()
        }

        caiFbl.setOnClickListener {
            XPopup.Builder(requireActivity()).asConfirm("提示",if (caiTv.isSelected)"是否取消点踩" else "是否点踩") {
                if (caiTv.isSelected){/*取消点踩*/
                    HomeRepository.list<StoreupItemBean>("storeup", mapOf("page" to "1","limit" to "1","refid" to mId.toString(),
                        "tablename" to "duanshipin","userid" to Utils.getUserId().toString(),"type" to "%2%")).observeKt {
                        it.getOrNull()?.let {
                            if (it.data.list.isNotEmpty()){
                                HomeRepository.delete<Any>("storeup", listOf(it.data.list[0].id!!)).observeKt {
                                    it.getOrNull()?.let {
                                        caiTv.isSelected = false
                                        zanFbl.isVisible = true
                                        mDuanshipinItemBean.crazilyNumber -=1
                                        caiTv.text = "踩:"
                                        caiIconTv.text = Html.fromHtml("&#xef81;")
                                        caiNumTv.text = "${mDuanshipinItemBean.crazilyNumber}"
                                        mDetailsViewModel.update("duanshipin",mDuanshipinItemBean)
                                        "取消成功".showToast()
                                    }
                                }
                            }
                        }
                    }
                }else{/*点踩*/
                    HomeRepository.add<Any>("storeup", StoreupItemBean(
                        userid = Utils.getUserId(),
                        name = mDuanshipinItemBean.biaoti,
                    picture =mDuanshipinItemBean.fengmian.split(",")[0],
                    refid = mId,
                    tablename="duanshipin",
                    type= "22"
                    )).observeKt {
                        it.getOrNull()?.let {
                            caiTv.isSelected = true
                            zanFbl.isVisible = false
                            mDuanshipinItemBean.crazilyNumber +=1
                            caiTv.text = "已踩:"
                            caiIconTv.text = Html.fromHtml("&#xef80;")
                            caiNumTv.text = "${mDuanshipinItemBean.crazilyNumber}"
                            mDetailsViewModel.update("duanshipin",mDuanshipinItemBean)
                            "点踩成功".showToast()
                        }
                    }
                }
            }.show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== AppCompatActivity.RESULT_OK && requestCode==101){
            loadData()
            HomeRepository.list<DiscussduanshipinItemBean>("discussduanshipin", mapOf("page" to "1","limit" to "999","refid" to mId.toString())).observeKt {
                it.getOrNull()?.let {
                    mDuanshipinItemBean.discussNumber=it.data.list.size
                    UserRepository.update("duanshipin",mDuanshipinItemBean).observeKt {
                        it.getOrNull()?.let {
                        }
                    }
                }
            }
            mDetailsViewModel.comments("discussduanshipin", mapOf("page" to "1","limit" to "10","refid" to mId.toString()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mHeaderBinding.videoview.release()
        }catch (e:Exception){}
    }

    override fun onPause() {
        super.onPause()
        mHeaderBinding.videoview.pause()
    }

    override fun onResume() {
        super.onResume()
        mHeaderBinding.videoview.resume()
    }
}