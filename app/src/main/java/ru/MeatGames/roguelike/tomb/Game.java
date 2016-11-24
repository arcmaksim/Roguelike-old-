package ru.MeatGames.roguelike.tomb;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;

import org.xmlpull.v1.XmlPullParser;

import java.util.Random;

import ru.MeatGames.roguelike.tomb.model.HeroClass;
import ru.MeatGames.roguelike.tomb.model.Item;
import ru.MeatGames.roguelike.tomb.model.MapClass;
import ru.MeatGames.roguelike.tomb.screen.BrezenhamView;
import ru.MeatGames.roguelike.tomb.screen.InventoryView;
import ru.MeatGames.roguelike.tomb.screen.MainMenu;
import ru.MeatGames.roguelike.tomb.screen.MapView;
import ru.MeatGames.roguelike.tomb.screen.StatsView;
import ru.MeatGames.roguelike.tomb.util.AssetHelper;

public class Game extends Activity {

    public static Vibrator v;
    public static int curLvls = 0;
    public final int mw = 96; //64
    public final int mh = 96; //64
    public final int step = 48; //48
    public final int maxTiles = 100;
    public final int maxItems = 20;
    public final int maxStats = 35;
    public final int maxMobs = 6;
    public final int defValue = 99;
    public final int maxLvl = 3;
    public RoomDBClass[] room;
    public Typeface font;
    public int turnCount = 0;
    public boolean turn = true;
    public boolean move = true;
    public boolean tap;
    public Bitmap a;
    public Bitmap b;
    public Bitmap backIcon;
    public Bitmap d;
    public Bitmap j;
    public Random rnd;
    public boolean lines = true;
    public int scr = 0;
    public Bitmap lastAttack;
    public Bitmap bag;
    public MobList firstMob;
    public int[][] zone;

    public static Bitmap getHeroImg(int n) {
        return Global.INSTANCE.getHero().img[n];
    }

    public static int getFloor(int x, int y) {
        return Global.INSTANCE.getMap()[x][y].f;
    }

    public static int getObject(int x, int y) {
        return Global.INSTANCE.getMap()[x][y].o;
    }

    protected void onCreate(Bundle w) {
        super.onCreate(w);

        Global.INSTANCE.setGame(this);
        rnd = new Random();

        Global.INSTANCE.setMap(new MapClass[mw][mh]);
        for (int x = 0; x < mw; x++)
            for (int y = 0; y < mh; y++)
                Global.INSTANCE.getMap()[x][y] = new MapClass();

        Global.INSTANCE.setTiles(new TileDB[maxTiles]);
        for (int x = 0; x < maxTiles; x++)
            Global.INSTANCE.getTiles()[x] = new TileDB();

        Global.INSTANCE.setItemDB(new ItemDB[maxItems]);
        for (int x = 0; x < maxItems; x++)
            Global.INSTANCE.getItemDB()[x] = new ItemDB();

        Global.INSTANCE.setMobDB(new MobDB[maxMobs]);
        for (int x = 0; x < maxMobs; x++)
            Global.INSTANCE.getMobDB()[x] = new MobDB();

        Global.INSTANCE.setStats(new StatsDB[Global.INSTANCE.getGame().maxStats]);
        for (int x = 0; x < Global.INSTANCE.getGame().maxStats; x++)
            Global.INSTANCE.getStats()[x] = new StatsDB();

        Global.INSTANCE.setMAssetHelper(new AssetHelper(getAssets()));

        Global.INSTANCE.setHero(new HeroClass());
        Global.INSTANCE.setMapg(new MapGenerationClass());
        Global.INSTANCE.setMmview(new MainMenu(this));
        Global.INSTANCE.setMapview(new MapView(this));
        Global.INSTANCE.setInvview(new InventoryView(this));
        Global.INSTANCE.setStsview(new StatsView(this));
        Global.INSTANCE.setBview(new BrezenhamView(this));

        zone = new int[11][11];

        font = Typeface.createFromAsset(this.getAssets(), "fonts/crancyr.ttf");
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loading();
        newGame();
        mainBody();
    }

    public void onBackPressed() {
        if (scr == 0 && !Global.INSTANCE.getMapview().getMDrawProgressBar())
            Global.INSTANCE.getMapview().setMDrawExitDialog(!Global.INSTANCE.getMapview().getMDrawExitDialog());
    }

    public void exitGame() {
        finish();
    }

    public void newGame() {
        Global.INSTANCE.getHero().newHero();
        curLvls = 0;
        Global.INSTANCE.getMapg().mapGen();
        Global.INSTANCE.getMapview().clearLog();
        changeScreen(4);
    }

    public void changeScreen(int u) {
        scr = u;
        switch (u) {
            case 0:
                setContentView(Global.INSTANCE.getMapview());
                Global.INSTANCE.getMapview().requestFocus();
                break;
            case 1:
                Global.INSTANCE.getInvview().fillList();
                setContentView(Global.INSTANCE.getInvview());
                Global.INSTANCE.getInvview().requestFocus();
                break;
            case 2:
                setContentView(Global.INSTANCE.getStsview());
                Global.INSTANCE.getStsview().requestFocus();
                break;
            case 3:
                setContentView(Global.INSTANCE.getBview());
                Global.INSTANCE.getBview().requestFocus();
                break;
            case 4:
                setContentView(Global.INSTANCE.getMmview());
                Global.INSTANCE.getMmview().requestFocus();
                break;
        }
    }

    public Item createItem() {
        Item item = new Item(rnd.nextInt(13));
        /*item.id = rnd.nextInt(13);
        item.n = Global.itemDB[item.id].n;
        item.n1 = Global.itemDB[item.id].n1;
        item.type = Global.itemDB[item.id].type;
        item.val1 = Global.itemDB[item.id].val1;
        item.val2 = Global.itemDB[item.id].val2;
        item.val3 = Global.itemDB[item.id].val3;
        item.property = Global.itemDB[item.id].property;*/
        return item;
    }

    public Item createItem(int t) {
        Item item = new Item(t);
        /*item.id = t;
        item.n = Global.itemDB[item.id].n;
        item.n1 = Global.itemDB[item.id].n1;
        item.type = Global.itemDB[item.id].type;
        item.val1 = Global.itemDB[item.id].val1;
        item.val2 = Global.itemDB[item.id].val2;
        item.val3 = Global.itemDB[item.id].val3;
        item.property = Global.itemDB[item.id].property;*/
        return item;
    }

    public void createMob(int x, int y) {
        MobList temp = new MobList(rnd.nextInt(4));
        temp.x = x;
        temp.y = y;
        Global.INSTANCE.getMap()[x][y].addMob(temp);
        addInQueue(temp);
    }

    public void createMob(int x, int y, int t) {
        MobList temp = new MobList(t);
        temp.x = x;
        temp.y = y;
        Global.INSTANCE.getMap()[x][y].addMob(temp);
        addInQueue(temp);
    }

    public void addInQueue(MobList mob) {
        mob.turnCount = turnCount + mob.mob.getSpd();
        if (firstMob == null) {
            firstMob = mob;
        } else {
            MobList cur;
            boolean b = false;
            for (cur = firstMob; cur.turnCount <= mob.turnCount; cur = cur.next)
                if (cur.next == null) {
                    b = true;
                    break;
                }
            if (b) {
                cur.next = mob;
                cur.next.next = null;
            } else {
                MobList temp;
                if (cur == firstMob) {
                    temp = firstMob;
                    firstMob = mob;
                    firstMob.next = temp;
                } else {
                    for (temp = firstMob; temp.next != cur; temp = temp.next) {
                    }
                    temp.next = mob;
                    temp.next.next = cur;
                }
            }
        }
    }

    public void isCollision(int mx, int my) {
        if (mx > -1 && mx < mw && my < mh && my > -1) {
            move = true;
            if (Global.INSTANCE.getMap()[mx][my].use) {
                switch (Game.getObject(mx, my)) {
                    case 31:
                        fillArea(mx, my, 1, 1, Game.getFloor(mx, my), 32);
                        Global.INSTANCE.getMapview().addLine("????? ???????");
                        move = false;
                        turn = false;
                        break;
                    case 33:
                        fillArea(mx, my, 1, 1, Game.getFloor(mx, my), 34);
                        Global.INSTANCE.getMapview().setMDrawLog(false);
                        Global.INSTANCE.getMapview().initProgressBar(33, 159);
                        move = false;
                        turn = false;
                        break;
                    case 36:
                        Global.INSTANCE.getMapview().setMDrawLog(false);
                        Global.INSTANCE.getMapview().initProgressBar(36, 259);
                        move = false;
                        turn = false;
                        break;
                }
            }
            if (turn && Global.INSTANCE.getMap()[mx][my].hasMob())
                attack(Global.INSTANCE.getMap()[mx][my]);
            if (turn)
                if (Game.getObject(mx, my) == 44) {
                    Global.INSTANCE.getHero().modifyStat(5, rnd.nextInt(3) + 1, -1);
                    Global.INSTANCE.getMapview().addLine("?????? ?????!");
                    if (Global.INSTANCE.getHero().getStat(5) < 1) {
                        lastAttack = Bitmap.createScaledBitmap(Global.INSTANCE.getTiles()[44].getImg(), 72, 72, false);
                        Global.INSTANCE.getMapview().setMDrawDeathScreen(true);
                    }
                }
            if (!Global.INSTANCE.getMap()[mx][my].psb && turn) {
                Global.INSTANCE.getMapview().addLine("??????????? ?? ????");
                v.vibrate(30);
                move = false;
            } else {
                turn = false;
            }
        } else {
            move = false;
        }
    }

    public void move(int mx, int my) {
        Global.INSTANCE.getMapview().setMx(mx);
        Global.INSTANCE.getMapview().setMy(my);
        isCollision(Global.INSTANCE.getHero().mx + mx, Global.INSTANCE.getHero().my + my);
        if (move) {
            Global.INSTANCE.getHero().mx += mx;
            Global.INSTANCE.getHero().my += my;
            Global.INSTANCE.getMapview().setCamx(Global.INSTANCE.getMapview().getCamx() + mx);
            Global.INSTANCE.getMapview().setCamy(Global.INSTANCE.getMapview().getCamy() + my);
        }
        Global.INSTANCE.getMapview().calculateLineOfSight(Global.INSTANCE.getHero().mx, Global.INSTANCE.getHero().my);
        if (!turn) {
            tap = false;
            if (mx == 1) Global.INSTANCE.getHero().side = false;
            if (mx == -1) Global.INSTANCE.getHero().side = true;
        }
        if ((mx != 0 || my != 0) && Global.INSTANCE.getMap()[Global.INSTANCE.getHero().mx][Global.INSTANCE.getHero().my].hasItem()) {
            if (Global.INSTANCE.getMap()[Global.INSTANCE.getHero().mx][Global.INSTANCE.getHero().my].head.next == null)
                Global.INSTANCE.getMapview().addLine(Global.INSTANCE.getMap()[Global.INSTANCE.getHero().mx][Global.INSTANCE.getHero().my].head.item.n + " ????? ?? ?????");
            else
                Global.INSTANCE.getMapview().addLine("????????? ????????? ????? ?? ?????");
        }
        updateZone();
    }

    public void spread(int i1, int j1, int c) {
        for (int i = i1 - 1; i < i1 + 2; i++)
            for (int j = j1 - 1; j < j1 + 2; j++)
                if (zone[i][j] == defValue && Global.INSTANCE.getMap()[Global.INSTANCE.getMapview().getCamx() - 1 + i][Global.INSTANCE.getMapview().getCamy() - 1 + j].psb && !Global.INSTANCE.getMap()[Global.INSTANCE.getMapview().getCamx() - 1 + i][Global.INSTANCE.getMapview().getCamy() - 1 + j].hasMob())
                    zone[i][j] = c;
    }

    public void clearZone() {
        for (int i = 0; i < 11; i++)
            for (int j = 0; j < 11; j++)
                zone[i][j] = defValue;
        zone[5][5] = 0;
    }

    public void updateZone() {
        clearZone();
        int xl, xr, yl, yr;
        xl = (Global.INSTANCE.getMapview().getCamx() - 1 < 1) ? 1 : Global.INSTANCE.getMapview().getCamx() - 1;
        yl = (Global.INSTANCE.getMapview().getCamy() - 1 < 1) ? 1 : Global.INSTANCE.getMapview().getCamy() - 1;
        xr = (Global.INSTANCE.getMapview().getCamx() + 10 > mw - 2) ? mw - 2 : Global.INSTANCE.getMapview().getCamx() + 10;
        yr = (Global.INSTANCE.getMapview().getCamy() + 10 > mh - 2) ? mh - 2 : Global.INSTANCE.getMapview().getCamy() + 10;
        for (int c = 0; c < 5; c++)
            for (int i = xl; i < xr; i++)
                for (int j = yl; j < yr; j++)
                    if (zone[i - xl][j - yl] == c)
                        spread(i - xl, j - yl, c + 1);
    }

    public void pickupItem() {
        v.vibrate(30);
        move(0, 0);
    }

    public void attack(MapClass map) {
        int att = rnd.nextInt(20) + 1 + Global.INSTANCE.getHero().getStat(11);
        if (att >= map.mob.mob.getDef()) {
            int u = rnd.nextInt(Global.INSTANCE.getHero().getStat(13) - Global.INSTANCE.getHero().getStat(12) + 1) + Global.INSTANCE.getHero().getStat(12) - map.mob.mob.getArm();
            if (u < 1) {
                u = 1;
            }
            map.mob.mob.setHp(map.mob.mob.getHp() - u);
            Global.INSTANCE.getMapview().addLine(map.mob.mob.getName() + " ???????? ????");
            if (map.mob.mob.getHp() < 1) {
                Global.INSTANCE.getMapview().addLine(map.mob.mob.getName() + " ???????");
                Global.INSTANCE.getHero().modifyStat(20, map.mob.t, 1);
                if (map.mob.t == maxMobs - 1)
                    Global.INSTANCE.getMapview().setMDrawWinScreen(true);
                deleteMob(map);
                int x4, y4;
                do {
                    x4 = rnd.nextInt(Global.INSTANCE.getGame().mw);
                    y4 = rnd.nextInt(Global.INSTANCE.getGame().mh);
                }
                while (!Global.INSTANCE.getMap()[x4][y4].psb || Global.INSTANCE.getMap()[x4][y4].see || Global.INSTANCE.getMap()[x4][y4].hasMob());
                int en = rnd.nextInt(Global.INSTANCE.getGame().maxMobs - curLvls - 1) + curLvls;
                if (en < 3 && rnd.nextInt(3) == 0) {
                    if (Global.INSTANCE.getMap()[x4 - 1][y4].psb && !Global.INSTANCE.getMap()[x4 - 1][y4].hasItem())
                        Global.INSTANCE.getGame().createMob(x4 - 1, y4, en);
                    if (Global.INSTANCE.getMap()[x4 + 1][y4].psb && !Global.INSTANCE.getMap()[x4 + 1][y4].hasItem())
                        Global.INSTANCE.getGame().createMob(x4 + 1, y4, en);
                }
                Global.INSTANCE.getGame().createMob(x4, y4, en);
            }
        } else {
            Global.INSTANCE.getMapview().addLine("??????");
        }
        turn = false;
        move = false;
    }

    public void deleteMob(MapClass map) {
        MobList temp = map.mob;
        if (map.mob == firstMob) {
            firstMob = firstMob.next;
        } else {
            MobList cur;
            for (cur = firstMob; cur.next != map.mob; cur = cur.next) {
            }
            cur.next = map.mob.next;
        }
        temp.map.mob = null;
        temp.map = null;
    }

    public void mobAttack(MobList mob) {
        int att = rnd.nextInt(20) + 1 + mob.mob.getArm();
        if (att >= Global.INSTANCE.getHero().getStat(19)) {
            int u = mob.mob.getDmg() - Global.INSTANCE.getHero().getStat(22);
            if (u < 1) {
                u = 1;
            }
            Global.INSTANCE.getHero().modifyStat(5, u, -1);
            Global.INSTANCE.getMapview().addLine(mob.mob.getName() + " ??????? ????");
        } else {
            Global.INSTANCE.getMapview().addLine(mob.mob.getName() + " ?????????????");
        }
        if (Global.INSTANCE.getHero().getStat(5) < 1) {
            Global.INSTANCE.getHero().modifyStat(5, Global.INSTANCE.getHero().getStat(5), -1);
            lastAttack = Bitmap.createScaledBitmap(mob.getImg(0), 72, 72, false);
            Global.INSTANCE.getMapview().setMDrawDeathScreen(true);
        }
    }

    private void loading() {
        loadStats();
        loadTiles();
        loadItems();
        loadMobs();
        // Images loading
        AssetHelper assetHelper = Global.INSTANCE.getMAssetHelper();
        Bitmap img = assetHelper.getBitmapFromAsset("character_animation_sheet");
        for (int x = 0; x < 4; x++)
            Global.INSTANCE.getHero().img[x] = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, 0, 24, 24), step, step, false);
        bag = Bitmap.createScaledBitmap(Bitmap.createBitmap(assetHelper.getBitmapFromAsset("bag"), 0, 0, 24, 24), step, step, false);
        a = assetHelper.getBitmapFromAsset("character_icon");
        b = assetHelper.getBitmapFromAsset("inventory_icon");
        backIcon = assetHelper.getBitmapFromAsset("back_icon");
        d = assetHelper.getBitmapFromAsset("ery");
        j = assetHelper.getBitmapFromAsset("skip_turn_icon");
        img = assetHelper.getBitmapFromAsset("tileset");
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                Global.INSTANCE.getTiles()[y * 10 + x].setImg(Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, y * 24, 24, 24), step, step, false));
        img = assetHelper.getBitmapFromAsset("items_sheet");
        for (int y = 0; y < 2; y++)
            for (int x = 0; x < 10; x++)
                Global.INSTANCE.getItemDB()[y * 10 + x].setImg(Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, y * 24, 24, 24), step, step, false));
        img = assetHelper.getBitmapFromAsset("mobs_sheet");
        for (int x = 0; x < maxMobs; x++) {
            Global.INSTANCE.getMobDB()[x].getImg()[0] = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, 0, 24, 24), step, step, false);
            Global.INSTANCE.getMobDB()[x].getImg()[1] = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, 24, 24, 24), step, step, false);
        }
    }

    private void loadStats() {
        try {
            int i = 0;
            XmlPullParser parser = getResources().getXml(R.xml.stats);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("stat")) {
                    Global.INSTANCE.getStats()[i].setN(parser.getAttributeValue(0));
                    Global.INSTANCE.getStats()[i].setS(parser.getAttributeValue(1).equals("t"));
                    Global.INSTANCE.getStats()[i++].setM(parser.getAttributeValue(2).equals("t"));
                    parser.next();
                }
                parser.next();
            }
        } catch (Throwable t) {
        }
    }

    private void loadTiles() {
        try {
            int i = 0;
            XmlPullParser parser = getResources().getXml(R.xml.tiles);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("tile")) {
                    Global.INSTANCE.getTiles()[i].setPsb(parser.getAttributeValue(0).equals("t"));
                    Global.INSTANCE.getTiles()[i].setVis(parser.getAttributeValue(1).equals("t"));
                    Global.INSTANCE.getTiles()[i++].setUse(parser.getAttributeValue(2).equals("t"));
                }
                parser.next();
            }
            i = 30;
            parser = getResources().getXml(R.xml.objects);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("object")) {
                    Global.INSTANCE.getTiles()[i].setPsb(parser.getAttributeValue(0).equals("t"));
                    Global.INSTANCE.getTiles()[i].setVis(parser.getAttributeValue(1).equals("t"));
                    Global.INSTANCE.getTiles()[i].setUse(parser.getAttributeValue(2).equals("t"));
                    Global.INSTANCE.getTiles()[i++].setWall(parser.getAttributeValue(3).equals("t"));
                }
                parser.next();
            }
        } catch (Throwable t) {
        }
    }

    private void loadItems() {
        try {
            int i = 0;
            XmlPullParser parser = getResources().getXml(R.xml.items);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("weapon")) {
                        Global.INSTANCE.getItemDB()[i].type = 1;
                        Global.INSTANCE.getItemDB()[i].n = parser.getAttributeValue(0);
                        Global.INSTANCE.getItemDB()[i].n1 = parser.getAttributeValue(1);
                        Global.INSTANCE.getItemDB()[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.INSTANCE.getItemDB()[i].val2 = Integer.parseInt(parser.getAttributeValue(3));
                        Global.INSTANCE.getItemDB()[i].val3 = Integer.parseInt(parser.getAttributeValue(4));
                        Global.INSTANCE.getItemDB()[i++].property = parser.getAttributeValue(5).equals("t");
                    }
                    if (parser.getName().equals("shield")) {
                        Global.INSTANCE.getItemDB()[i].type = 2;
                        Global.INSTANCE.getItemDB()[i].n = parser.getAttributeValue(0);
                        Global.INSTANCE.getItemDB()[i].n1 = parser.getAttributeValue(1);
                        Global.INSTANCE.getItemDB()[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.INSTANCE.getItemDB()[i++].val2 = Integer.parseInt(parser.getAttributeValue(3));
                    }
                    if (parser.getName().equals("armor")) {
                        Global.INSTANCE.getItemDB()[i].type = 3;
                        Global.INSTANCE.getItemDB()[i].n = parser.getAttributeValue(0);
                        Global.INSTANCE.getItemDB()[i].n1 = parser.getAttributeValue(1);
                        Global.INSTANCE.getItemDB()[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.INSTANCE.getItemDB()[i++].val2 = Integer.parseInt(parser.getAttributeValue(3));
                    }
                    if (parser.getName().equals("item")) {
                        Global.INSTANCE.getItemDB()[i].type = 5;
                        Global.INSTANCE.getItemDB()[i].n = parser.getAttributeValue(0);
                        Global.INSTANCE.getItemDB()[i].n1 = parser.getAttributeValue(1);
                        Global.INSTANCE.getItemDB()[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.INSTANCE.getItemDB()[i++].val2 = Integer.parseInt(parser.getAttributeValue(3));
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {
        }
    }

    private void loadMobs() {
        try {
            int i = 0;
            XmlPullParser parser = getResources().getXml(R.xml.mobs);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("mob")) {
                    Global.INSTANCE.getMobDB()[i].getMob().setName(parser.getAttributeValue(0));
                    Global.INSTANCE.getMobDB()[i].getMob().setHp(Integer.parseInt(parser.getAttributeValue(1)));
                    Global.INSTANCE.getMobDB()[i].getMob().setAtt(Integer.parseInt(parser.getAttributeValue(2)));
                    Global.INSTANCE.getMobDB()[i].getMob().setDef(Integer.parseInt(parser.getAttributeValue(3)));
                    Global.INSTANCE.getMobDB()[i].getMob().setArm(Integer.parseInt(parser.getAttributeValue(4)));
                    Global.INSTANCE.getMobDB()[i].getMob().setSpd(Integer.parseInt(parser.getAttributeValue(5)));
                    Global.INSTANCE.getMobDB()[i++].getMob().setDmg(Integer.parseInt(parser.getAttributeValue(6)));
                }
                parser.next();
            }
        } catch (Throwable t) {
        }
    }

    public void createItem(int x4, int y4) {
        Item item = createItem();
        Global.INSTANCE.getMap()[x4][y4].addItem(item);
    }

    public void createItem(int x4, int y4, int t) {
        Item item = createItem(t);
        Global.INSTANCE.getMap()[x4][y4].addItem(item);
    }

    public void modifyTile(int px, int py, int f, int o) {
        Global.INSTANCE.getMap()[px][py].psb = Global.INSTANCE.getTiles()[f].getPsb();
        Global.INSTANCE.getMap()[px][py].vis = Global.INSTANCE.getTiles()[f].getVis();
        Global.INSTANCE.getMap()[px][py].use = Global.INSTANCE.getTiles()[f].getUse();
        Global.INSTANCE.getMap()[px][py].psb = Global.INSTANCE.getTiles()[o].getPsb();
        Global.INSTANCE.getMap()[px][py].vis = Global.INSTANCE.getTiles()[o].getVis();
        Global.INSTANCE.getMap()[px][py].use = Global.INSTANCE.getTiles()[o].getUse();
    }

    public void fillArea(int sx, int sy, int lx1, int ly1, int f, int o) {
        for (int y = sy; y < sy + ly1; y++) {
            for (int x = sx; x < sx + lx1; x++) {
                Global.INSTANCE.getMap()[x][y].f = f;
                Global.INSTANCE.getMap()[x][y].o = o;
                modifyTile(x, y, f, o);
            }
        }
    }

    public void mobTurn(MobList mob) {
        int a = min(mob);
        if (a == 0) {
            mobAttack(mob);
        } else {
            int x4 = 0, y4 = 0;
            int x = mob.x - Global.INSTANCE.getHero().mx + 5, y = mob.y - Global.INSTANCE.getHero().my + 5;
            boolean u, d, l, r;
            u = d = l = r = false;
            for (int c = -1; c < 2; c++) {
                if (zone[x + c][y - 1] == a) u = true;
                if (zone[x + c][y + 1] == a) d = true;
                if (zone[x - 1][y + c] == a) l = true;
                if (zone[x + 1][y + c] == a) r = true;
            }
            if (l ^ r) {
                if (l) x4 = -1;
                if (r) x4 = 1;
            }
            if (u ^ d) {
                if (u) y4 = -1;
                if (d) y4 = 1;
            }
            if (Global.INSTANCE.getMap()[mob.x + x4][mob.y + y4].psb && !Global.INSTANCE.getMap()[mob.x + x4][mob.y + y4].hasMob()) {
                Global.INSTANCE.getMap()[mob.x][mob.y].deleteMob();
                mob.x += x4;
                mob.y += y4;
                Global.INSTANCE.getMap()[mob.x][mob.y].addMob(mob);
                updateZone();
            }
        }
    }

    public void newTurnCount() {
        Global.INSTANCE.getHero().init = Global.INSTANCE.getHero().getStat(25);
    }

    public int min(MobList mob) {
        int a = defValue;
        for (int x1 = -1; x1 < 2; x1++)
            for (int y1 = -1; y1 < 2; y1++)
                if (zone[mob.x - Global.INSTANCE.getHero().mx + 5 + x1][mob.y - Global.INSTANCE.getHero().my + 5 + y1] < a)
                    a = zone[mob.x - Global.INSTANCE.getHero().mx + 5 + x1][mob.y - Global.INSTANCE.getHero().my + 5 + y1];
        return a;
    }

    public void mainBody() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (--Global.INSTANCE.getHero().init == 0) {
                        newTurnCount();
                        if (--Global.INSTANCE.getHero().cregen == 0) {
                            Global.INSTANCE.getHero().cregen = Global.INSTANCE.getHero().regen;
                            if (Global.INSTANCE.getHero().getStat(5) != Global.INSTANCE.getHero().getStat(6))
                                Global.INSTANCE.getHero().modifyStat(5, 1, 1);
                        }
                        tap = turn = move = true;
                        while (tap) ;
                    }
                    if (firstMob != null)
                        while (firstMob.turnCount <= turnCount) {
                            MobList temp = firstMob;
                            firstMob = firstMob.next;
                            if (Math.abs(temp.x - Global.INSTANCE.getHero().mx) < 5 && Math.abs(temp.y - Global.INSTANCE.getHero().my) < 5)
                                mobTurn(temp);
                            addInQueue(temp);
                        }
                    turnCount++;
                }
            }
        });
        t.start();
    }
}
