package ru.MeatGames.roguelike.tomb;

import android.graphics.Bitmap;

/**
 * Класс игрока. Содержит инвентарь, характеристики, а также функции для управления ими.
 */

public class HeroClass{
	// System
	/**
	 * @uml.property  name="x"
	 */
	public int x;
	/**
	 * @uml.property  name="y"
	 */
	public int y;
	/**
	 * @uml.property  name="mx"
	 */
	public int mx;
	/**
	 * @uml.property  name="my"
	 */
	public int my;
    /**
	 * @uml.property  name="equipmentList"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    public InvItem[] equipmentList;
	/**
	 * @uml.property  name="side"
	 */
	public boolean side=true; //left
	// Stats
	/**
	 * @uml.property  name="regen"
	 */
	public int regen;
	/**
	 * @uml.property  name="cregen"
	 */
	public int cregen;
	/**
	 * @uml.property  name="init"
	 */
	public int init = 10;
	public Bitmap[] img = new Bitmap[4];
	/**
	 * @uml.property  name="inv"
	 * @uml.associationEnd  readOnly="true"
	 */
	public InvItem inv;

    public void newHero(){
        inv = null;
        equipmentList = new InvItem[3];
        Global.stats[0].a = 10;
        Global.stats[1].a = 10;
        Global.stats[2].a = 10;
        Global.stats[3].a = 10;
        Global.stats[4].a = 10;
        Global.stats[5].a = 18;
        Global.stats[6].a = 18;
        Global.stats[7].a = 10;
        Global.stats[8].a = 10;
        Global.stats[9].a = 10;
        Global.stats[10].a = 10;
        Global.stats[11].a = 2;
        Global.stats[12].a = 1;
        Global.stats[13].a = 3;
        Global.stats[14].a = 10;
        Global.stats[16].a = 10;
        Global.stats[18].a = 1000;
        Global.stats[19].a = 10;
        Global.stats[20].a = 0;
        Global.stats[21].a = 32;
        Global.stats[22].a = 1;
        Global.stats[25].a = 10;
        Global.stats[27].a = 1000;
        Global.stats[28].a = 10;
        Global.stats[29].a = 1;
        Global.stats[31].a = 1;
        cregen = regen = 22;
        addItem(Global.game.createItem(1));
        addItem(Global.game.createItem(4));
        addItem(Global.game.createItem(7));
        addItem(Global.game.createItem(10));
        addItem(Global.game.createItem(10));
        addItem(Global.game.createItem(10));
        preequipItem(inv);
        preequipItem(inv.next);
        preequipItem(inv.next.next);
    }

	public int getStat(int id){
		return Global.stats[id].a;
	}
	
	public void modifyStat(int id,int value,int m){
        Global.stats[id].a += m * value;
        if(Global.stats[id].m && Global.stats[id].a > Global.stats[id+1].a)
            Global.stats[id].a = Global.stats[id+1].a;
    	if(id == 20)isLevelUp();
    }

    public void isLevelUp(){
        if(getStat(20)>=getStat(21)){
            modifyStat(20,getStat(21),-1);
            modifyStat(21,getStat(21),1);
            modifyStat(31,1,1);
            Global.mapview.addLine("Уровнь повышен!");
            int u = Global.game.rnd.nextInt(3)+2;
            modifyStat(6,u,1);
            modifyStat(5,u,1);
            Global.mapview.addLine("Здоровье увеличено");
            if(getStat(31)%4==0){
                modifyStat(12,1,1);
                Global.mapview.addLine("Минимальный урон увеличен");
            }
            if(getStat(31)%5==0){
                regen--;
                if(regen<cregen)cregen=regen;
                Global.mapview.addLine("Скорость регенерации увеличена");
            }
            if(getStat(31)%2==0){
                modifyStat(13,1,1);
                Global.mapview.addLine("Максимальный урон увеличен");
            }
            if(getStat(31)%3==0){
                modifyStat(19,1,1);
                Global.mapview.addLine("Защита увеличена");
            }
        }
    }

    public void addItem(Item item){
        if(inv == null){
            inv = new InvItem();
            inv.item = item;
        }else{
            InvItem cur;
            for(cur=inv;cur.next!=null;cur=cur.next){}
            cur.next = new InvItem();
            cur.next.item = item;
            for(cur=inv;cur!=null;cur=cur.next)
                cur.nextList = null;
        }
    }

    public boolean isEquiped(InvItem item){
        return equipmentList[item.item.type-1] == item;
    }

    public void dropItem(InvItem item){
        if(!item.item.isConsumable() && isEquiped(item))
            takeOffItem(item);
        Item i = item.item;
        InvItem cur;
        for(cur=inv;cur!=null;cur=cur.next)
            cur.nextList = null;
        if(inv == item){
            inv = inv.next;
        }else{
            for(cur=inv;cur.next!=item;cur=cur.next){}
            cur.next = item.next;
        }
        Global.map[mx][my].addItem(i);
        Global.mapview.addLine(i.n + " выброшен" + i.n1);
        Global.game.move(0,0);
    }

    public void deleteItem(InvItem item){
        InvItem cur;
        for(cur=inv;cur!=null;cur=cur.next)
            cur.nextList = null;
        if(item == inv){
            inv = inv.next;
        }else{
            for(cur=inv;cur.next!=item;cur=cur.next){}
                cur.next = item.next;
        }
    }

    public void preequipItem(InvItem item){
        switch(item.item.type){
            case 1:
                equipmentList[0] = item;
                modifyStat(11,item.item.val1,1);
                modifyStat(12,item.item.val2,1);
                modifyStat(13,item.item.val3,1);
                break;
            case 2:
                equipmentList[1] = item;
                modifyStat(19,item.item.val1,1);
                modifyStat(22,item.item.val2,1);
                break;
            case 3:
                equipmentList[2] = item;
                modifyStat(19,item.item.val1,1);
                modifyStat(22,item.item.val2,1);
                break;
        }
    }

    public void equipItem(InvItem item){
        switch(item.item.type){
            case 1:
                equipmentList[0] = item;
                if(item.item.property && equipmentList[1]!=null)
                    takeOffItem(1);
                modifyStat(11,item.item.val1,1);
                modifyStat(12,item.item.val2,1);
                modifyStat(13,item.item.val3,1);
                break;
            case 2:
                if(equipmentList[0]!=null && equipmentList[0].item.property)
                    takeOffItem(0);
                equipmentList[1] = item;
                modifyStat(19,item.item.val1,1);
                modifyStat(22,item.item.val2,1);
                break;
            case 3:
                equipmentList[2] = item;
                modifyStat(19,item.item.val1,1);
                modifyStat(22,item.item.val2,1);
                break;
        }
        Global.mapview.addLine(item.item.n + " одет" + item.item.n1);
        Global.game.move(0,0);
    }
       
    public void takeOffItem(InvItem item){
        switch(item.item.type){
            case 1:
                modifyStat(11,item.item.val1,-1);
                modifyStat(12,item.item.val2,-1);
                modifyStat(13,item.item.val3,-1);
                break;
            case 2:
                modifyStat(19,item.item.val1,-1);
                modifyStat(22,item.item.val2,-1);
                break;
            case 3:
                modifyStat(19,item.item.val1,-1);
                modifyStat(22,item.item.val2,-1);
                break;
        }
        Global.hero.equipmentList[item.item.type-1] = null;
        Global.mapview.addLine(item.item.n + " снят" + item.item.n1);
        Global.game.move(0,0);
    }

    public void takeOffItem(int i){
        InvItem item = equipmentList[i];
        switch(item.item.type){
            case 1:
                modifyStat(11,item.item.val1,-1);
                modifyStat(12,item.item.val2,-1);
                modifyStat(13,item.item.val3,-1);
                break;
            case 2:
                modifyStat(19,item.item.val1,-1);
                modifyStat(22,item.item.val2,-1);
                break;
            case 3:
                modifyStat(19,item.item.val1,-1);
                modifyStat(22,item.item.val2,-1);
                break;
        }
        Global.hero.equipmentList[item.item.type-1] = null;
        Global.mapview.addLine(item.item.n + " снят" + item.item.n1);
        Global.game.move(0,0);
    }
}
