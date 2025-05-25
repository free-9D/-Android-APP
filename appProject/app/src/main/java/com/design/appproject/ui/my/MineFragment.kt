package com.design.appproject.ui.my

import android.text.Html
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.design.appproject.widget.MyTextView
import androidx.core.view.setPadding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.design.appproject.R
import com.design.appproject.base.BaseBindingFragment
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.base.CommonBean
import com.design.appproject.base.EventBus
import com.design.appproject.bean.Child
import com.design.appproject.bean.roleMenusList
import com.design.appproject.databinding.FragmentMineLayoutBinding
import com.design.appproject.ext.load
import com.design.appproject.ext.observeEvent
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.ui.MainActivity
import com.design.appproject.utils.ArouterUtils
import com.design.appproject.utils.Utils
import com.design.appproject.widget.ExpandableView
import com.google.gson.internal.LinkedTreeMap
import com.union.union_basic.ext.*
import com.union.union_basic.utils.StorageUtil
import androidx.core.view.children
import com.design.appproject.ui.ChatFriendListDialog
import com.design.appproject.ui.ChangePassWordDialog
import com.lxj.xpopup.XPopup

/**
 * 我的fragment
 * */
@Route(path = CommonArouteApi.PATH_FRAGMENT_MY)
class MineFragment: BaseBindingFragment<FragmentMineLayoutBinding>() {

    private val childNameList = listOf("yifahuodingdan","yituikuandingdan","yiquxiaodingdan","weizhifudingdan"
        ,"yiwanchengdingdan","exampaper","examquestion" ,"orders")

    private var mUserBean = LinkedTreeMap<String, Any>()

    override fun initEvent() {
        binding.apply {
            srl.setOnRefreshListener {
                initData()
            }
            infoLl.setOnClickListener {
                if (Utils.isLogin()){
                    ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_USER_INFO).navigation()
                }else{
                    ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_LOGIN).navigation()
                }
            }
            settingTv.setOnClickListener {
                infoLl.callOnClick()
            }
            observeEvent<Boolean>(EventBus.LOGIN_SUCCESS) {
                initData()
            }
            observeEvent<Boolean>(EventBus.USER_INFO_UPDATED) {
                initData()
                it.no {/*退出登录*/
                    setData(LinkedTreeMap())
                }
            }
            initView()
        }
    }

    private fun FragmentMineLayoutBinding.initView(){
        noLoginFl.setOnClickListener{
            ARouter.getInstance().build(com.design.appproject.base.CommonArouteApi.PATH_ACTIVITY_LOGIN).navigation()
        }
        val orderList = listOf("全部","未支付","已支付","已完成","已取消")
        ordersListFbl.children.forEachIndexed { index, view ->
            view.setOnClickListener {
                if (Utils.isLogin()){
                    ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_LIST_ORDERS, map = mapOf("mTitle" to orderList[index]))
                }else{
                    ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_LOGIN).navigation()
                }
            }
        }
    }

    override fun onResume() {
        if (isFragmentViewInit && Utils.isLogin()){
            initData()
        }
        super.onResume()
    }

    private fun creatMenuItem(child: Child,uniCode:String ="",isSecond:Boolean =false,parent: ViewGroup) = LayoutInflater.from(context).inflate(if (isSecond)R.layout.item_menu2_layout else R.layout.item_menu_layout,parent,false).apply {
        findViewById<MyTextView>(R.id.icon_start_tv).let {
                it.isVisible = true
                it.text = Html.fromHtml(uniCode)
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            findViewById<MyTextView>(R.id.content_tv).let {
                it.text = child.menu
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            setOnClickListener {
                if (Utils.isLogin()){
                    if(child.tableName == "chat"){
                    ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_CHAT_LIST)
                    return@setOnClickListener
                }
                    if(child.tableName == "orders"){/*订单*/
                        ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_LIST_ORDERS,map = mapOf("mTitle" to child.menuJump,"mIsBack" to true))
                        return@setOnClickListener
                    }
                    if(child.tableName=="forget"){
                        var mima = ""
                        if ("yonghu" == CommonBean.tableName){
                            mima = "yonghuzhanghao"
                        }
                        if ("yonghu" == CommonBean.tableName){
                            mima = "mima"
                        }
                        if ("shangjia" == CommonBean.tableName){
                            mima = "shangjiazhanghao"
                        }
                        if ("shangjia" == CommonBean.tableName){
                            mima = "mima"
                        }
                        XPopup.Builder(requireActivity())
                            .asCustom(ChangePassWordDialog(context,mUserBean[mima].toString()).apply {
                                this.mUpdateListener={pw->
                                    mUserBean[mima]= pw
                                    UserRepository.update(CommonBean.tableName,mUserBean).observeKt {
                                        it.getOrNull()?.let {
                                            "密码更新成功，重新登录生效".showToast()
                                            this.dismiss()
                                        }
                                    }
                                }
                            })
                            .show()
                        return@setOnClickListener
                    }
                    ArouterUtils.startFragment("/ui/fragment/${child.tableName.replace("_","")}/list",map = mapOf("menuJump" to child.menuJump,"mIsBack" to true))
                return@setOnClickListener
            }
        }
    }
    val menuViewList = mutableListOf<View>()

    override fun initData() {
        super.initData()
        binding.noLoginFl.isVisible = !Utils.isLogin()
        binding.infoLl.isVisible = Utils.isLogin()
        val options = StorageUtil.decodeString(CommonBean.LOGIN_USER_OPTIONS)
        binding.menulistLl.removeAllViews()
        roleMenusList.forEach {
            if (options == it.roleName){
                var evCount = 0
                it.backMenu.forEachIndexed { index, menuItem ->
                    val childList = menuItem.child.filter {!childNameList.contains(it.tableName)|| (it.tableName=="exampaper" && it.menuJump=="12")}
                    if (childList.size==1){
                        val itemView = creatMenuItem(childList[0],menuItem.unicode?:"", parent = binding.menulistLl)
                        binding.menulistLl.addView(itemView)
                        menuViewList.add(itemView)
                    }else if (childList.size>1) {/*有二级列表*/
                        val oneView= LayoutInflater.from(context).inflate(R.layout.item_menu_layout,binding.menulistLl,false)
                        oneView.findViewById<MyTextView>(R.id.icon_start_tv).let {
                            it.text = Html.fromHtml(menuItem.unicode?:"")
                            it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
                        }
                        oneView.findViewById<MyTextView>(R.id.content_tv).let {
                            it.text = menuItem.menu
                            it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
                        }
                        oneView.findViewById<MyTextView>(R.id.icon_end_tv).let {
                            it.isVisible =true
                        }
//                        binding.menulistLl.addView(oneView,index+evCount)
                        binding.menulistLl.addView(oneView)
                        menuViewList.add(oneView)
                        val ev = ExpandableView(context)
                        oneView.setOnClickListener {
                            ev.showOrHide()
                        }
                        childList.forEach {
                            val itemView =creatMenuItem(it, isSecond = true, parent = ev)
                            ev.addContentView(itemView)
                        }
//                        binding.menulistLl.addView(ev,index+evCount+1)
                        binding.menulistLl.addView(ev)
                        evCount+=1
                        menuViewList.add(ev)
                    }
                }

                val child = Child("&#xe8d4;", listOf(),"修改密码", menuJump = "",tableName="forget")
                val itemView = creatMenuItem(child, uniCode = "&#xe8d4;", parent = binding.menulistLl)
                binding.menulistLl.addView(itemView)
                menuViewList.add(itemView)
            }
        }

        val rechargeView = LayoutInflater.from(context).inflate(R.layout.item_menu_layout,binding.menulistLl,false).apply {
            findViewById<MyTextView>(R.id.icon_start_tv).let {
                it.isVisible = true
                it.text = Html.fromHtml("&#xe638;")
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            findViewById<MyTextView>(R.id.content_tv).let {
                it.text = "用户充值"
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            findViewById<MyTextView>(R.id.icon_end_tv).let {
                it.isVisible =true
                it.text = Html.fromHtml("&#xe602;")
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            setOnClickListener {
                if (Utils.isLogin()) {
                    ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_PAY_LIST, mapOf("mTitle" to "请选择充值方式", "mIsBack" to true))
                }else{
                    ARouter.getInstance().build(com.design.appproject.base.CommonArouteApi.PATH_ACTIVITY_LOGIN).navigation()
                }
            }
        }
        binding.menulistLl.addView(rechargeView)
        val chatListView = LayoutInflater.from(context).inflate(R.layout.item_menu_layout,binding.menulistLl,false).apply {
            findViewById<MyTextView>(R.id.icon_start_tv).let {
                it.isVisible = true
                it.text = Html.fromHtml("&#xedb2;")
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            findViewById<MyTextView>(R.id.content_tv).let {
                it.text = "聊天记录"
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            findViewById<MyTextView>(R.id.icon_end_tv).let {
                it.isVisible =true
                it.text = Html.fromHtml("&#xe602;")
                it.typeface= ResourcesCompat.getFont(requireContext(),R.font.iconfont)
            }
            setOnClickListener {
                if (Utils.isLogin()) {
                    XPopup.Builder(context).asCustom(ChatFriendListDialog(context,2)).show()
                }else{
                    ARouter.getInstance().build(com.design.appproject.base.CommonArouteApi.PATH_ACTIVITY_LOGIN).navigation()
                }
            }
        }
        binding.menulistLl.addView(chatListView)
        chatListView.isVisible = Utils.isLogin()
        UserRepository.session<Any>().observeKt(errorBlock = {binding.srl.isRefreshing =false}) {
            binding.srl.isRefreshing =false
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    binding.setData(it)
                }
            }
        }
    }

    private fun FragmentMineLayoutBinding.setData(session:LinkedTreeMap<String, Any>){
        mUserBean= session
        activity?.let {avatarboxIfv.load(it,Utils.getAvatarUrl(), errorResId = R.mipmap.upload) }
        avatarboxTv.text =Utils.getUserName()+if (session["vip"].toString()=="是")"(VIP)" else ""
        usersLl.isVisible = "users" == CommonBean.tableName
        usersMoneyTv.text = "余额:${session["money"].toString()}"
        yonghuLl.isVisible = "yonghu" == CommonBean.tableName
        yonghuMoneyTv.text = "余额:${session["money"].toString()}"
        shangjiaLl.isVisible = "shangjia" == CommonBean.tableName
        shangjiaMoneyTv.text = "余额:${session["money"].toString()}"
    }
}