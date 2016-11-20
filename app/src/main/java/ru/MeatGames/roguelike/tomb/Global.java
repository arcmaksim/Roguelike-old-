package ru.MeatGames.roguelike.tomb;

/**
 * Предоставляет возможность взаимодействия между экранами, а также с базами данных.
 */

public class Global{
	public static Game game;
	public static HeroClass hero;
	public static MapGenerationClass mapg;
	public static MapView mapview;
	public static InventoryView invview;
	public static StatsView stsview;
	public static BrezenhamView bview;
    public static MainMenu mmview;
	public static MapClass[][] map;
	public static TileDB[] tiles; // 0 элемент прозрачный
	public static ItemDB[] itemDB;
	public static MobDB[] mobDB;
    public static StatsDB[] stats;
}
