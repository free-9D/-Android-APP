package com.design.appproject.widget

// LotteryWheelView.kt
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.union.union_basic.logger.LoggerManager
import kotlin.math.cos
import kotlin.math.sin

class LotteryWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 配置参数
    private companion object {
        const val FULL_ROTATION = 360f
        const val BASE_DURATION = 1500L  // 基础动画时长
        const val EXTRA_SPINS = 1        // 额外旋转圈数
    }

    private val items = mutableListOf<LotteryItem>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var rotationAngle = 0f
    private var currentAnimator: ValueAnimator? = null
    var isSpinning = false

    data class LotteryItem(
        val color: Int,
        val text: String,
        val weight: Int
    )

    fun setItems(newItems: List<LotteryItem>) {
        items.clear()
        items.addAll(newItems)
        invalidate()
    }

    fun spinWheel(resultIndex: Int, completion: () -> Unit) {
        if (items.isEmpty() || isSpinning) return

        cancelCurrentAnimation()
        isSpinning = true

        val itemAngle = FULL_ROTATION / items.size
        val targetCenterAngle = resultIndex * itemAngle + itemAngle / 2

        // 计算需要旋转的总角度
        val currentRotation = rotationAngle % FULL_ROTATION
        val targetRotation = EXTRA_SPINS * FULL_ROTATION + (FULL_ROTATION - targetCenterAngle) - currentRotation
        LoggerManager.d("targetCenterAngle:"+targetCenterAngle)
        LoggerManager.d("currentRotation:"+currentRotation)
        LoggerManager.d("targetRotation:"+targetRotation)
        // 动态计算动画时长（圈数越多时间越长）
        val duration = BASE_DURATION + (EXTRA_SPINS * 200L)

        currentAnimator = ValueAnimator.ofFloat(0f, targetRotation).apply {
            this.duration = duration
            interpolator = LinearInterpolator()

            addUpdateListener {
                rotationAngle += it.animatedValue as Float
                rotationAngle %= FULL_ROTATION
                invalidate()
            }

            addListener(onEnd = {
                rotationAngle = FULL_ROTATION - targetCenterAngle-itemAngle
                invalidate()
                isSpinning = false
                completion()
            })

            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f * 0.9f

        drawRotatingWheel(canvas, centerX, centerY, radius)
        drawStaticPointer(centerX, centerY, radius, canvas)
    }

    private fun drawRotatingWheel(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)

        val itemAngle = FULL_ROTATION / items.size
        items.forEachIndexed { index, item ->
            // 绘制扇形
            paint.color = item.color
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                index * itemAngle,
                itemAngle,
                true,
                paint
            )

            // 优化文字绘制性能
            drawTextLabel(canvas, centerX, centerY, radius, index, itemAngle, item.text)
        }
        canvas.restore()
    }

    private fun drawTextLabel(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        index: Int,
        itemAngle: Float,
        text: String
    ) {
        val textRadius = radius * 0.7f
        val angle = Math.toRadians((index * itemAngle + itemAngle / 2).toDouble())

        val x = centerX + cos(angle) * textRadius
        val y = centerY + sin(angle) * textRadius + textPaint.textSize / 3

        canvas.save()
        canvas.rotate(
            (index * itemAngle + itemAngle / 2 + 90).toFloat(),
            x.toFloat(),
            y.toFloat()
        )
        canvas.drawText(text, x.toFloat(), y.toFloat(), textPaint)
        canvas.restore()
    }

    private fun drawStaticPointer(centerX: Float, centerY: Float, radius: Float, canvas: Canvas) {
        // 绘制带阴影的指针
        val path = Path().apply {
            moveTo(centerX, centerY - 220f)
            lineTo(centerX - 25f, centerY )
            lineTo(centerX + 25f, centerY )
            close()
        }

        // 绘制阴影
        pointerPaint.setShadowLayer(8f, 0f, 4f, Color.argb(100, 0, 0, 0))
        canvas.drawPath(path, pointerPaint)

        // 绘制主体
        pointerPaint.clearShadowLayer()
        canvas.drawPath(path, pointerPaint)
    }

    fun getRandomResultIndex(): Int {
        val totalWeight = items.sumOf { it.weight }
        val random = (0 until totalWeight).random()

        var accumulated = 0
        items.forEachIndexed { index, item ->
            accumulated += item.weight
            if (random < accumulated) return index
        }
        return 0
    }

    private fun cancelCurrentAnimation() {
        currentAnimator?.let {
            it.removeAllUpdateListeners()
            it.cancel()
        }
        currentAnimator = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelCurrentAnimation()
    }
}

