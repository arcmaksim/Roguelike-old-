package ru.MeatGames.roguelike.tomb

import ru.MeatGames.roguelike.tomb.model.HeroClass
import ru.MeatGames.roguelike.tomb.model.MapClass
import ru.MeatGames.roguelike.tomb.screen.BrezenhamView
import ru.MeatGames.roguelike.tomb.screen.MainMenu
import ru.MeatGames.roguelike.tomb.screen.MapView
import ru.MeatGames.roguelike.tomb.screen.StatsView
import ru.MeatGames.roguelike.tomb.util.AssetHelper

object Global {

    var game: Game? = null
    var hero: HeroClass? = null
    var mapg: MapGenerationClass? = null
    var mapview: MapView? = null
    var stsview: StatsView? = null
    var bview: BrezenhamView? = null
    var mmview: MainMenu? = null
    var map: Array<Array<MapClass>>? = null
    var tiles: Array<TileDB>? = null // 0 element is opaque
    var itemDB: Array<ItemDB>? = null
    var mobDB: Array<MobDB>? = null
    var stats: Array<StatsDB>? = null
    var mAssetHelper: AssetHelper? = null

}