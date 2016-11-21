package ru.MeatGames.roguelike.tomb;

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