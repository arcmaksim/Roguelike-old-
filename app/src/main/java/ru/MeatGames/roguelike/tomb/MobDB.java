package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

/**
 * Класс базы данных противников.
 */

public class MobDB{
    /**
	 * @uml.property  name="mob"
	 * @uml.associationEnd  readOnly="true"
	 */
    public MobClass mob;
	public Bitmap[] img;

    public MobDB(){
        mob = new MobClass();
        img = new Bitmap[2];
    }
}
