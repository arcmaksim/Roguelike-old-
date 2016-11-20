package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

/**
 *  ласс €чейки карты. —одержит методы дл€ управлени€ содержимым €чейки.
 */

public class MapClass {
	/**
	 * @uml.property  name="f"
	 */
	public int f;
	/**
	 * @uml.property  name="o"
	 */
	public int o;
    /**
	 * @uml.property  name="head"
	 * @uml.associationEnd  readOnly="true"
	 */
    public ItemList head;
    /**
	 * @uml.property  name="mob"
	 * @uml.associationEnd  readOnly="true" inverse="map:ru.MeatGames.roguelike.tomb.MobList"
	 */
    public MobList mob;
	/**
	 * @uml.property  name="psb"
	 */
	public boolean psb;
	/**
	 * @uml.property  name="vis"
	 */
	public boolean vis;
	/**
	 * @uml.property  name="see"
	 */
	public boolean see;
	/**
	 * @uml.property  name="dis"
	 */
	public boolean dis;
	/**
	 * @uml.property  name="use"
	 */
	public boolean use;
	
	public MapClass(){
		see = false;
		vis = true;
		psb = true;
		dis = false;
	}

    public void addItem(Item item){
        if(head == null){
            head = new ItemList();
            head.item = item;
        }else{
            ItemList cur;
            for(cur=head;cur.next!=null;cur=cur.next){}
            cur.next = new ItemList();
            cur.next.item = item;
        }
    }

    public Item getItem(){
        Item res = head.item;
        head = head.next;
        return res;
    }

    public void deleteItems(){
        while(head!=null)
            getItem();
    }

    public void addMob(MobList mob){
        mob.map = this;
        this.mob = mob;
    }

    public void deleteMob(){
        mob = null;
    }
    public boolean hasItem(){
        return head != null;
    }
    public boolean hasMob(){
        return mob != null;
    }
    public boolean isWall(){
        return Global.tiles[o].isWall;
    }
    public Bitmap getFloorImg(){
        return Global.tiles[f].img;
    }
    public Bitmap getObjectImg(){
        return Global.tiles[o].img;
    }
    public Bitmap getItemImg(){
        return (head.next == null)?Global.itemDB[head.item.id].img:Global.game.bag;
    }
}
