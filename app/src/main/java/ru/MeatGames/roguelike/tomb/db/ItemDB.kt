package ru.MeatGames.roguelike.tomb.db

import android.graphics.Bitmap

import ru.MeatGames.roguelike.tomb.model.Item

// TODO: reformat code - not very readable
class ItemDB : Item {

    var img: Bitmap? = null

    constructor(type: Int, title: String,
                titleEnding: String, value1: Int,
                value2: Int, value3: Int = 0,
                property: Boolean = false) : super(type, title, titleEnding, value1, value2, value3, property)

}