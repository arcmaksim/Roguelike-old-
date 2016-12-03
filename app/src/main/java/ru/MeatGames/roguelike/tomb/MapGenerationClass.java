package ru.MeatGames.roguelike.tomb;

import java.util.Random;

import ru.MeatGames.roguelike.tomb.db.RoomDBClass;
import ru.MeatGames.roguelike.tomb.model.RoomClass;

public class MapGenerationClass {

    public RoomClass[] room;
    public RoomDBClass[] room1;
    public int[][] zone;
    public Random rnd;
    public int m;
    public int n;
    public int z = 0;
    public int z1 = 0;
    public int rc;
    public int mr = 70;
    public int xl;
    public int xr;
    public int yl;
    public int yr;

    public MapGenerationClass() {
        rnd = new Random();
        room1 = new RoomDBClass[mr];
        loadingRooms();
    }

    public void loadingRooms() {
        room = new RoomClass[16];
        zone = new int[][]{
                {5030, 5000, 5041},
                {5041, 5000, 5038},
                {5038, 5000, 5041},
                {5041, 5000, 5030}
        };
        room[0] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 11036, 12036, 11036, 5030},
                {11036, 12000, 11000, 12000, 11036},
                {12036, 11000, 12000, 11000, 12036},
                {11036, 12000, 11000, 12000, 11036},
                {5030, 11036, 12036, 11036, 5030}
        };
        room[1] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 5030, 5000, 5000, 5041, 5030},
                {5000, 5000, 5000, 5041, 5038, 5041},
                {5000, 5041, 5000, 5000, 5041, 5000},
                {5041, 5038, 5041, 5000, 5000, 5000},
                {5041, 5038, 5041, 5000, 5000, 5000},
                {5000, 5041, 5000, 5000, 5041, 5000},
                {5000, 5000, 5000, 5041, 5038, 5041},
                {5030, 5030, 5000, 5000, 5041, 5030},
        };
        room[2] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 5030, 5033, 5030, 5030},
                {5033, 5000, 5000, 5000, 5033},
                {5030, 5000, 5042, 5000, 5030},
                {5033, 5000, 5000, 5000, 5033},
                {5030, 5030, 5033, 5030, 5030},
        };
        room[3] = new RoomClass(zone);
        zone = new int[][]{
                {5000, 5000, 5000, 5000, 5000},
                {5000, 6000, 6000, 6000, 5000},
                {5000, 6000, 6033, 6000, 5000},
                {5000, 6000, 6000, 6000, 5000},
                {5000, 5000, 5000, 5000, 5000}
        };
        room[4] = new RoomClass(zone);
        zone = new int[][]{
                {6036, 6000, 6041, 6030},
                {6036, 6000, 6038, 6041},
                {6036, 6000, 6000, 6000},
                {6030, 6036, 6036, 6036}
        };
        room[5] = new RoomClass(zone);
        zone = new int[][]{
                {11030, 12036, 11036, 12036, 11036, 12036, 11036, 12036, 11030},
                {12036, 11000, 12000, 11000, 12000, 11000, 12000, 11000, 12036},
                {11036, 12000, 11036, 12036, 11000, 12036, 11036, 12000, 11036},
                {12036, 11000, 12036, 11000, 12000, 11000, 12036, 11000, 12036},
                {11036, 12000, 11000, 12000, 11036, 12000, 11000, 12000, 11036},
                {12036, 11000, 12036, 11000, 12000, 11000, 12036, 11000, 12036},
                {11036, 12000, 11036, 12036, 11000, 12036, 11036, 12000, 11036},
                {12036, 11000, 12000, 11000, 12000, 11000, 12000, 11000, 12036},
                {11030, 12036, 11036, 12036, 11036, 12036, 11036, 12036, 11030}
        };
        room[6] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 5000, 5000, 5000, 5030},
                {5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5030, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000},
                {5030, 5000, 5000, 5000, 5030},
                {5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5030, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000},
                {5030, 5000, 5000, 5000, 5030}
        };
        room[7] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 5000, 5000, 5000, 5000, 5000, 5030},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5030, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5030, 5000, 5000, 5000, 5000, 5000, 5030}
        };
        room[8] = new RoomClass(zone);
        zone = new int[][]{
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 5000},
                {5000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 5000},
                {5000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 5000},
                {5000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000}
        };
        room[9] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 5000, 6000, 5000, 5030},
                {5000, 5000, 6000, 5000, 5000},
                {6000, 6000, 6000, 6000, 6000},
                {5000, 5000, 6000, 5000, 5000},
                {5030, 5000, 6000, 5000, 5030}
        };
        room[10] = new RoomClass(zone);
        zone = new int[][]{
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5030, 5000, 5000, 5030, 5000, 5000, 5030, 5000, 5000, 5030, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5030, 5000, 5000, 5030, 5000, 5000, 5030, 5000, 5000, 5030, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000}
        };
        room[11] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 5030, 5030, 5000, 5000, 5000, 5000, 5000, 5000, 5030, 5030, 5030},
                {5030, 5030, 5030, 5000, 5000, 5000, 5000, 5000, 5000, 5030, 5030, 5030},
                {5030, 5030, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5030, 5030},
                {5000, 5000, 5000, 5030, 5000, 5000, 5000, 5000, 5030, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000},
                {5000, 5000, 5000, 5030, 5000, 5000, 5000, 5000, 5030, 5000, 5000, 5000},
                {5030, 5030, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5030, 5030},
                {5030, 5030, 5030, 5000, 5000, 5000, 5000, 5000, 5000, 5030, 5030, 5030},
                {5030, 5030, 5030, 5000, 5000, 5000, 5000, 5000, 5000, 5030, 5030, 5030}
        };
        room[12] = new RoomClass(zone);
        zone = new int[][]{
                {11000, 12000, 11000, 12000, 11000},
                {12000, 11038, 12000, 11036, 12000},
                {11000, 12000, 11000, 12036, 11000},
                {12000, 11036, 12000, 11036, 12000},
                {11000, 12036, 11000, 12000, 11000},
                {12000, 11036, 12000, 11038, 12000},
                {11000, 12000, 11000, 12000, 11000}
        };
        room[13] = new RoomClass(zone);
        zone = new int[][]{
                {5030, 12036, 11036, 12036},
                {12038, 11000, 12000, 11000},
                {11045, 12000, 11000, 12000},
                {12038, 11000, 12000, 11000},
                {5030, 12036, 11036, 12036}
        };
        room[14] = new RoomClass(zone);
        zone = new int[][]{
                {9000, 10000, 9000, 10000, 9000, 10000, 9000},
                {10000, 9000, 10000, 9000, 10000, 9000, 10000},
                {9000, 10000, 9000, 10000, 9000, 10000, 9000},
                {10000, 9000, 10000, 9000, 10000, 9000, 10000},
                {9000, 10000, 9000, 10000, 9000, 10000, 9000},
                {10000, 9000, 10000, 9000, 10000, 9000, 10000},
                {9000, 10000, 9000, 10000, 9000, 10000, 9000}
        };
        room[15] = new RoomClass(zone);
        zone = null;
    }

    public boolean correctPlace(int x, int y) {
        return Global.INSTANCE.getMap()[x][y].isWall() && ((!Global.INSTANCE.getMap()[x][y - 1].isWall() ^ !Global.INSTANCE.getMap()[x][y + 1].isWall()) ^ (!Global.INSTANCE.getMap()[x - 1][y].isWall() ^ !Global.INSTANCE.getMap()[x + 1][y].isWall()));
    }

    public boolean findCell() {
        for (int z2 = 0; z2 < 20; z2++) {
            z = rnd.nextInt(xr - xl + 1) + xl;
            z1 = rnd.nextInt(yr - yl - 1) + yl;
            if (correctPlace(z, z1))
                return true;
        }
        return false;
    }

    public boolean checkZone(int n, int m, int ln, int lm) {
        if (n + ln > Global.INSTANCE.getGame().mw - 3 || m + lm > Global.INSTANCE.getGame().mh - 3 || n < 2 || m < 2)
            return false;
        for (int n1 = n; n1 < n + ln + 1; n1++)
            for (int m1 = m; m1 < m + lm + 1; m1++)
                if (!Global.INSTANCE.getMap()[n1][m1].isWall())
                    return false;
        return true;
    }

    public void fillArea(int sx, int sy, int lx1, int ly1, int f) {
        for (int y = sy; y < sy + ly1; y++)
            for (int x = sx; x < sx + lx1; x++) {
                Global.INSTANCE.getMap()[x][y].mFloorID = f / 1000;
                Global.INSTANCE.getMap()[x][y].mObjectID = f % 1000;
                modifyTile(x, y, f / 1000, f % 1000);
            }
    }

    public void modifyTile(int px, int py, int f, int o) {
        Global.INSTANCE.getMap()[px][py].mIsPassable = Global.INSTANCE.getTiles()[o].getMIsPassable();
        Global.INSTANCE.getMap()[px][py].mIsTransparent = Global.INSTANCE.getTiles()[o].getMIsTransparent();
        Global.INSTANCE.getMap()[px][py].mIsUsable = Global.INSTANCE.getTiles()[o].getMIsUsable();
    }

    public void deleteObjects(int x, int y, int lx, int ly) {
        for (int x1 = 0; x1 < lx; x1++)
            for (int y1 = 0; y1 < ly; y1++)
                if (!Global.INSTANCE.getMap()[x + x1][y + y1].isWall()) {
                    Global.INSTANCE.getMap()[x + x1][y + y1].mObjectID = 0;
                    modifyTile(x + x1, y + y1, Global.INSTANCE.getMap()[x + x1][y + y1].mFloorID, 0);
                }
    }

    public void horizontalMirror(int lx, int ly) {
        int temp;
        for (int y = 0; y < ly / 2; y++)
            for (int x = 0; x < lx; x++) {
                temp = zone[x][y];
                zone[x][y] = zone[x][ly - 1 - y];
                zone[x][ly - 1 - y] = temp;
            }
    }

    public void verticalMirror(int lx, int ly) {
        int temp;
        for (int x = 0; x < lx / 2; x++)
            for (int y = 0; y < ly; y++) {
                temp = zone[x][y];
                zone[x][y] = zone[lx - 1 - x][y];
                zone[lx - 1 - x][y] = temp;
            }
    }

    public void newZone(int lx, int ly, int n) {
        zone = new int[lx][ly];
        zone = room[n].map.clone();
    }

    public void newRotateZone(int lx, int ly, int n) {
        zone = new int[ly][lx];
        int[][] temp;
        temp = room[n].map.clone();
        for (int x = 0; x < lx; x++)
            for (int y = 0; y < ly; y++)
                zone[y][x] = temp[x][y];
    }

    public int getRoom(int x, int y) {
        int xx;
        for (xx = 0; xx < room1.length; xx++)
            if (room1[xx] != null && x >= room1[xx].getX() && y >= room1[xx].getY() && x <= room1[xx].getX() + room1[xx].getLx() - 1 && y <= room1[xx].getY() + room1[xx].getLy() - 1)
                return xx;
        return -1;
    }

    public void generateMap() {
        rc = 0;
        int lx, ly;
        fillArea(0, 0, Global.INSTANCE.getGame().mw, Global.INSTANCE.getGame().mh, 5030);
        for (int i = 0; i < rc; i++)
            room1[i] = null;
        for (int x = 0; x < Global.INSTANCE.getGame().mw; x++)
            for (int y = 0; y < Global.INSTANCE.getGame().mh; y++) {
                Global.INSTANCE.getMap()[x][y].deleteItems();
                Global.INSTANCE.getMap()[x][y].mIsDiscovered = false;
                Global.INSTANCE.getMap()[x][y].mCurrentlyVisible = false;
            }
        while (Global.INSTANCE.getGame().firstMob != null) {
            Global.INSTANCE.getGame().firstMob.map.deleteMob();
            Global.INSTANCE.getGame().firstMob.mob = null;
            Global.INSTANCE.getGame().firstMob = Global.INSTANCE.getGame().firstMob.next;
        }
        int x2 = 0;
        int y2 = 0;
        boolean up, down, left, right;
        lx = rnd.nextInt(Global.INSTANCE.getGame().mw / 2) + 16;
        ly = rnd.nextInt(Global.INSTANCE.getGame().mh / 2) + 16;
        Global.INSTANCE.getMapview().setCamx(lx - 2);
        Global.INSTANCE.getMapview().setCamy(ly - 2);
        Global.INSTANCE.getHero().mx = lx + 2;
        Global.INSTANCE.getHero().my = ly + 2;
        fillArea(lx, ly, 5, 5, 5000);
        fillArea(lx + 2, ly + 2, 1, 1, 5039);
        room1[rc] = new RoomDBClass(x2, y2, lx, ly);
        xl = lx - 1;
        xr = lx + 5;
        yl = ly - 1;
        yr = ly + 5;
        while (rc < mr - 1) {
            if (findCell()) {
                right = Global.INSTANCE.getMap()[z - 1][z1].mIsPassable;
                left = Global.INSTANCE.getMap()[z + 1][z1].mIsPassable;
                down = Global.INSTANCE.getMap()[z][z1 - 1].mIsPassable;
                up = Global.INSTANCE.getMap()[z][z1 + 1].mIsPassable;
                if ((right ^ left) ^ (down ^ up)) {
                    int n = 0;
                    int b = rnd.nextInt(100);
                    if (b < 7) {
                        lx = 4;
                        ly = 3;
                        n = 0;
                    }
                    if (b > 6 && b < 12) {
                        lx = ly = 5;
                        n = 1;
                    }
                    if (b > 11 && b < 16) {
                        lx = 8;
                        ly = 6;
                        n = 2;
                    }
                    if (b > 15 && b < 19) {
                        lx = ly = 5;
                        n = 3;
                    }
                    if (b > 18 && b < 22) {
                        lx = ly = 5;
                        n = 4;
                    }
                    if (b > 21 && b < 26) {
                        lx = ly = 4;
                        n = 5;
                    }
                    if (b > 25 && b < 30) {
                        lx = ly = 9;
                        n = 6;
                    }
                    if (b > 29 && b < 35) {
                        lx = 9;
                        ly = 5;
                        n = 7;
                    }
                    if (b > 34 && b < 41) {
                        lx = ly = 5;
                        n = 7;
                    }
                    if (b > 40 && b < 47) {
                        lx = ly = 4;
                        n = 8;
                    }
                    if (b > 46 && b < 52) {
                        lx = ly = 7;
                        n = 8;
                    }
                    if (b > 51 && b < 61) {
                        lx = 6;
                        ly = 9;
                        n = 9;
                    }
                    if (b > 60 && b < 70) {
                        lx = ly = 5;
                        n = 10;
                    }
                    if (b > 69 && b < 78) {
                        lx = 12;
                        ly = 16;
                        n = 11;
                    }
                    if (b > 77 && b < 86) {
                        lx = ly = n = 12;
                    }
                    if (b > 85 && b < 92) {
                        lx = 7;
                        ly = 5;
                        n = 13;
                    }
                    if (b > 91 && b < 96) {
                        lx = 5;
                        ly = 4;
                        n = 14;
                    }
                    if (b > 95) {
                        lx = rnd.nextInt(8) + 3;
                        ly = rnd.nextInt(8) + 3;
                        n = 100;
                    }
                    if (n != 100) {
                        int tmp;
                        switch (rnd.nextInt(13)) {
                            case 0:
                                newZone(lx, ly, n);
                                horizontalMirror(lx, ly);
                                break;
                            case 2:
                                newZone(lx, ly, n);
                                verticalMirror(lx, ly);
                                break;
                            case 4:
                                newZone(lx, ly, n);
                                verticalMirror(lx, ly);
                                horizontalMirror(lx, ly);
                                break;
                            case 6:
                                newRotateZone(lx, ly, n);
                                tmp = lx;
                                lx = ly;
                                ly = tmp;
                                break;
                            case 8:
                                newRotateZone(lx, ly, n);
                                tmp = lx;
                                lx = ly;
                                ly = tmp;
                                verticalMirror(lx, ly);
                                break;
                            case 10:
                                newRotateZone(lx, ly, n);
                                tmp = lx;
                                lx = ly;
                                ly = tmp;
                                horizontalMirror(lx, ly);
                                break;
                            case 12:
                                newRotateZone(lx, ly, n);
                                tmp = lx;
                                lx = ly;
                                ly = tmp;
                                verticalMirror(lx, ly);
                                horizontalMirror(lx, ly);
                                break;
                            default:
                                newZone(lx, ly, n);
                                break;
                        }
                    }
                    if (up) {
                        y2 = z1 - ly;
                        if (n != 100) {
                            do {
                                x2 = z - rnd.nextInt(lx);
                            } while (zone[z - x2][ly - 1] % 1000 == 30);
                        } else {
                            x2 = z - rnd.nextInt(lx);
                        }
                    }
                    if (down) {
                        y2 = z1 + 1;
                        if (n != 100) {
                            do {
                                x2 = z - rnd.nextInt(lx);
                            } while (zone[z - x2][0] % 1000 == 30);
                        } else {
                            x2 = z - rnd.nextInt(lx);
                        }
                    }
                    if (left) {
                        x2 = z - lx;
                        if (n != 100) {
                            do {
                                y2 = z1 - rnd.nextInt(ly);
                            } while (zone[lx - 1][z1 - y2] % 1000 == 30);
                        } else {
                            y2 = z1 - rnd.nextInt(ly);
                        }
                    }
                    if (right) {
                        x2 = z + 1;
                        if (n != 100) {
                            do {
                                y2 = z1 - rnd.nextInt(ly);
                            } while (zone[0][z1 - y2] % 1000 == 30);
                        } else {
                            y2 = z1 - rnd.nextInt(ly);
                        }
                    }
                    if (checkZone(x2 - 1, y2 - 1, lx + 1, ly + 1)) {
                        rc++;
                        if (n != 100) {
                            for (int x = 0; x < lx; x++)
                                for (int y = 0; y < ly; y++) {
                                    int v = zone[x][y];
                                    Global.INSTANCE.getMap()[x2 + x][y2 + y].mFloorID = v / 1000;
                                    Global.INSTANCE.getMap()[x2 + x][y2 + y].mObjectID = v % 1000;
                                    modifyTile(x2 + x, y2 + y, v / 1000, v % 1000);
                                }
                            if (up) deleteObjects(z, z1 - 1, 1, 1);
                            if (down) deleteObjects(z, z1 + 1, 1, 1);
                            if (right) deleteObjects(z + 1, z1, 1, 1);
                            if (left) deleteObjects(z - 1, z1, 1, 1);
                        } else fillArea(x2, y2, lx, ly, 5000);
                        fillArea(z, z1, 1, 1, 5031);
                        if (x2 < xl) xl = x2 - 1;
                        if (x2 + lx > xr) xr = x2 + lx + 1;
                        if (xl < 2) xl = 2;
                        if (xr > Global.INSTANCE.getGame().mw - 2)
                            xr = Global.INSTANCE.getGame().mw - 2;
                        if (y2 < yl) yl = y2 - 1;
                        if (y2 + ly > yr) yr = y2 + ly + 1;
                        if (yl < 2) yl = 2;
                        if (yr > Global.INSTANCE.getGame().mh - 2)
                            yr = Global.INSTANCE.getGame().mh - 2;
                        room1[rc] = new RoomDBClass(x2, y2, lx, ly);
                        if (rnd.nextInt(2) == 0) {
                            if (up) {
                                int r = getRoom(z, z1 + 1);
                                for (int x = 0; x < lx; x++)
                                    if (getRoom(x2 + x, z1 + 1) == r && !Global.INSTANCE.getMap()[x2 + x][z1 + 1].isWall() && !Global.INSTANCE.getMap()[x2 + x][z1 - 1].isWall())
                                        if (Global.INSTANCE.getMap()[x2 + x][z1 + 1].mFloorID == Global.INSTANCE.getMap()[x2 + x][z1 - 1].mFloorID)
                                            Global.INSTANCE.getGame().fillArea(x2 + x, z1, 1, 1, Global.INSTANCE.getMap()[x2 + x][z1 + 1].mFloorID, 0);
                            }
                            if (down) {
                                int r = getRoom(z, z1 - 1);
                                for (int x = 0; x < lx; x++)
                                    if (getRoom(x2 + x, z1 - 1) == r && !Global.INSTANCE.getMap()[x2 + x][z1 + 1].isWall() && !Global.INSTANCE.getMap()[x2 + x][z1 - 1].isWall())
                                        if (Global.INSTANCE.getMap()[x2 + x][z1 + 1].mFloorID == Global.INSTANCE.getMap()[x2 + x][z1 - 1].mFloorID)
                                            Global.INSTANCE.getGame().fillArea(x2 + x, z1, 1, 1, Global.INSTANCE.getMap()[x2 + x][z1 + 1].mFloorID, 0);
                            }
                            if (right) {
                                int r = getRoom(z - 1, z1);
                                for (int y = 0; y < ly; y++)
                                    if (getRoom(z - 1, y2 + y) == r && !Global.INSTANCE.getMap()[z + 1][y2 + y].isWall() && !Global.INSTANCE.getMap()[z - 1][y2 + y].isWall())
                                        if (Global.INSTANCE.getMap()[z + 1][y2 + y].mFloorID == Global.INSTANCE.getMap()[z - 1][y2 + y].mFloorID)
                                            Global.INSTANCE.getGame().fillArea(z, y2 + y, 1, 1, Global.INSTANCE.getMap()[z + 1][y2 + y].mFloorID, 0);
                            }
                            if (left) {
                                int r = getRoom(z + 1, z1);
                                for (int y = 0; y < ly; y++)
                                    if (getRoom(z + 1, y2 + y) == r && !Global.INSTANCE.getMap()[z + 1][y2 + y].isWall() && !Global.INSTANCE.getMap()[z - 1][y2 + y].isWall())
                                        if (Global.INSTANCE.getMap()[z + 1][y2 + y].mFloorID == Global.INSTANCE.getMap()[z - 1][y2 + y].mFloorID)
                                            Global.INSTANCE.getGame().fillArea(z, y2 + y, 1, 1, Global.INSTANCE.getMap()[z + 1][y2 + y].mFloorID, 0);
                            }
                        }
                    }
                }
            } else
                rc++;
            if (Game.curLvls == Global.INSTANCE.getGame().maxLvl - 1 && rc == mr - 2)
                placeFinalRoom();
        }
        Global.INSTANCE.getMapview().calculateLineOfSight(Global.INSTANCE.getHero().mx, Global.INSTANCE.getHero().my);
        Global.INSTANCE.getGame().updateZone();
        int x4, y4;
        for (int x = 0; x < 30 + Game.curLvls * 7; x++) {
            do {
                x4 = rnd.nextInt(Global.INSTANCE.getGame().mw);
                y4 = rnd.nextInt(Global.INSTANCE.getGame().mh);
            }
            while (!Global.INSTANCE.getMap()[x4][y4].mIsPassable || Global.INSTANCE.getMap()[x4][y4].mCurrentlyVisible || Global.INSTANCE.getMap()[x4][y4].hasMob());
            int en = rnd.nextInt(Global.INSTANCE.getGame().maxMobs - Game.curLvls - 1) + Game.curLvls;
            if (en < 3 && rnd.nextInt(3) == 0) {
                if (Global.INSTANCE.getMap()[x4 - 1][y4].mIsPassable && !Global.INSTANCE.getMap()[x4 - 1][y4].hasItem())
                    Global.INSTANCE.getGame().createMob(x4 - 1, y4, en);
                if (Global.INSTANCE.getMap()[x4 + 1][y4].mIsPassable && !Global.INSTANCE.getMap()[x4 + 1][y4].hasItem())
                    Global.INSTANCE.getGame().createMob(x4 + 1, y4, en);
            }
            Global.INSTANCE.getGame().createMob(x4, y4, en);
        }
        if (Game.curLvls < Global.INSTANCE.getGame().maxLvl - 1) {
            while (true) {
                x4 = rnd.nextInt(Global.INSTANCE.getGame().mw);
                y4 = rnd.nextInt(Global.INSTANCE.getGame().mh);
                if (Global.INSTANCE.getMap()[x4][y4].mObjectID == 0 && !Global.INSTANCE.getMap()[x4][y4].mCurrentlyVisible) {
                    Global.INSTANCE.getMap()[x4][y4].mObjectID = 40;
                    m = x4 - 2;
                    n = y4 - 2;
                    break;
                }
            }
        }
    }

    private void placeFinalRoom() {
        int lx = 7, ly = 7, n = 15;
        int x2 = 0, y2 = 0;
        boolean right, left, up, down;
        newZone(lx, ly, n);
        while (true) {
            if (findCell()) {
                right = Global.INSTANCE.getMap()[z - 1][z1].mIsPassable;
                left = Global.INSTANCE.getMap()[z + 1][z1].mIsPassable;
                down = Global.INSTANCE.getMap()[z][z1 - 1].mIsPassable;
                up = Global.INSTANCE.getMap()[z][z1 + 1].mIsPassable;
                if ((right ^ left) ^ (down ^ up)) {
                    if (up) {
                        y2 = z1 - ly;
                        x2 = z - rnd.nextInt(lx);
                    }
                    if (down) {
                        y2 = z1 + 1;
                        x2 = z - rnd.nextInt(lx);
                    }
                    if (left) {
                        x2 = z - lx;
                        y2 = z1 - rnd.nextInt(ly);
                    }
                    if (right) {
                        x2 = z + 1;
                        y2 = z1 - rnd.nextInt(ly);
                    }
                    if (checkZone(x2 - 1, y2 - 1, lx + 1, ly + 1)) {
                        rc++;
                        for (int x = 0; x < lx; x++)
                            for (int y = 0; y < ly; y++) {
                                int v = zone[x][y];
                                Global.INSTANCE.getMap()[x2 + x][y2 + y].mFloorID = v / 1000;
                                Global.INSTANCE.getMap()[x2 + x][y2 + y].mObjectID = v % 1000;
                                modifyTile(x2 + x, y2 + y, v / 1000, v % 1000);
                            }
                        fillArea(z, z1, 1, 1, 5031);
                        Global.INSTANCE.getGame().createMob(x2 + 3, y2 + 3, 5);
                        Global.INSTANCE.getGame().createMob(x2 + 4, y2 + 3, 4);
                        Global.INSTANCE.getGame().createMob(x2 + 2, y2 + 3, 4);
                        Global.INSTANCE.getGame().createMob(x2 + 3, y2 + 4, 4);
                        Global.INSTANCE.getGame().createMob(x2 + 3, y2 + 2, 4);
                        break;
                    }
                }
            }
        }
    }

}