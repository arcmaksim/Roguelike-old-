package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

import ru.MeatGames.roguelike.tomb.model.MapClass;
import ru.MeatGames.roguelike.tomb.model.MobClass;

public class MobList {

	public MobList next;
    public MobClass mob;
    public MapClass map;
    public int t;
    public int turnCount;
	public int x;
	public int y;

    public Bitmap getImg(int time) {
        return Global.INSTANCE.getMobDB()[t].getImg()[time];
    }

    public MobList(int t) {
        this.t = t;
        mob = new MobClass();
        mob.setMHealth(Global.INSTANCE.getMobDB()[t].getMob().getMHealth());
        mob.setName(Global.INSTANCE.getMobDB()[t].getMob().getName());
        mob.setMDefense(Global.INSTANCE.getMobDB()[t].getMob().getMDefense());
        mob.setMArmor(Global.INSTANCE.getMobDB()[t].getMob().getMDefense());
        mob.setMSpeed(Global.INSTANCE.getMobDB()[t].getMob().getMSpeed());
    }
}