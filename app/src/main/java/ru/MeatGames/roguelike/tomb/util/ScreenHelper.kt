package ru.MeatGames.roguelike.tomb.util

import android.graphics.Point
import android.view.WindowManager

object ScreenHelper {

    @JvmStatic
    fun getScreenSize(windowManager: WindowManager): Point {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        return size
    }

}