package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.UnitConverter
import ru.MeatGames.roguelike.tomb.util.fillFrame
import ru.MeatGames.roguelike.tomb.view.TextButton

class MainMenu(context: Context) : View(context) {

    private val mBackgroundPaint = Paint()
    private val mTitleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    private val mNewGameButton: TextButton
    private val mExitGameButton: TextButton

    init {
        Global.game = context as Game
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize(context.windowManager)
        mScreenWidth = screenSize.x
        mScreenHeight = screenSize.y

        mBackgroundPaint.color = resources.getColor(R.color.mainBackground)

        mTitleTextPaint.color = resources.getColor(R.color.cell)
        mTitleTextPaint.style = Style.FILL_AND_STROKE
        mTitleTextPaint.textScaleX = 1f
        mTitleTextPaint.textSize = UnitConverter.convertSpToPixels(32F, context)
        mTitleTextPaint.textAlign = Paint.Align.CENTER
        mTitleTextPaint.typeface = Typeface.createFromAsset(Global.game!!.assets, "fonts/Bulgaria_Glorious_Cyr.ttf")

        mNewGameButton = TextButton(context, "Новая игра")
        mNewGameButton.mTextPaint.textAlign = Paint.Align.LEFT
        mNewGameButton.mDimensions = Rect(0,
                mScreenHeight - mScreenHeight / 10,
                mScreenWidth / 3,
                mScreenHeight)

        mExitGameButton = TextButton(context, "Выход")
        mExitGameButton.mTextPaint.textAlign = Paint.Align.RIGHT
        mExitGameButton.mDimensions = Rect(mScreenWidth / 3 * 2,
                mScreenHeight - mScreenHeight / 10,
                mScreenWidth,
                mScreenHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, measuredHeight, mBackgroundPaint)
        canvas.drawText("Yet Another",
                (mScreenWidth / 2).toFloat(),
                (mScreenHeight / 8 * 3).toFloat(),
                mTitleTextPaint)
        canvas.drawText("Roguelike",
                (mScreenWidth / 2).toFloat(),
                (mScreenHeight / 8 * 3 + mScreenHeight / 16).toFloat(),
                mTitleTextPaint)
        mNewGameButton.draw(canvas)
        mExitGameButton.draw(canvas)
        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val touchX = event.x.toInt()
                val touchY = event.y.toInt()
                if (mNewGameButton.isPressed(touchX, touchY)) {
                    Global.game!!.changeScreen(0)
                }
                if (mExitGameButton.isPressed(touchX, touchY)) {
                    Global.game!!.exitGame()
                }
            }
        }
        return true
    }

}