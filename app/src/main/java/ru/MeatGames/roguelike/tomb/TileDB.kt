package ru.MeatGames.roguelike.tomb

import android.graphics.Bitmap

class TileDB {

    var psb: Boolean = false
    var vis: Boolean = false
    var use: Boolean = false
    var isWall: Boolean = false
    var img: Bitmap? = null

    init {
        isWall = false
    }

}