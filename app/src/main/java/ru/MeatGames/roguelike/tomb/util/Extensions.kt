package ru.MeatGames.roguelike.tomb.util

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.fillFrame(width: Int, height: Int, backgroundColor: Paint) {
    this.drawRect(0F, 0F, width.toFloat(), height.toFloat(), backgroundColor)
}