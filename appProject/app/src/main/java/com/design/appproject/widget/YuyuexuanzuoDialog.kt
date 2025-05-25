package com.design.appproject.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.databinding.DialogYuyuexuanzuoLayoutBinding
import com.dylanc.viewbinding.inflate
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.lxj.xpopup.core.BottomPopupView
import com.union.union_basic.ext.showToast
import com.union.union_basic.ext.toConversion
import com.union.union_basic.logger.LoggerManager
import java.text.DateFormat

class YuyuexuanzuoDialog(context: Context) : BottomPopupView(context) {

    lateinit var binding: DialogYuyuexuanzuoLayoutBinding

    var mNextListener: ((String,String,Int) -> Unit)? =null//下一步监听，日期，开始时间，0:选座，1提交
    var opentime:String=""
    var mColumns:Int=0
    var mTotal:Int=0
    override fun addInnerContent() {
        binding = bottomPopupContainer.inflate()
    }

    override fun onCreate() {
        super.onCreate()
        binding.apply {
            seatRv.layoutManager = GridLayoutManager(context,mColumns)
            seatRv.adapter = mSeatListAdapter
            val seatList = mutableListOf<String>()
            (1..mTotal).forEach {
                seatList.add(it.toString())
            }
            mSeatListAdapter.setNewInstance(seatList)
            selectDateTv.setOnClickListener {
                context.toConversion<Activity>()?.let { it1 ->
                    DatePicker(it1).apply {
                        wheelLayout.setRange(
                            DateEntity.today(),
                            DateEntity.target(2050, 12, 31), DateEntity.today())
                        setOnDatePickedListener { year, month, day ->
                            val newMonth = if (month<10){
                                "0$month"
                            }else{
                                month.toString()
                            }
                            val newDay = if (day<10){
                                "0$day"
                            }else{
                                day.toString()
                            }
                            selectDateTv.text = "预约日期:$year-$newMonth-$newDay"
                        }
                    }.show()
                }
            }
            startTv.setOnClickListener {
                if (selectDateTv.text.toString().contains("请选择")){
                    "请先选择日期".showToast()
                    return@setOnClickListener
                }
                context.toConversion<Activity>()?.let { it1 ->
                    OptionPicker(it1).apply {
                        try {
                            val startTime = opentime.split("-")[0].split(":")[0].toInt()
                            val endTime = opentime.split("-")[1].split(":")[0].toInt()
                            val nowDate = selectDateTv.text.toString().split(":")[1]
                            val nowTime = DatimeEntity.now().toString().split(" ")[1].split(":")[0].toInt()
                            val timelist = mutableListOf<String>()
                            for (i in startTime..endTime) {
                                if (TimeUtils.isToday(nowDate, TimeUtils.getSafeDateFormat("yyyy-MM-dd"))){
                                    if (i>nowTime){
                                        if (i<10){
                                            timelist.add("0${i}:00")
                                        }else{
                                            timelist.add("${i}:00")
                                        }
                                    }
                                }else{
                                    if (i<10){
                                        timelist.add("0${i}:00")
                                    }else{
                                        timelist.add("${i}:00")
                                    }
                                }
                            }
                            timelist.removeLast()
                            setData(timelist)
                        }catch (e:Exception){}

                        setOnOptionPickedListener { position, item ->
                            startTv.text = "开始时间:$item"
                            var endTime = item.toString().split(":")[0].toInt()+1
                            if (endTime<10){
                                endTv.text = "结束时间:0${endTime}:00"
                            }else{
                                endTv.text = "结束时间:${endTime}:00"
                            }
                        }
                    }.show()
                }
            }
            preBtn.setOnClickListener {
                if (preBtn.text.toString()=="关闭"){
                    dismiss()
                }else{
                    preBtn.text = "关闭"
                    timeLl.visibility = View.VISIBLE
                    seatRv.visibility = View.GONE
                }
            }
            nextBtn.setOnClickListener {
                if (preBtn.text.toString()=="关闭"){
                    if (selectDateTv.text.toString().contains("请选择")){
                        "请先选择日期".showToast()
                        return@setOnClickListener
                    }
                    if (startTv.text.toString().contains("请选择")){
                        "请先选择预约时间段".showToast()
                        return@setOnClickListener
                    }
                    timeLl.visibility = View.GONE
                    seatRv.visibility = View.VISIBLE
                    preBtn.text = "上一步"
                    mNextListener?.invoke(selectDateTv.text.toString().split(":")[1],startTv.text.toString().split("开始时间:")[1]+"-"+endTv.text.toString().split("结束时间:")[1],0)
                }else{
                    if (newSelected.isNullOrEmpty()){
                        "请先选择座位".showToast()
                        return@setOnClickListener
                    }
                    mNextListener?.invoke(newSelected[0],selectDateTv.text.toString().split(":")[1]+"[]"+startTv.text.toString().split("开始时间:")[1]+"-"+endTv.text.toString().split("结束时间:")[1],1)
                    dismiss()
                }
            }
        }
    }

    fun setSelectNumber(selectNumber:MutableList<String>){
        this.selectNumber = selectNumber
        mSeatListAdapter.notifyDataSetChanged()
    }
    private var selectNumber = mutableListOf<String>()/*已被预定座位*/
    private var newSelected = mutableListOf<String>()/*选择的座位*/

    private val mSeatListAdapter by lazy {
        object: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_seat_layout){
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.getView<ImageView>(R.id.seat_iv).let {
                    it.setColorFilter(if (newSelected.contains(item) ||selectNumber.contains(item)) Color.RED else Color.GRAY)
                }
                holder.setText(R.id.seat_number_tv,item)
            }
        }.apply {
            setOnItemClickListener { adapter, view, position ->
                if (selectNumber.contains(data[position])){
                    "该座位已被预定".showToast()
                }else{
                    if (newSelected.contains((position+1).toString())){
                        newSelected.remove((position+1).toString())
                    }else{
                        newSelected.clear()
                        newSelected.add((position+1).toString())
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}