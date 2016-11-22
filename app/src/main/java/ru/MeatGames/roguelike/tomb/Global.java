package ru.MeatGames.roguelike.tomb;

import ru.MeatGames.roguelike.tomb.model.HeroClass;
import ru.MeatGames.roguelike.tomb.model.MapClass;
import ru.MeatGames.roguelike.tomb.screen.BrezenhamView;
import ru.MeatGames.roguelike.tomb.screen.InventoryView;
import ru.MeatGames.roguelike.tomb.screen.MainMenu;
import ru.MeatGames.roguelike.tomb.screen.MapView;
import ru.MeatGames.roguelike.tomb.screen.StatsView;
import ru.MeatGames.roguelike.tomb.util.AssetHelper;

public class Global {

    public static Game game;
    public static HeroClass hero;
    public static MapGenerationClass mapg;
    public static MapView mapview;
    public static InventoryView invview;
    public static StatsView stsview;
    public static BrezenhamView bview;
    public static MainMenu mmview;
    public static MapClass[][] map;
    public static TileDB[] tiles; // 0 element is opaque
    public static ItemDB[] itemDB;
    public static MobDB[] mobDB;
    public static StatsDB[] stats;
    public static AssetHelper mAssetHelper;

}