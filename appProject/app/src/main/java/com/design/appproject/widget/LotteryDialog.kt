package com.design.appproject.widget

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import com.design.appproject.databinding.DialogLotteryLayoutBinding
import com.design.appproject.utils.Utils
import com.dylanc.viewbinding.inflate
import com.lxj.xpopup.core.CenterPopupView
import com.union.union_basic.logger.LoggerManager

class LotteryDialog(context: Context) : CenterPopupView(context) {

    lateinit var binding: DialogLotteryLayoutBinding

    var mSpinWheelListener: ((String) -> Unit)? =null
    var prizes:MutableList<Pair<String,Int>> = mutableListOf()
    override fun addInnerContent() {
        binding = centerPopupContainer.inflate()
    }

    override fun onCreate() {
        super.onCreate()
        binding.apply {
            // 设置奖品（颜色，文字，权重）
            val items = prizes.mapIndexed { index, pair ->
                LotteryWheelView.LotteryItem(Utils.getRandomLightColor(index), pair.first, pair.second)

            }
            lotteryWheel.setItems(items)

            btnStart.setOnClickListener {
                if (lotteryWheel.isSpinning) return@setOnClickListener
                val resultIndex = lotteryWheel.getRandomResultIndex()
                btnStart.isEnabled = false
                lotteryWheel.spinWheel(resultIndex) {
                    btnStart.isEnabled = true
                    //这里可以处理抽奖结果
                    val result = items[resultIndex]
                    mSpinWheelListener?.invoke(result.text)
                    AlertDialog.Builder(context)
                        .setTitle("抽奖结果")
                        .setMessage("恭喜你获得：${result.text}")
                        .setCancelable(false)
                        .setPositiveButton("确定"
                        ) { p0, p1 ->
                            dismiss()
                            this@LotteryDialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}