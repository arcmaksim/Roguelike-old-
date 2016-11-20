package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

/**
 *  ласс дл€ создани€ очерЄдности ходов противников.
 */

public class MobList{
	/**
	 * @uml.property  name="next"
	 * @uml.associationEnd  readOnly="true"
	 */
	public MobList next;
    /**
	 * @uml.property  name="mob"
	 * @uml.associationEnd  readOnly="true"
	 */
    public MobClass mob;
    /**
	 * @uml.property  name="map"
	 * @uml.associationEnd  readOnly="true" inverse="mob:ru.MeatGames.roguelike.tomb.MapClass"
	 */
    public MapClass map; // указатель на €чейку карты
    /**
	 * @uml.property  name="t"
	 */
    public int t;
    /**
	 * @uml.property  name="turnCount"
	 */
    public int turnCount;
	/**
	 * @uml.property  name="x"
	 */
	public int x;
	/**
	 * @uml.property  name="y"
	 */
	public int y;

    public Bitmap getImg(int time){return Global.mobDB[t].img[time];}

    public MobList(int t){
        this.t = t;
        mob = new MobClass();
        mob.hp = Global.mobDB[t].mob.hp;
        mob.name = Global.mobDB[t].mob.name;
        mob.def = Global.mobDB[t].mob.def;
        mob.arm = Global.mobDB[t].mob.arm;
        mob.spd = Global.mobDB[t].mob.spd;
    }
}
