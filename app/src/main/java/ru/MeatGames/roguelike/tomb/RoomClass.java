package ru.MeatGames.roguelike.tomb;

/**
 * Класс базы данных комнат.
 */

public class RoomClass {

	/**
	 * @uml.property  name="map" multiplicity="(0 -1)" dimension="2"
	 */
	public int map[][];
	/**
	 * @uml.property  name="n"
	 */
	public int n;
	
	public RoomClass(int[][] x){
		map = x;
	}
}
