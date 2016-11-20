package ru.MeatGames.roguelike.tomb;

/**
 * Класс базы данных характеристик игрока.
 */

public class StatsDB {
	/**
	 * @uml.property  name="a"
	 */
	public int a; // значение
	public String n;
	/**
	 * @uml.property  name="s"
	 */
	public boolean s; // true если значение статично (защита, скорость и т.д.)
	/**
	 * @uml.property  name="m"
	 */
	public boolean m; // true если значение максимально/минимально (здоровье, мана и т.д.), тогда n <= n + 1
	// s и m == false значит, что эта характеристика является максимумом для характеристики с предыдущем номером
}
