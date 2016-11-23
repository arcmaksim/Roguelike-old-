package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.UnitConverter
import ru.MeatGames.roguelike.tomb.util.fillFrame
import ru.MeatGames.roguelike.tomb.view.TextButton

class MainMenu(context: Context) : View(context) {

    private val mBackgroundColor: Paint
    private val mTitleTextPaint: Paint

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    private val mNewGameButton: TextButton
    private val mExitGameButton: TextButton

    init {
        Global.game = context as Game
        isFocusable = true
        isFocusableInTouchMode = true

        val display = context.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        mScreenWidth = size.x
        mScreenHeight = size.y

        mBackgroundColor = Paint()
        mBackgroundColor.color = resources.getColor(R.color.frame)

        mTitleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTitleTextPaint.color = resources.getColor(R.color.cell)
        mTitleTextPaint.style = Style.FILL_AND_STROKE
        mTitleTextPaint.textScaleX = 1f
        mTitleTextPaint.textSize = UnitConverter.convertSpToPixels(32F, context)
        mTitleTextPaint.textAlign = Paint.Align.CENTER
        mTitleTextPaint.typeface = Typeface.createFromAsset(Global.game!!.assets, "fonts/Bulgaria_Glorious_Cyr.ttf")

        mNewGameButton = TextButton(context, "Новая игра")
        mNewGameButton.mTextPaint.textAlign = Paint.Align.LEFT
        mNewGameButton.mDimensions = Rect(0, size.y - size.y / 10, size.x / 3, size.y)

        mExitGameButton = TextButton(context, "Выход")
        mExitGameButton.mTextPaint.textAlign = Paint.Align.RIGHT
        mExitGameButton.mDimensions = Rect(size.x / 3 * 2, size.y - size.y / 10, size.x, size.y)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, measuredHeight, mBackgroundColor)
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