package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

public class MobDB{
    public MobClass mob;
	public Bitmap[] img;

    public MobDB(){
        mob = new MobClass();
        img = new Bitmap[2];
    }
}
