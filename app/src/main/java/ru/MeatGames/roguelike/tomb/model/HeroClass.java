package ru.MeatGames.roguelike.tomb.model;

import android.graphics.Bitmap;

import ru.MeatGames.roguelike.tomb.Global;
import ru.MeatGames.roguelike.tomb.InvItem;

public class HeroClass {

    // System
    public int x;
    public int y;
    public int mx;
    public int my;
    public InvItem[] equipmentList;
    public boolean side = true; //left

    // Stats
    public int regen;
    public int cregen;
    public int init = 10;
    public Bitmap[] img = new Bitmap[4];
    public InvItem inv;

    public void newHero() {
        inv = null;
        equipmentList = new InvItem[3];
        Global.INSTANCE.getStats()[0].setA(10);
        Global.INSTANCE.getStats()[1].setA(10);
        Global.INSTANCE.getStats()[2].setA(10);
        Global.INSTANCE.getStats()[3].setA(10);
        Global.INSTANCE.getStats()[4].setA(10);
        Global.INSTANCE.getStats()[5].setA(18);
        Global.INSTANCE.getStats()[6].setA(18);
        Global.INSTANCE.getStats()[7].setA(10);
        Global.INSTANCE.getStats()[8].setA(10);
        Global.INSTANCE.getStats()[9].setA(10);
        Global.INSTANCE.getStats()[10].setA(10);
        Global.INSTANCE.getStats()[11].setA(2);
        Global.INSTANCE.getStats()[12].setA(1);
        Global.INSTANCE.getStats()[13].setA(3);
        Global.INSTANCE.getStats()[14].setA(10);
        Global.INSTANCE.getStats()[16].setA(10);
        Global.INSTANCE.getStats()[18].setA(1000);
        Global.INSTANCE.getStats()[19].setA(10);
        Global.INSTANCE.getStats()[20].setA(0);
        Global.INSTANCE.getStats()[21].setA(32);
        Global.INSTANCE.getStats()[22].setA(1);
        Global.INSTANCE.getStats()[25].setA(10);
        Global.INSTANCE.getStats()[27].setA(1000);
        Global.INSTANCE.getStats()[28].setA(10);
        Global.INSTANCE.getStats()[29].setA(1);
        Global.INSTANCE.getStats()[31].setA(1);
        cregen = regen = 22;
        addItem(Global.INSTANCE.getGame().createItem(1));
        addItem(Global.INSTANCE.getGame().createItem(4));
        addItem(Global.INSTANCE.getGame().createItem(7));
        addItem(Global.INSTANCE.getGame().createItem(10));
        addItem(Global.INSTANCE.getGame().createItem(10));
        addItem(Global.INSTANCE.getGame().createItem(10));
        preequipItem(inv);
        preequipItem(inv.next);
        preequipItem(inv.next.next);
    }

    public int getStat(int id) {
        return Global.INSTANCE.getStats()[id].getA();
    }

    public void modifyStat(int id, int value, int m) {
        Global.INSTANCE.getStats()[id].setA(Global.INSTANCE.getStats()[id].getA() + m * value);
        if (Global.INSTANCE.getStats()[id].getM() && Global.INSTANCE.getStats()[id].getA() > Global.INSTANCE.getStats()[id + 1].getA())
            Global.INSTANCE.getStats()[id].setA(Global.INSTANCE.getStats()[id + 1].getA());
        if (id == 20) isLevelUp();
    }

    public void isLevelUp() {
        if (getStat(20) >= getStat(21)) {
            modifyStat(20, getStat(21), -1);
            modifyStat(21, getStat(21), 1);
            modifyStat(31, 1, 1);
            Global.INSTANCE.getMapview().addLine("Уровень повышен!");
            int u = Global.INSTANCE.getGame().rnd.nextInt(3) + 2;
            modifyStat(6, u, 1);
            modifyStat(5, u, 1);
            Global.INSTANCE.getMapview().addLine("Здоровье увеличено");
            if (getStat(31) % 4 == 0) {
                modifyStat(12, 1, 1);
                Global.INSTANCE.getMapview().addLine("Минимальный урон увеличен");
            }
            if (getStat(31) % 5 == 0) {
                regen--;
                if (regen < cregen) cregen = regen;
                Global.INSTANCE.getMapview().addLine("Скорость регенерации увеличена");
            }
            if (getStat(31) % 2 == 0) {
                modifyStat(13, 1, 1);
                Global.INSTANCE.getMapview().addLine("Максиммальный урон увеличен");
            }
            if (getStat(31) % 3 == 0) {
                modifyStat(19, 1, 1);
                Global.INSTANCE.getMapview().addLine("Защита увеличена");
            }
        }
    }

    public void addItem(Item item) {
        if (inv == null) {
            inv = new InvItem();
            inv.item = item;
        } else {
            InvItem cur;
            for (cur = inv; cur.next != null; cur = cur.next) {
            }
            cur.next = new InvItem();
            cur.next.item = item;
            for (cur = inv; cur != null; cur = cur.next)
                cur.nextList = null;
        }
    }

    public boolean isEquiped(InvItem item) {
        return equipmentList[item.item.type - 1] == item;
    }

    public void dropItem(InvItem item) {
        if (!item.item.isConsumable() && isEquiped(item))
            takeOffItem(item);
        Item i = item.item;
        InvItem cur;
        for (cur = inv; cur != null; cur = cur.next)
            cur.nextList = null;
        if (inv == item) {
            inv = inv.next;
        } else {
            for (cur = inv; cur.next != item; cur = cur.next) {
            }
            cur.next = item.next;
        }
        Global.INSTANCE.getMap()[mx][my].addItem(i);
        Global.INSTANCE.getMapview().addLine(i.n + " выброшен" + i.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

    public void deleteItem(InvItem item) {
        InvItem cur;
        for (cur = inv; cur != null; cur = cur.next)
            cur.nextList = null;
        if (item == inv) {
            inv = inv.next;
        } else {
            for (cur = inv; cur.next != item; cur = cur.next) {
            }
            cur.next = item.next;
        }
    }

    public void preequipItem(InvItem item) {
        switch (item.item.type) {
            case 1:
                equipmentList[0] = item;
                modifyStat(11, item.item.val1, 1);
                modifyStat(12, item.item.val2, 1);
                modifyStat(13, item.item.val3, 1);
                break;
            case 2:
                equipmentList[1] = item;
                modifyStat(19, item.item.val1, 1);
                modifyStat(22, item.item.val2, 1);
                break;
            case 3:
                equipmentList[2] = item;
                modifyStat(19, item.item.val1, 1);
                modifyStat(22, item.item.val2, 1);
                break;
        }
    }

    public void equipItem(InvItem item) {
        switch (item.item.type) {
            case 1:
                equipmentList[0] = item;
                if (item.item.property && equipmentList[1] != null)
                    takeOffItem(1);
                modifyStat(11, item.item.val1, 1);
                modifyStat(12, item.item.val2, 1);
                modifyStat(13, item.item.val3, 1);
                break;
            case 2:
                if (equipmentList[0] != null && equipmentList[0].item.property)
                    takeOffItem(0);
                equipmentList[1] = item;
                modifyStat(19, item.item.val1, 1);
                modifyStat(22, item.item.val2, 1);
                break;
            case 3:
                equipmentList[2] = item;
                modifyStat(19, item.item.val1, 1);
                modifyStat(22, item.item.val2, 1);
                break;
        }
        Global.INSTANCE.getMapview().addLine(item.item.n + " надет" + item.item.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

    public void takeOffItem(InvItem item) {
        switch (item.item.type) {
            case 1:
                modifyStat(11, item.item.val1, -1);
                modifyStat(12, item.item.val2, -1);
                modifyStat(13, item.item.val3, -1);
                break;
            case 2:
                modifyStat(19, item.item.val1, -1);
                modifyStat(22, item.item.val2, -1);
                break;
            case 3:
                modifyStat(19, item.item.val1, -1);
                modifyStat(22, item.item.val2, -1);
                break;
        }
        Global.INSTANCE.getHero().equipmentList[item.item.type - 1] = null;
        Global.INSTANCE.getMapview().addLine(item.item.n + " снят" + item.item.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

    public void takeOffItem(int i) {
        InvItem item = equipmentList[i];
        switch (item.item.type) {
            case 1:
                modifyStat(11, item.item.val1, -1);
                modifyStat(12, item.item.val2, -1);
                modifyStat(13, item.item.val3, -1);
                break;
            case 2:
                modifyStat(19, item.item.val1, -1);
                modifyStat(22, item.item.val2, -1);
                break;
            case 3:
                modifyStat(19, item.item.val1, -1);
                modifyStat(22, item.item.val2, -1);
                break;
        }
        Global.INSTANCE.getHero().equipmentList[item.item.type - 1] = null;
        Global.INSTANCE.getMapview().addLine(item.item.n + " снят" + item.item.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

}