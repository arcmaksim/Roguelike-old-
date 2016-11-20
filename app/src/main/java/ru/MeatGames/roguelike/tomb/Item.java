package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

/**
 * Класс предметов.
 */

public class Item{
	public String n; //Name
	public String n1; //окончание
    /**
	 * @uml.property  name="w"
	 */
    public int w; //Weight
    /**
	 * @uml.property  name="v"
	 */
    public int v; //Value
    /**
	 * @uml.property  name="type"
	 */
    public int type; //Тип
    /**
	 * @uml.property  name="id"
	 */
    public int id; //ссылка на itemDB
    /**
	 * @uml.property  name="val1"
	 */
    public int val1;
	/**
	 * @uml.property  name="val2"
	 */
	public int val2;
	/**
	 * @uml.property  name="val3"
	 */
	public int val3;
    /**
	 * @uml.property  name="property"
	 */
    public boolean property;

    public Item(){
        val1 = val2 = val3 = -10000;
        property = false;
    }

    public Item(int id){
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

    public boolean isWeapon(){
        return type == 1;
    }
    public boolean isShield(){
        return type == 2;
    }
    public boolean isArmor(){
        return type == 3;
    }
    public boolean isGear(){
        return type == 4;
    }
    public boolean isConsumable(){
        return type == 5;
    }
    public Bitmap getImage(){
       return Global.itemDB[id].img;
    }
}