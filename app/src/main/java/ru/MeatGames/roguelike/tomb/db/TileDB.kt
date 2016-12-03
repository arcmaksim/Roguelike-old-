package ru.MeatGames.roguelike.tomb.db

import android.graphics.Bitmap

class TileDB(var mIsPassable: Boolean,
             var mIsTransparent: Boolean,
             var mIsUsable: Boolean) {

    var mIsWall: Boolean = false
    var img: Bitmap? = null

}