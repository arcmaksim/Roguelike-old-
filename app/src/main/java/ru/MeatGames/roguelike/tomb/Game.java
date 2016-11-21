package ru.MeatGames.roguelike.tomb;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;

import org.xmlpull.v1.XmlPullParser;

import java.util.Random;

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
    public Bitmap c;
    public Bitmap d;
    public Bitmap j;
    public Random rnd;
    public boolean lines = false;
    public int scr = 0;
    public Bitmap lastAttack;
    public Bitmap bag;
    public MobList firstMob;
    public int[][] zone;

    public static Bitmap getHeroImg(int n) {
        return Global.hero.img[n];
    }

    public static int getFloor(int x, int y) {
        return Global.map[x][y].f;
    }

    public static int getObject(int x, int y) {
        return Global.map[x][y].o;
    }

    protected void onCreate(Bundle w) {
        super.onCreate(w);

        Global.game = this;
        rnd = new Random();

        Global.map = new MapClass[mw][mh];
        for (int x = 0; x < mw; x++)
            for (int y = 0; y < mh; y++)
                Global.map[x][y] = new MapClass();

        Global.tiles = new TileDB[maxTiles];
        for (int x = 0; x < maxTiles; x++)
            Global.tiles[x] = new TileDB();

        Global.itemDB = new ItemDB[maxItems];
        for (int x = 0; x < maxItems; x++)
            Global.itemDB[x] = new ItemDB();

        Global.mobDB = new MobDB[maxMobs];
        for (int x = 0; x < maxMobs; x++)
            Global.mobDB[x] = new MobDB();

        Global.stats = new StatsDB[Global.game.maxStats];
        for (int x = 0; x < Global.game.maxStats; x++)
            Global.stats[x] = new StatsDB();

        Global.hero = new HeroClass();
        Global.mapg = new MapGenerationClass();
        Global.mmview = new MainMenu(this);
        Global.mapview = new MapView(this);
        Global.invview = new InventoryView(this);
        Global.stsview = new StatsView(this);
        Global.bview = new BrezenhamView(this);

        zone = new int[11][11];

        font = Typeface.createFromAsset(this.getAssets(), "fonts/crancyr.ttf");
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loading();
        newGame();
        mainBody();
    }

    public void onBackPressed() {
        if (scr == 0 && !Global.mapview.prgb)
            Global.mapview.exit = !Global.mapview.exit;
    }

    public void exitGame() {
        finish();
    }

    public void newGame() {
        Global.hero.newHero();
        curLvls = 0;
        Global.mapg.mapGen();
        Global.mapview.clearLog();
        changeScreen(4);
    }

    public void changeScreen(int u) {
        scr = u;
        switch (u) {
            case 0:
                setContentView(Global.mapview);
                Global.mapview.requestFocus();
                break;
            case 1:
                Global.invview.fillList();
                setContentView(Global.invview);
                Global.invview.requestFocus();
                break;
            case 2:
                setContentView(Global.stsview);
                Global.stsview.requestFocus();
                break;
            case 3:
                setContentView(Global.bview);
                Global.bview.requestFocus();
                break;
            case 4:
                setContentView(Global.mmview);
                Global.mmview.requestFocus();
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
        Global.map[x][y].addMob(temp);
        addInQueue(temp);
    }

    public void createMob(int x, int y, int t) {
        MobList temp = new MobList(t);
        temp.x = x;
        temp.y = y;
        Global.map[x][y].addMob(temp);
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
            if (Global.map[mx][my].use) {
                switch (Game.getObject(mx, my)) {
                    case 31:
                        fillArea(mx, my, 1, 1, Game.getFloor(mx, my), 32);
                        Global.mapview.addLine("????? ???????");
                        move = false;
                        turn = false;
                        break;
                    case 33:
                        fillArea(mx, my, 1, 1, Game.getFloor(mx, my), 34);
                        Global.mapview.line = false;
                        Global.mapview.initPrgb(33, 159);
                        move = false;
                        turn = false;
                        break;
                    case 36:
                        Global.mapview.line = false;
                        Global.mapview.initPrgb(36, 259);
                        move = false;
                        turn = false;
                        break;
                }
            }
            if (turn && Global.map[mx][my].hasMob())
                attack(Global.map[mx][my]);
            if (turn)
                if (Game.getObject(mx, my) == 44) {
                    Global.hero.modifyStat(5, rnd.nextInt(3) + 1, -1);
                    Global.mapview.addLine("?????? ?????!");
                    if (Global.hero.getStat(5) < 1) {
                        lastAttack = Bitmap.createScaledBitmap(Global.tiles[44].img, 72, 72, false);
                        Global.mapview.death = true;
                    }
                }
            if (!Global.map[mx][my].psb && turn) {
                Global.mapview.addLine("??????????? ?? ????");
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
        Global.mapview.mx = mx;
        Global.mapview.my = my;
        isCollision(Global.hero.mx + mx, Global.hero.my + my);
        if (move) {
            Global.hero.mx += mx;
            Global.hero.my += my;
            Global.mapview.camx += mx;
            Global.mapview.camy += my;
        }
        Global.mapview.los(Global.hero.mx, Global.hero.my);
        if (!turn) {
            tap = false;
            if (mx == 1) Global.hero.side = false;
            if (mx == -1) Global.hero.side = true;
        }
        if ((mx != 0 || my != 0) && Global.map[Global.hero.mx][Global.hero.my].hasItem()) {
            if (Global.map[Global.hero.mx][Global.hero.my].head.next == null)
                Global.mapview.addLine(Global.map[Global.hero.mx][Global.hero.my].head.item.n + " ????? ?? ?????");
            else
                Global.mapview.addLine("????????? ????????? ????? ?? ?????");
        }
        updateZone();
    }

    public void spread(int i1, int j1, int c) {
        for (int i = i1 - 1; i < i1 + 2; i++)
            for (int j = j1 - 1; j < j1 + 2; j++)
                if (zone[i][j] == defValue && Global.map[Global.mapview.camx - 1 + i][Global.mapview.camy - 1 + j].psb && !Global.map[Global.mapview.camx - 1 + i][Global.mapview.camy - 1 + j].hasMob())
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
        xl = (Global.mapview.camx - 1 < 1) ? 1 : Global.mapview.camx - 1;
        yl = (Global.mapview.camy - 1 < 1) ? 1 : Global.mapview.camy - 1;
        xr = (Global.mapview.camx + 10 > mw - 2) ? mw - 2 : Global.mapview.camx + 10;
        yr = (Global.mapview.camy + 10 > mh - 2) ? mh - 2 : Global.mapview.camy + 10;
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
        int att = rnd.nextInt(20) + 1 + Global.hero.getStat(11);
        if (att >= map.mob.mob.getDef()) {
            int u = rnd.nextInt(Global.hero.getStat(13) - Global.hero.getStat(12) + 1) + Global.hero.getStat(12) - map.mob.mob.getArm();
            if (u < 1) {
                u = 1;
            }
            map.mob.mob.setHp(map.mob.mob.getHp() - u);
            Global.mapview.addLine(map.mob.mob.getName() + " ???????? ????");
            if (map.mob.mob.getHp() < 1) {
                Global.mapview.addLine(map.mob.mob.getName() + " ???????");
                Global.hero.modifyStat(20, map.mob.t, 1);
                if (map.mob.t == maxMobs - 1)
                    Global.mapview.win = true;
                deleteMob(map);
                int x4, y4;
                do {
                    x4 = rnd.nextInt(Global.game.mw);
                    y4 = rnd.nextInt(Global.game.mh);
                }
                while (!Global.map[x4][y4].psb || Global.map[x4][y4].see || Global.map[x4][y4].hasMob());
                int en = rnd.nextInt(Global.game.maxMobs - curLvls - 1) + curLvls;
                if (en < 3 && rnd.nextInt(3) == 0) {
                    if (Global.map[x4 - 1][y4].psb && !Global.map[x4 - 1][y4].hasItem())
                        Global.game.createMob(x4 - 1, y4, en);
                    if (Global.map[x4 + 1][y4].psb && !Global.map[x4 + 1][y4].hasItem())
                        Global.game.createMob(x4 + 1, y4, en);
                }
                Global.game.createMob(x4, y4, en);
            }
        } else {
            Global.mapview.addLine("??????");
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
        if (att >= Global.hero.getStat(19)) {
            int u = mob.mob.getDmg() - Global.hero.getStat(22);
            if (u < 1) {
                u = 1;
            }
            Global.hero.modifyStat(5, u, -1);
            Global.mapview.addLine(mob.mob.getName() + " ??????? ????");
        } else {
            Global.mapview.addLine(mob.mob.getName() + " ?????????????");
        }
        if (Global.hero.getStat(5) < 1) {
            Global.hero.modifyStat(5, Global.hero.getStat(5), -1);
            lastAttack = Bitmap.createScaledBitmap(mob.getImg(0), 72, 72, false);
            Global.mapview.death = true;
        }
    }

    private void loading() {
        loadStats();
        loadTiles();
        loadItems();
        loadMobs();
        // Images loading
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.asdq);
        for (int x = 0; x < 4; x++)
            Global.hero.img[x] = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, 0, 24, 24), step, step, false);
        bag = Bitmap.createScaledBitmap(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bag), 0, 0, 24, 24), step, step, false);
        a = BitmapFactory.decodeResource(getResources(), R.drawable.ert);
        b = BitmapFactory.decodeResource(getResources(), R.drawable.asd);
        c = BitmapFactory.decodeResource(getResources(), R.drawable.a321);
        d = BitmapFactory.decodeResource(getResources(), R.drawable.ery);
        j = BitmapFactory.decodeResource(getResources(), R.drawable.time);
        img = BitmapFactory.decodeResource(getResources(), R.drawable.tileset);
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                Global.tiles[y * 10 + x].img = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, y * 24, 24, 24), step, step, false);
        img = BitmapFactory.decodeResource(getResources(), R.drawable.items);
        for (int y = 0; y < 2; y++)
            for (int x = 0; x < 10; x++)
                Global.itemDB[y * 10 + x].img = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, y * 24, 24, 24), step, step, false);
        img = BitmapFactory.decodeResource(getResources(), R.drawable.mobs);
        for (int x = 0; x < maxMobs; x++) {
            Global.mobDB[x].img[0] = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, 0, 24, 24), step, step, false);
            Global.mobDB[x].img[1] = Bitmap.createScaledBitmap(Bitmap.createBitmap(img, x * 24, 24, 24, 24), step, step, false);
        }
    }

    private void loadStats() {
        try {
            int i = 0;
            XmlPullParser parser = getResources().getXml(R.xml.stats);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("stat")) {
                    Global.stats[i].n = parser.getAttributeValue(0);
                    Global.stats[i].s = parser.getAttributeValue(1).equals("t");
                    Global.stats[i++].m = parser.getAttributeValue(2).equals("t");
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
                    Global.tiles[i].psb = parser.getAttributeValue(0).equals("t");
                    Global.tiles[i].vis = parser.getAttributeValue(1).equals("t");
                    Global.tiles[i++].use = parser.getAttributeValue(2).equals("t");
                }
                parser.next();
            }
            i = 30;
            parser = getResources().getXml(R.xml.objects);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("object")) {
                    Global.tiles[i].psb = parser.getAttributeValue(0).equals("t");
                    Global.tiles[i].vis = parser.getAttributeValue(1).equals("t");
                    Global.tiles[i].use = parser.getAttributeValue(2).equals("t");
                    Global.tiles[i++].isWall = parser.getAttributeValue(3).equals("t");
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
                        Global.itemDB[i].type = 1;
                        Global.itemDB[i].n = parser.getAttributeValue(0);
                        Global.itemDB[i].n1 = parser.getAttributeValue(1);
                        Global.itemDB[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.itemDB[i].val2 = Integer.parseInt(parser.getAttributeValue(3));
                        Global.itemDB[i].val3 = Integer.parseInt(parser.getAttributeValue(4));
                        Global.itemDB[i++].property = parser.getAttributeValue(5).equals("t");
                    }
                    if (parser.getName().equals("shield")) {
                        Global.itemDB[i].type = 2;
                        Global.itemDB[i].n = parser.getAttributeValue(0);
                        Global.itemDB[i].n1 = parser.getAttributeValue(1);
                        Global.itemDB[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.itemDB[i++].val2 = Integer.parseInt(parser.getAttributeValue(3));
                    }
                    if (parser.getName().equals("armor")) {
                        Global.itemDB[i].type = 3;
                        Global.itemDB[i].n = parser.getAttributeValue(0);
                        Global.itemDB[i].n1 = parser.getAttributeValue(1);
                        Global.itemDB[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.itemDB[i++].val2 = Integer.parseInt(parser.getAttributeValue(3));
                    }
                    if (parser.getName().equals("item")) {
                        Global.itemDB[i].type = 5;
                        Global.itemDB[i].n = parser.getAttributeValue(0);
                        Global.itemDB[i].n1 = parser.getAttributeValue(1);
                        Global.itemDB[i].val1 = Integer.parseInt(parser.getAttributeValue(2));
                        Global.itemDB[i++].val2 = Integer.parseInt(parser.getAttributeValue(3));
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
                    Global.mobDB[i].mob.setName(parser.getAttributeValue(0));
                    Global.mobDB[i].mob.setHp(Integer.parseInt(parser.getAttributeValue(1)));
                    Global.mobDB[i].mob.setAtt(Integer.parseInt(parser.getAttributeValue(2)));
                    Global.mobDB[i].mob.setDef(Integer.parseInt(parser.getAttributeValue(3)));
                    Global.mobDB[i].mob.setArm(Integer.parseInt(parser.getAttributeValue(4)));
                    Global.mobDB[i].mob.setSpd(Integer.parseInt(parser.getAttributeValue(5)));
                    Global.mobDB[i++].mob.setDmg(Integer.parseInt(parser.getAttributeValue(6)));
                }
                parser.next();
            }
        } catch (Throwable t) {
        }
    }

    public void createItem(int x4, int y4) {
        Item item = createItem();
        Global.map[x4][y4].addItem(item);
    }

    public void createItem(int x4, int y4, int t) {
        Item item = createItem(t);
        Global.map[x4][y4].addItem(item);
    }

    public void modifyTile(int px, int py, int f, int o) {
        Global.map[px][py].psb = Global.tiles[f].psb;
        Global.map[px][py].vis = Global.tiles[f].vis;
        Global.map[px][py].use = Global.tiles[f].use;
        Global.map[px][py].psb = Global.tiles[o].psb;
        Global.map[px][py].vis = Global.tiles[o].vis;
        Global.map[px][py].use = Global.tiles[o].use;
    }

    public void fillArea(int sx, int sy, int lx1, int ly1, int f, int o) {
        for (int y = sy; y < sy + ly1; y++) {
            for (int x = sx; x < sx + lx1; x++) {
                Global.map[x][y].f = f;
                Global.map[x][y].o = o;
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
            int x = mob.x - Global.hero.mx + 5, y = mob.y - Global.hero.my + 5;
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
            if (Global.map[mob.x + x4][mob.y + y4].psb && !Global.map[mob.x + x4][mob.y + y4].hasMob()) {
                Global.map[mob.x][mob.y].deleteMob();
                mob.x += x4;
                mob.y += y4;
                Global.map[mob.x][mob.y].addMob(mob);
                updateZone();
            }
        }
    }

    public void newTurnCount() {
        Global.hero.init = Global.hero.getStat(25);
    }

    public int min(MobList mob) {
        int a = defValue;
        for (int x1 = -1; x1 < 2; x1++)
            for (int y1 = -1; y1 < 2; y1++)
                if (zone[mob.x - Global.hero.mx + 5 + x1][mob.y - Global.hero.my + 5 + y1] < a)
                    a = zone[mob.x - Global.hero.mx + 5 + x1][mob.y - Global.hero.my + 5 + y1];
        return a;
    }

    public void mainBody() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (--Global.hero.init == 0) {
                        newTurnCount();
                        if (--Global.hero.cregen == 0) {
                            Global.hero.cregen = Global.hero.regen;
                            if (Global.hero.getStat(5) != Global.hero.getStat(6))
                                Global.hero.modifyStat(5, 1, 1);
                        }
                        tap = turn = move = true;
                        while (tap) ;
                    }
                    if (firstMob != null)
                        while (firstMob.turnCount <= turnCount) {
                            MobList temp = firstMob;
                            firstMob = firstMob.next;
                            if (Math.abs(temp.x - Global.hero.mx) < 5 && Math.abs(temp.y - Global.hero.my) < 5)
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
