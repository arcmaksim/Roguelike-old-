package ru.MeatGames.roguelike.tomb.model;

import android.graphics.Bitmap;

import java.util.LinkedList;

import ru.MeatGames.roguelike.tomb.Global;

public class HeroClass {

    // System
    public int x;
    public int y;
    public int mx;
    public int my;
    public Item[] equipmentList;
    public boolean side = true; //left

    // Stats
    public int regen;
    public int cregen;
    public int init = 10;
    public Bitmap[] img = new Bitmap[4];
    public LinkedList<Item> mInventory;

    public void newHero() {
        mInventory = null;
        mInventory = new LinkedList<>();
        equipmentList = new Item[3];
        Global.INSTANCE.getStats()[0].setValue(10);
        Global.INSTANCE.getStats()[1].setValue(10);
        Global.INSTANCE.getStats()[2].setValue(10);
        Global.INSTANCE.getStats()[3].setValue(10);
        Global.INSTANCE.getStats()[4].setValue(10);
        Global.INSTANCE.getStats()[5].setValue(18);
        Global.INSTANCE.getStats()[6].setValue(18);
        Global.INSTANCE.getStats()[7].setValue(10);
        Global.INSTANCE.getStats()[8].setValue(10);
        Global.INSTANCE.getStats()[9].setValue(10);
        Global.INSTANCE.getStats()[10].setValue(10);
        Global.INSTANCE.getStats()[11].setValue(2);
        Global.INSTANCE.getStats()[12].setValue(1);
        Global.INSTANCE.getStats()[13].setValue(3);
        Global.INSTANCE.getStats()[14].setValue(10);
        Global.INSTANCE.getStats()[16].setValue(10);
        Global.INSTANCE.getStats()[18].setValue(1000);
        Global.INSTANCE.getStats()[19].setValue(10);
        Global.INSTANCE.getStats()[20].setValue(0);
        Global.INSTANCE.getStats()[21].setValue(32);
        Global.INSTANCE.getStats()[22].setValue(1);
        Global.INSTANCE.getStats()[25].setValue(10);
        Global.INSTANCE.getStats()[27].setValue(1000);
        Global.INSTANCE.getStats()[28].setValue(10);
        Global.INSTANCE.getStats()[29].setValue(1);
        Global.INSTANCE.getStats()[31].setValue(1);
        cregen = regen = 22;
        addItem(Global.INSTANCE.getGame().createItem(1));
        addItem(Global.INSTANCE.getGame().createItem(4));
        addItem(Global.INSTANCE.getGame().createItem(7));
        addItem(Global.INSTANCE.getGame().createItem(10));
        addItem(Global.INSTANCE.getGame().createItem(10));
        addItem(Global.INSTANCE.getGame().createItem(10));
        for (int i = 0; i < 30; i++) {
            addItem(Global.INSTANCE.getGame().createItem(i % 11));
        }
        preequipItem(mInventory.get(0));
        preequipItem(mInventory.get(1));
        preequipItem(mInventory.get(2));
    }

    public int getStat(int id) {
        return Global.INSTANCE.getStats()[id].getValue();
    }

    public void modifyStat(int id, int value, int m) {
        Global.INSTANCE.getStats()[id].setValue(Global.INSTANCE.getStats()[id].getValue() + m * value);
        if (Global.INSTANCE.getStats()[id].getMaximum() && Global.INSTANCE.getStats()[id].getValue() > Global.INSTANCE.getStats()[id + 1].getValue())
            Global.INSTANCE.getStats()[id].setValue(Global.INSTANCE.getStats()[id + 1].getValue());
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
        mInventory.add(item);
    }

    public boolean isEquiped(Item item) {
        return equipmentList[item.type - 1] == item;
    }

    public void dropItem(Item item) {
        if (!item.isConsumable() && isEquiped(item))
            takeOffItem(item);
        mInventory.remove(item);
        Global.INSTANCE.getMap()[mx][my].addItem(item);
        Global.INSTANCE.getMapview().addLine(item.n + " выброшен" + item.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

    public void deleteItem(Item item) {
        mInventory.remove(item);
    }

    public void preequipItem(Item item) {
        switch (item.type) {
            case 1:
                equipmentList[0] = item;
                modifyStat(11, item.val1, 1);
                modifyStat(12, item.val2, 1);
                modifyStat(13, item.val3, 1);
                break;
            case 2:
                equipmentList[1] = item;
                modifyStat(19, item.val1, 1);
                modifyStat(22, item.val2, 1);
                break;
            case 3:
                equipmentList[2] = item;
                modifyStat(19, item.val1, 1);
                modifyStat(22, item.val2, 1);
                break;
        }
    }

    public void equipItem(Item item) {
        switch (item.type) {
            case 1:
                equipmentList[0] = item;
                if (item.property && equipmentList[1] != null) {
                    takeOffItem(1);
                }
                modifyStat(11, item.val1, 1);
                modifyStat(12, item.val2, 1);
                modifyStat(13, item.val3, 1);
                break;
            case 2:
                if (equipmentList[0] != null && equipmentList[0].property) {
                    takeOffItem(0);
                }
                equipmentList[1] = item;
                modifyStat(19, item.val1, 1);
                modifyStat(22, item.val2, 1);
                break;
            case 3:
                equipmentList[2] = item;
                modifyStat(19, item.val1, 1);
                modifyStat(22, item.val2, 1);
                break;
        }
        Global.INSTANCE.getMapview().addLine(item.n + " надет" + item.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

    public void takeOffItem(Item item) {
        switch (item.type) {
            case 1:
                modifyStat(11, item.val1, -1);
                modifyStat(12, item.val2, -1);
                modifyStat(13, item.val3, -1);
                break;
            case 2:
                modifyStat(19, item.val1, -1);
                modifyStat(22, item.val2, -1);
                break;
            case 3:
                modifyStat(19, item.val1, -1);
                modifyStat(22, item.val2, -1);
                break;
        }
        Global.INSTANCE.getHero().equipmentList[item.type - 1] = null;
        Global.INSTANCE.getMapview().addLine(item.n + " снят" + item.n1);
        Global.INSTANCE.getGame().move(0, 0);
    }

    public void takeOffItem(int i) {
        takeOffItem(equipmentList[i]);
    }
}