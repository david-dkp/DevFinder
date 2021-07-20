package fr.feepin.devfinder.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import fr.feepin.devfinder.R

class StepProgressBarView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var trackColor: Int
    private var indicatorColor: Int
    private var ballRadius: Int
    private var step: Int
    private var trackPadding: Float
    private var startingBallsMargin: Float = 0.0f
    private val stepCount: Int = 3

    private var trackPaint: Paint
    private var indicatorPaint: Paint


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.StepProgressBarView,
            0, 0
        ).apply {
            try {
                trackColor = getColor(R.styleable.StepProgressBarView_trackColor, Color.GRAY)
                indicatorColor =
                    getColor(R.styleable.StepProgressBarView_indicatorColor, Color.BLACK)
                ballRadius = getDimensionPixelSize(R.styleable.StepProgressBarView_ballRadius, 100)
                step = getInteger(R.styleable.StepProgressBarView_step, 0)
                if (step < 0) step = 0
                trackPadding = getDimensionPixelSize(
                    R.styleable.StepProgressBarView_trackPadding,
                    10
                ).toFloat()

                trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = trackColor
                    style = Paint.Style.FILL
                }

                indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = indicatorColor
                    style = Paint.Style.FILL
                }
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            //Track
            val rectYOffset = height * 0.3f
            drawRoundRect(
                0F,
                rectYOffset,
                width.toFloat(),
                height - rectYOffset,
                999f,
                999f,
                trackPaint
            )

            //Balls
            for (i in 0 until stepCount) {
                val ballX = calculateBallX(i)
                drawCircle(
                    ballX,
                    height / 2f,
                    ballRadius.toFloat(),
                    trackPaint
                )
            }

            //Indicator
            if (step != 0) {
                drawRoundRect(
                    trackPadding,
                    rectYOffset + trackPadding,
                    calculateIndicatorRight(),
                    height - rectYOffset - trackPadding,
                    999f,
                    999f,
                    indicatorPaint
                )
            }

            //Filled balls
            for (i in 0 until step) {
                val ballX = calculateBallX(i)
                drawCircle(
                    ballX,
                    height / 2f,
                    if (step == 0) 0f else ballRadius.toFloat() - trackPadding,
                    indicatorPaint
                )

            }
        }
    }

    private fun calculateBallX(ballIndex: Int): Float {
        val ballsContainerWidth = width - (startingBallsMargin * 2)
        return startingBallsMargin + (ballIndex * (ballsContainerWidth / (stepCount - 1)))
    }

    private fun calculateIndicatorRight(): Float {
        val ballsContainerWidth = width - (startingBallsMargin * 2)
        return when (step) {
            0 -> 0f
            stepCount + 1 -> width - trackPadding
            else -> startingBallsMargin + ((step - 1) * (ballsContainerWidth / (stepCount - 1))) - trackPadding
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, ballRadius * 2)
        setMeasuredDimension(widthMeasureSpec, ballRadius * 2)
        startingBallsMargin = measuredWidth * 0.2F
    }

}