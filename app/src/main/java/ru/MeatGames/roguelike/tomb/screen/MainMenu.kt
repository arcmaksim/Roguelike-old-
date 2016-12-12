package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.UnitConverter
import ru.MeatGames.roguelike.tomb.util.fillFrame
import ru.MeatGames.roguelike.tomb.view.TextButton

class MainMenu(mContext: Context) : View(mContext) {

    private val mBackgroundPaint = Paint()
    private val mTitleTextPaint: Paint

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    private val mNewGameButton: TextButton
    private val mExitGameButton: TextButton

    init {
        Global.game = mContext as Game
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize(mContext.windowManager)
        mScreenWidth = screenSize.x
        mScreenHeight = screenSize.y

        mBackgroundPaint.color = resources.getColor(R.color.mainBackground)

        mTitleTextPaint = ScreenHelper.getDefaultTextPaint(mContext)
        mTitleTextPaint.textSize = UnitConverter.convertSpToPixels(32F, context)

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
                    Global.game.changeScreen(Screens.GAME_SCREEN)
                }
                if (mExitGameButton.isPressed(touchX, touchY)) {
                    Global.game.exitGame()
                }
            }
        }
        return true
    }

}