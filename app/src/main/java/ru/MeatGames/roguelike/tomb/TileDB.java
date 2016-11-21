package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

public class TileDB {

	public boolean psb;
	public boolean vis;
	public boolean use;
    public boolean isWall;
	public Bitmap img;

    public TileDB(){
        isWall = false;
    }

}