package ru.MeatGames.roguelike.tomb

import android.graphics.Bitmap

import ru.MeatGames.roguelike.tomb.model.MobClass

class MobDB {

    var mob: MobClass
    var img: Array<Bitmap?>

    init {
        mob = MobClass()
        img = arrayOfNulls<Bitmap>(2)
    }

}