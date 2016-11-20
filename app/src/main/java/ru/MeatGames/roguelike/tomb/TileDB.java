package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

/**
 * Класс базы данных тайлов.
 */

public class TileDB{
	/**
	 * @uml.property  name="psb"
	 */
	public boolean psb;
	/**
	 * @uml.property  name="vis"
	 */
	public boolean vis;
	/**
	 * @uml.property  name="use"
	 */
	public boolean use;
    /**
	 * @uml.property  name="isWall"
	 */
    public boolean isWall;
	public Bitmap img;

    public TileDB(){
        isWall = false;
    }
}
