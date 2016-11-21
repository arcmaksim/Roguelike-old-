package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

public class MapClass {

	public int f;
	public int o;
    public ItemList head;
    public MobList mob;
	public boolean psb;
	public boolean vis;
	public boolean see;
	public boolean dis;
	public boolean use;

    public MapClass() {
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