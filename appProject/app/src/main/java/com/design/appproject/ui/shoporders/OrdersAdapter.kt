package com.design.appproject.ui.shoporders

import android.graphics.Color
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.shoporders.OrdersItemBean
import com.design.appproject.ext.load
import com.design.appproject.ext.twoDecimal
import com.design.appproject.widget.LoadMoreAdapter
import com.union.union_basic.ext.isNotNullOrEmpty
/**
 *订单列表
 */
class OrdersAdapter: LoadMoreAdapter<OrdersItemBean>(R.layout.item_orders_list_layout) {

    override fun convert(holder: BaseViewHolder, item: OrdersItemBean) {
        holder.getView<TextView>(R.id.status_tv).let {
            it.text = item.status
            it.setTextColor(statusColor(item.status))
        }
        holder.setText(R.id.orderid_tv,item.orderid.toString())
        holder.getView<ImageView>(R.id.goods_iv).load(context,item.picture)
        holder.setText(R.id.goods_name_tv,item.goodname)
        holder.setText(R.id.count_price_tv,"${item.buynumber} x ${if (item.type=="2") "${item.price.twoDecimal}积分" else item.price.twoDecimal}")
        holder.setText(R.id.consignee_tv,"收货人：${item.consignee}")
        holder.setText(R.id.tel_tv,"电话：${item.tel}")
        holder.setText(R.id.consignee_tv,"地址：${item.address}")
        holder.setText(R.id.addtime_tv,"下单时间：${item.addtime}")
        holder.setText(R.id.remark_tv,"备注：${item.remark}")
        holder.setText(R.id.total_tv,if (item.type=="2") "${item.total}积分" else item.total.twoDecimal+"元")

        holder.getView<Button>(R.id.left_btn).isVisible=false
        holder.getView<Button>(R.id.right_btn).isVisible=false
        when(item.status){
            "未支付"->{
                holder.getView<Button>(R.id.left_btn).isVisible=true
                holder.getView<Button>(R.id.right_btn).isVisible=true
                holder.getView<Button>(R.id.left_btn).text = "支付"
                holder.getView<Button>(R.id.right_btn).text = "取消订单"
            }
            "已支付"->{
                holder.getView<Button>(R.id.right_btn).isVisible=true
                holder.getView<Button>(R.id.right_btn).text = "退款"
            }
            "已完成"->{
                holder.getView<Button>(R.id.left_btn).isVisible=true
                holder.getView<Button>(R.id.left_btn).text = "退货"
            }
            "已发货"->{
                holder.getView<Button>(R.id.left_btn).isVisible=true
                holder.getView<Button>(R.id.left_btn).text = "确认收货"
            }
        }
        holder.getView<Button>(R.id.delete_btn).isVisible=false
        if (item.status == "已取消"||item.status == "已完成"||item.status == "已退款"){
            holder.getView<Button>(R.id.delete_btn).isVisible=true
        }

    }

    private fun statusColor(status:String) =when(status){
        "未支付"->Color.RED
        "已支付" ->Color.GREEN
        "已发货"->Color.RED
        "已完成"->Color.BLUE
        "已取消"->Color.GRAY
        "已退款"->Color.GRAY
        else->Color.GRAY
    }
}