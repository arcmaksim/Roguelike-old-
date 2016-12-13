package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.fillFrame

open class BasicScreen(context: Context) : View(context) {

    protected val mScreenWidth: Int
    protected val mScreenHeight: Int

    protected val mBackgroundPaint = Paint()

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize((context as Game).windowManager)
        mScreenWidth = screenSize.x
        mScreenHeight = screenSize.y

        mBackgroundPaint.color = resources.getColor(R.color.mainBackground)
    }

    protected fun drawBackground(canvas: Canvas, backgroundPaint: Paint = mBackgroundPaint) {
        canvas.fillFrame(mScreenWidth, mScreenHeight, backgroundPaint)
    }

}