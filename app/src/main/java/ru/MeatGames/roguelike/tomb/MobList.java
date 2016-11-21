package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

public class MobList {

	public MobList next;
    public MobClass mob;
    public MapClass map;
    public int t;
    public int turnCount;
	public int x;
	public int y;

    public Bitmap getImg(int time) {
        return Global.mobDB[t].img[time];
    }

    public MobList(int t) {
        this.t = t;
        mob = new MobClass();
        mob.setHp(Global.mobDB[t].mob.getHp());
        mob.setName(Global.mobDB[t].mob.getName());
        mob.setDef(Global.mobDB[t].mob.getDef());
        mob.setArm(Global.mobDB[t].mob.getDef());
        mob.setSpd(Global.mobDB[t].mob.getSpd());
    }
}