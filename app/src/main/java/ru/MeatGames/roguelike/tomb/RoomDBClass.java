package ru.MeatGames.roguelike.tomb;

/**
 * Хранение данных уже построенных комнат.
 */

public class RoomDBClass {
	/**
	 * @uml.property  name="x"
	 */
	public int x;
	/**
	 * @uml.property  name="y"
	 */
	public int y;
	/**
	 * @uml.property  name="lx"
	 */
	public int lx;
	/**
	 * @uml.property  name="ly"
	 */
	public int ly;
	/**
	 * @uml.property  name="t"
	 */
	public int t;
	/**
	 * @uml.property  name="n"
	 */
	public int n;
	
	public RoomDBClass(int x,int y,int lx,int ly){
		this.x = x;
		this.y = y;
		this.lx = lx;
		this.ly = ly;
	}
}
