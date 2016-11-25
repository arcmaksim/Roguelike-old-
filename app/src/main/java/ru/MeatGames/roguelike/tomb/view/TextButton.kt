package ru.MeatGames.roguelike.tomb.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.UnitConverter

class TextButton(mContext: Context, var mLabel: String = "") : Button(mContext) {

    override lateinit var mDimensions: Rect
    var mBackgroundPaint: Paint? = null
    var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mPadding: Float = UnitConverter.convertDpToPixels(24F, mContext)

    init {
        isFocusable = true
        isClickable = true

        mTextPaint.color = resources.getColor(R.color.cell)
        mTextPaint.style = Paint.Style.FILL_AND_STROKE
        mTextPaint.textScaleX = 1f
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = UnitConverter.convertSpToPixels(12F, mContext)
        mTextPaint.typeface = Typeface.createFromAsset(mContext.assets, "fonts/Bulgaria_Glorious_Cyr.ttf")
    }

    override fun onDraw(canvas: Canvas?) {
        if(mBackgroundPaint != null) {
            canvas?.drawRect(mDimensions, mBackgroundPaint)
        }
        when(mTextPaint.textAlign) {
            Paint.Align.CENTER ->
                canvas?.drawText(mLabel,
                        mDimensions.exactCenterX(),
                        mDimensions.bottom - mPadding,
                        mTextPaint)
            Paint.Align.LEFT ->
                canvas?.drawText(mLabel,
                        mDimensions.left + mPadding,
                        mDimensions.bottom - mPadding,
                        mTextPaint)
            Paint.Align.RIGHT ->
                canvas?.drawText(mLabel,
                        mDimensions.right - mPadding,
                        mDimensions.bottom - mPadding,
                        mTextPaint)
        }
        invalidate()
    }

}