package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

public class Item {

    public String n; //Name
    public String n1; //окончание
    public int w; //Weight
    public int v; //Value
    public int type; //type
    public int id; //something with itemDB
    public int val1;
    public int val2;
    public int val3;
    public boolean property;

    public Item() {
        val1 = val2 = val3 = -10000;
        property = false;
    }

    public Item(int id) {
        val1 = val2 = val3 = -10000;
        property = false;
        this.id = id;
        this.n = Global.itemDB[id].n;
        this.n1 = Global.itemDB[id].n1;
        this.type = Global.itemDB[id].type;
        this.val1 = Global.itemDB[id].val1;
        this.val2 = Global.itemDB[id].val2;
        this.val3 = Global.itemDB[id].val3;
        this.property = Global.itemDB[id].property;
    }

    public boolean isWeapon() {
        return type == 1;
    }

    public boolean isShield() {
        return type == 2;
    }

    public boolean isArmor() {
        return type == 3;
    }

    public boolean isGear() {
        return type == 4;
    }

    public boolean isConsumable() {
        return type == 5;
    }

    public Bitmap getImage() {
        return Global.itemDB[id].img;
    }

}