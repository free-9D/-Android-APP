package com.design.appproject.ui

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ColorUtils
import com.design.appproject.R
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.bean.*
import com.design.appproject.bean.config.*
import com.design.appproject.databinding.FragmentIndexLayoutBinding
import com.design.appproject.ext.load
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.viewmodel.HomeViewModel
import com.design.appproject.utils.ArouterUtils
import com.design.appproject.utils.Utils
import com.design.appproject.ext.UrlPrefix
import com.design.appproject.widget.BottomSpinner
import com.youth.banner.indicator.CircleIndicator
import android.text.Html
import androidx.core.view.isVisible
import android.view.LayoutInflater
import com.design.appproject.widget.MyFlexBoxLayout
import com.qmuiteam.qmui.layout.QMUILinearLayout
import com.union.union_basic.ext.*
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.Banner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.blankj.utilcode.util.RegexUtils
import android.graphics.Color
import com.design.appproject.base.EventBus
import com.design.appproject.ext.observeEvent
import com.lxj.xpopup.XPopup
/**
 * 首页fragment
 * */
@Route(path = CommonArouteApi.PATH_FRAGMENT_INDEX)
class IndexFragment : BaseBindingFragment<FragmentIndexLayoutBinding>() {

    private val mHomeViewModel by viewModels<HomeViewModel>()

    override fun initEvent() {
        binding.apply {
            homeSrl.setOnRefreshListener {
                initHomeView()
                GlobalScope.launch {
                    delay(2000) /*延时2秒*/
                    homeSrl.isRefreshing =false
                }
            }
            initHomeView()
            observeEvent<Boolean>(EventBus.LOGIN_SUCCESS){
                initHomeView()
            }
        }
    }

    private fun FragmentIndexLayoutBinding.initHomeView(){/*初始化首页内容*/
        initBanner()
        initMenu()
        initProductView()
    }
    private fun FragmentIndexLayoutBinding.initProductView() {
        shangpinxinxiMoreTv.setOnClickListener {/*查看更多*/
            ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_LIST_SHANGPINXINXI)
        }
        HomeRepository.list<ShangpinxinxiItemBean>("shangpinxinxi",
            mapOf(
                "page" to "1",
                "limit" to "4"
            )
        ).observeKt {
                it.getOrNull()?.let {
                    shangpinxinxiListLl.removeAllViews()
                        it.data.list.forEachIndexed { index,data->
                            val itemView =creatshangpinxinxiItemView(data,shangpinxinxiListLl)
                            shangpinxinxiListLl.addView(itemView)
                            itemView.setOnClickListener {
                                ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_DETAILS_SHANGPINXINXI,map = mapOf("mId" to data.id!!))
                            }
                        }
            }
        }
    }

    private fun creatshangpinxinxiItemView(data:ShangpinxinxiItemBean,parent: ViewGroup) =LayoutInflater.from(context).inflate(R.layout.shangpinxinxi_item_index_list_layout,parent,false).apply{
        findViewById<TextView>(R.id.shangpinmingcheng_title_tv).text = "商品名称:"+ data.shangpinmingcheng.toString()
        findViewById<ImageView>(R.id.shangpintupian_iv).load(context,data.shangpintupian.split(",")[0], needPrefix = !(data.shangpintupian.startsWith("http")))
        findViewById<TextView>(R.id.price_price_tv).text = "￥${data.price}"
    }
    /**轮播图*/
    private fun FragmentIndexLayoutBinding.initBanner() {
        HomeRepository.list<ConfigItemBean>("config", mapOf("page" to "1", "limit" to "3")).observeKt{
            it.getOrNull()?.let {
                banner.setAdapter(object :
                    BannerImageAdapter<ConfigItemBean>(it.data.list.filter { it.name.contains("swiper") }) {
                    override fun onBindView(
                        holder: BannerImageHolder,
                        data: ConfigItemBean,
                        position: Int,
                        size: Int
                    ) {
                        activity?.let { holder.imageView.load(it, data.value.split(",")[0], radius = 5.dp) }
                    }
                }).setOnBannerListener { data, position ->
                    data.toConversion<ConfigItemBean>()?.let {
                        if (it.url.startsWith("http")){
                            ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_WEBVIEW).withString("mUrl",it.url).navigation()
                        }else{
                            ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_WEBVIEW).withString("mUrl",
                                UrlPrefix.URL_PREFIX+it.url).navigation()
                        }
                    }
                }
            }
        }
    }
    /**菜单*/
    private fun FragmentIndexLayoutBinding.initMenu() {
        val menuList = mutableListOf<MenuBean>()
        menuGl.removeAllViews()
        roleMenusList.filter { it.tableName == CommonBean.tableName }.forEach {/*筛选可查看的菜单*/
            it.frontMenu.forEach {
                val menuBean = MenuBean(
                    child = it.child.filter {child->child.buttons.contains("查看")},
                    menu = it.menu?:"", fontClass = it.fontClass?:"", unicode = it.unicode?:"")
                menuList.add(menuBean)
            }
        }
        menuList.forEachIndexed { index, menu ->
            if (menu.child.size>0 && !listOf("yifahuodingdan","yituikuandingdan","yiquxiaodingdan","weizhifudingdan","yizhifudingdan","yiwanchengdingdan").contains(menu.child[0].tableName)){
                val itemView = creatMenuItemView(menu)
                menuGl.addView(itemView)
            }
        }
    }

    private fun creatMenuItemView(menu: MenuBean) = LayoutInflater.from(context).inflate(R.layout.item_index_menu_layout,binding.menuGl,false).apply{
        findViewById<TextView>(R.id.menu_title_tv).text = menu.menu.split("列表")[0]
        findViewById<TextView>(R.id.menu_icon_tv).text = Html.fromHtml(menu.unicode?:"")
        setOnClickListener {
            if (menu.child.size>1){//二级菜单
                XPopup.Builder(requireActivity()).asBottomList("",
                    menu.child.map {it.menu}.toTypedArray()
                ) { position, text ->
                    ArouterUtils.startFragment("/ui/fragment/${menu.child[position].tableName.replace("_","")}/list")
                }.show()
            }else{// 一级菜单
                ArouterUtils.startFragment("/ui/fragment/${menu.child[0].tableName.replace("_","")}/list")
            }
        }
    }

}