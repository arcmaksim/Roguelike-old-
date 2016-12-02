package ru.MeatGames.roguelike.tomb

class StatsDB {

    var value: Int = 0
    var title: String? = null
    var single: Boolean = false // true означает, что эта характеристика не имеет ограничение на максимальное значение
    var maximum: Boolean = false /* true означает, что эта характеристика является текущим значением следующей характеристики
    (например, текущее здоровье), т.е. имеет ограничение на максимальное значение*/
    // single и maximum == false означает, что эта характеристика является максимальным значением для предыдущей характеристики

}