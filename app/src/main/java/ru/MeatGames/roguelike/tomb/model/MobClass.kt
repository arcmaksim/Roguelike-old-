package ru.MeatGames.roguelike.tomb.model;

data class MobClass(var name: String = "") {

    var hp: Int = 0
    var att: Int = 0
    var def: Int = 0
    var arm: Int = 0
    var spd: Int = 0
    var dmg: Int = 0

}