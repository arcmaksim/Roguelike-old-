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
import ru.MeatGames.roguelike.tomb.Screens
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.fillFrame
import ru.MeatGames.roguelike.tomb.view.TextButton

class CharacterScreen(mContext: Context) : View(mContext) {

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    private val mTextPaint: Paint
    private val mBackgroundPaint = Paint()

    private val mBackButton: TextButton

    private val mOffsetX: Float

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize((context as Game).windowManager)
        mScreenWidth = screenSize.x
        mScreenHeight = screenSize.y

        mBackgroundPaint.color = resources.getColor(R.color.mainBackground)
        mTextPaint = ScreenHelper.getDefaultTextPaint(mContext)
        mTextPaint.textSize = 24f
        mTextPaint.textAlign = Paint.Align.LEFT

        mBackButton = TextButton(context, resources.getString(R.string.back_label))
        mBackButton.mTextPaint.textAlign = Paint.Align.RIGHT
        mBackButton.mDimensions = Rect(mScreenWidth / 3 * 2,
                mScreenHeight - mScreenHeight / 10,
                mScreenWidth,
                mScreenHeight)

        mOffsetX = mScreenWidth * 0.146F
    }

    override fun onDraw(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, mScreenHeight, mBackgroundPaint)
        canvas.drawText("Уровень ${Global.hero!!.getStat(31)}", mOffsetX, mScreenHeight * 0.15F, mTextPaint)
        canvas.drawText("Сила ${Global.hero!!.getStat(0)}", mOffsetX, mScreenHeight * 0.2F, mTextPaint)
        canvas.drawText("Ловкость ${Global.hero!!.getStat(1)}", mOffsetX, mScreenHeight * 0.2375F, mTextPaint)
        canvas.drawText("Интеллект ${Global.hero!!.getStat(2)}", mOffsetX, mScreenHeight * 0.275F, mTextPaint)
        canvas.drawText("Выносливость ${Global.hero!!.getStat(3)}", mOffsetX, mScreenHeight * 0.3125F, mTextPaint)
        canvas.drawText("Восприятие ${Global.hero!!.getStat(4)}", mOffsetX, mScreenHeight * 0.35F, mTextPaint)
        canvas.drawText("Здоровье ${Global.hero!!.getStat(5)} / ${Global.hero!!.getStat(6)}", mOffsetX, mScreenHeight * 0.4F, mTextPaint)
        canvas.drawText("Мана ${Global.hero!!.getStat(7)} / ${Global.hero!!.getStat(8)}", mOffsetX, mScreenHeight * 0.4375F, mTextPaint)
        canvas.drawText("Запас сил ${Global.hero!!.getStat(9)} / ${Global.hero!!.getStat(10)}", mOffsetX, mScreenHeight * 0.475F, mTextPaint)
        canvas.drawText("Атака +${Global.hero!!.getStat(11)}", mOffsetX, mScreenHeight * 0.525F, mTextPaint)
        canvas.drawText("Урон ${Global.hero!!.getStat(12)} - ${Global.hero!!.getStat(13)}", mOffsetX, mScreenHeight * 0.5625F, mTextPaint)
        canvas.drawText("Защита ${Global.hero!!.getStat(19)}", mOffsetX, mScreenHeight * 0.6F, mTextPaint)
        canvas.drawText("Броня ${Global.hero!!.getStat(22)}", mOffsetX, mScreenHeight * 0.6375F, mTextPaint)

        mBackButton.draw(canvas)

        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x.toInt()
                val touchY = event.y.toInt()
                if (mBackButton.isPressed(touchX, touchY)) {
                    Global.game.changeScreen(Screens.GAME_SCREEN)
                }
            }
        }
        return true
    }
}