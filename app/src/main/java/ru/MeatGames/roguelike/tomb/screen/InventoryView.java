package ru.MeatGames.roguelike.tomb.screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Region;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import ru.MeatGames.roguelike.tomb.util.AssetHelper;
import ru.MeatGames.roguelike.tomb.Game;
import ru.MeatGames.roguelike.tomb.Global;
import ru.MeatGames.roguelike.tomb.InvItem;
import ru.MeatGames.roguelike.tomb.model.Item;
import ru.MeatGames.roguelike.tomb.R;

public class InventoryView extends View {

    public Global global;
    private Paint frame;
    private Paint text;
    private Paint text1;

    private InvItem curItem;
    private int id = 0;
    private Bitmap img;
    private int sx; //ACTION_DOWN
    private int sy;
    private int lx; //ACTION_UP,ACTION_MOVE
    private int ly;
    private int deadZone = 19;
    private int itemSize = 50;
    private int maxItemsOnScreen = 13;
    private int curItemsOnScreen;
    private int curScroll;
    private int maxScroll;
    private int savedScroll = 0;
    private boolean scroll = false;
    private boolean scrollPermission = true;
    private boolean tap = false;
    private boolean item = false;
    private boolean gear = false;
    private boolean sorted = false;
    private boolean fweapon = true;
    private boolean fshield = true;
    private boolean farmor = true;
    private boolean fgear = true;
    private boolean fconsumable = true;
    private Bitmap weapOn;
    private Bitmap weapOff;
    private Bitmap shieldOn;
    private Bitmap shieldOff;
    private Bitmap armorOn;
    private Bitmap armorOff;
    private Bitmap gearOn;
    private Bitmap gearOff;
    private Bitmap consOn;
    private Bitmap consOff;
    private InvItem list = null;
    private Region scrR = new Region(0, 0, 480, 800);
    private Region itemsR = new Region(40, 72, 435, 720);
    private Region parm = new Region(40, 92, 435, 140);
    private Region sarm = new Region(40, 142, 435, 190);
    private Region body = new Region(40, 242, 435, 290);
    private Paint framegrn;

    public InventoryView(Context c) {
        super(c);
        Global.game = (Game) c;
        setFocusable(true);
        setFocusableInTouchMode(true);

        frame = new Paint();
        frame.setColor(getResources().getColor(R.color.frame));
        framegrn = new Paint();
        framegrn.setColor(getResources().getColor(R.color.framegrn));
        text = new Paint(Paint.ANTI_ALIAS_FLAG);
        text.setColor(getResources().getColor(R.color.white));
        text.setStyle(Style.FILL);
        text.setTextSize(24);
        text.setTextScaleX(1);
        text.setTextAlign(Paint.Align.CENTER);
        text.setTypeface(Typeface.createFromAsset(Global.game.getAssets(), "fonts/Bulgaria_Glorious_Cyr.ttf"));
        text1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        text1.setColor(getResources().getColor(R.color.white));
        text1.setStyle(Style.FILL);
        text1.setTextSize(16);
        text1.setTextScaleX(1);
        text1.setTextAlign(Paint.Align.LEFT);
        text1.setTypeface(text.getTypeface());

        AssetHelper assetHelper = Global.mAssetHelper;
        weapOn = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("weapons_icon_outline"), 30, 30, false);
        weapOff = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("weapons_icon_filling"), 30, 30, false);
        shieldOn = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("shield_icon_outline"), 32, 36, false);
        shieldOff = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("shield_icon_filling"), 32, 36, false);
        armorOn = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("armor_icon_outline"), 38, 34, false);
        armorOff = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("armor_icon_filling"), 38, 34, false);
        gearOn = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("gear_icon_outline"), 32, 28, false);
        gearOff = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("gear_icon_filling"), 32, 28, false);
        consOn = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("consumables_icon_outline"), 24, 34, false);
        consOff = Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("consumables_icon_filling"), 24, 34, false);
    }

    public void fillList(boolean weapon, boolean shield, boolean armor, boolean gear, boolean consumable) {
        list = null;
        InvItem temp = null;
        curItemsOnScreen = curScroll = 0;
        if (Global.hero.inv != null)
            for (InvItem cur = Global.hero.inv; cur != null; cur = cur.next)
                if (isAllowed(cur.item, weapon, shield, armor, gear, consumable)) {
                    if (list == null) {
                        temp = list = cur;
                    } else {
                        temp.nextList = cur;
                        temp = temp.nextList;
                    }
                    curItemsOnScreen++;
                }
        if (temp != null)
            temp.nextList = null;
        maxScroll = (curItemsOnScreen > maxItemsOnScreen) ? -(curItemsOnScreen - maxItemsOnScreen) * itemSize : 0;
    }

    private boolean isAllowed(Item item, boolean weapon, boolean shield, boolean armor, boolean gear, boolean consumable) {
        return (weapon && item.isWeapon()) || (shield && item.isShield()) || (armor && item.isArmor()) || (gear && item.isGear()) || (consumable && item.isConsumable());
    }

    public void fillList() {
        list = null;
        InvItem temp = null;
        curItemsOnScreen = curScroll = 0;
        if (Global.hero.inv != null)
            for (InvItem cur = Global.hero.inv; cur != null; cur = cur.next)
                if (isAllowed(cur.item)) {
                    if (list == null) {
                        temp = list = cur;
                    } else {
                        temp.nextList = cur;
                        temp = temp.nextList;
                    }
                    curItemsOnScreen++;
                }
        if (temp != null)
            temp.nextList = null;
        maxScroll = (curItemsOnScreen > maxItemsOnScreen) ? -(curItemsOnScreen - maxItemsOnScreen) * itemSize : 0;
    }

    private boolean isAllowed(Item item) {
        return (fweapon && item.isWeapon()) || (fshield && item.isShield()) || (farmor && item.isArmor()) || (fgear && item.isGear()) || (fconsumable && item.isConsumable());
    }

    protected void drawFlags(Canvas canvas) {
        if (fweapon)
            canvas.drawBitmap(weapOn, 33, 17, null);
        else
            canvas.drawBitmap(weapOff, 33, 17, null);

        if (fshield)
            canvas.drawBitmap(shieldOn, 128, 16, null);
        else
            canvas.drawBitmap(shieldOff, 128, 16, null);

        if (farmor)
            canvas.drawBitmap(armorOn, 221, 16, null);
        else
            canvas.drawBitmap(armorOff, 221, 16, null);

        if (fgear)
            canvas.drawBitmap(gearOn, 320, 17, null);
        else
            canvas.drawBitmap(gearOff, 320, 17, null);

        if (fconsumable)
            canvas.drawBitmap(consOn, 420, 16, null);
        else
            canvas.drawBitmap(consOff, 420, 16, null);
    }

    protected void drawList(Canvas canvas) {
        InvItem cur = list;
        if (cur != null) {
            canvas.clipRegion(itemsR, Region.Op.REPLACE);
            int q1 = (curScroll + savedScroll) % itemSize;
            int u = -(curScroll + savedScroll) / itemSize;
            for (int q = 0; cur != null; cur = cur.nextList) {
                if (q >= u) {
                    if (!cur.item.isConsumable() && Global.hero.isEquiped(cur)) {
                        canvas.drawRect(40, 72 + q1, 96, 120 + q1, framegrn);
                        canvas.drawRect(95, 72 + q1, 435, 120 + q1, framegrn);
                    } else {
                        canvas.drawRect(40, 72 + q1, 96, 120 + q1, frame);
                        canvas.drawRect(95, 72 + q1, 435, 120 + q1, frame);
                    }
                    canvas.drawBitmap(cur.item.getImage(), 45, 72 + q1, null);
                    canvas.drawText(cur.item.n, 115, 102 + q1, text1);
                    q1 += itemSize;
                    if (q == u + 13) break;
                }
                q++;
            }
        } else {
            canvas.drawText(getContext().getString(R.string.inventory_is_empty_label), 240, 100, text);
        }
        canvas.clipRegion(scrR, Region.Op.REPLACE);
        if (!sorted) {
            text1.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(getContext().getString(R.string.gear_label), 35, 765, text1);
        }
    }

    protected void drawGear(Canvas canvas) {
        for (int x = 0; x < 2; x++) {
            if (Global.hero.equipmentList[x] != null) {
                canvas.drawRect(40, 92 + 50 * x, 98, 140 + 50 * x, frame);
                canvas.drawRect(97, 92 + 50 * x, 435, 140 + 50 * x, frame);
                canvas.drawBitmap(Global.hero.equipmentList[x].item.getImage(), 45, 92 + x * 50, null);
                canvas.drawText(Global.hero.equipmentList[x].item.n, 115, 122 + x * 50, text1);
            } else {
                text1.setTextAlign(Paint.Align.CENTER);
                canvas.drawRect(40, 92 + 50 * x, 435, 140 + 50 * x, frame);
                canvas.drawText("�����", 240, 123 + 50 * x, text1);
                text1.setTextAlign(Paint.Align.LEFT);
            }
        }
        if (Global.hero.equipmentList[2] != null) {
            canvas.drawRect(40, 242, 98, 290, frame);
            canvas.drawRect(97, 242, 435, 290, frame);
            canvas.drawBitmap(Global.hero.equipmentList[2].item.getImage(), 45, 242, null);
            canvas.drawText(Global.hero.equipmentList[2].item.n, 115, 272, text1);
        } else {
            text1.setTextAlign(Paint.Align.CENTER);
            canvas.drawRect(40, 242, 435, 290, frame);
            canvas.drawText("�����", 240, 273, text1);
            text1.setTextAlign(Paint.Align.LEFT);
        }
        canvas.drawText("������", 50, 72, text1);
        canvas.drawText("�������", 50, 222, text1);
        text1.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("���������", 35, 765, text1);
    }

    protected void drawItem(Canvas canvas) {
        canvas.drawBitmap(img, 156, 60, null);
        text1.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("---", 78, 144, text1);
        canvas.drawText("---", 402, 144, text1);
        canvas.drawText(curItem.item.n, 240, 248, text);
        switch (curItem.item.type) {
            case 1:
                if (curItem.item.property)
                    canvas.drawText("��������� ������", 240, 290, text1);
                else
                    canvas.drawText("���������� ������", 240, 290, text1);
                canvas.drawText("����� +" + curItem.item.val1, 240, 315, text1);
                canvas.drawText("���� " + curItem.item.val2 + " - " + curItem.item.val3, 240, 340, text1);
                break;
            case 2:
                canvas.drawText("������ " + curItem.item.val1, 240, 290, text1);
                canvas.drawText("����� " + curItem.item.val2, 240, 315, text1);
                break;
            case 3:
                canvas.drawText("������ " + curItem.item.val1, 240, 290, text1);
                canvas.drawText("����� " + curItem.item.val2, 240, 315, text1);
                break;
            case 5:
                canvas.drawText(Global.stats[curItem.item.val1].getN() + " +" + curItem.item.val2, 240, 290, text1);
                break;
        }
        text1.setTextAlign(Paint.Align.LEFT);
        if (gear) {
            if (sorted) {
                canvas.drawText("�����", 35, 765, text1);
            } else {
                canvas.drawText("�����", 35, 765, text1);
            }
        } else {
            if (curItem.item.isConsumable()) {
                canvas.drawText("������������", 35, 765, text1);
            } else {
                if (Global.hero.equipmentList[curItem.item.type - 1] == null) {
                    canvas.drawText("�����", 35, 765, text1);
                } else {
                    if (curItem == Global.hero.equipmentList[curItem.item.type - 1]) {
                        canvas.drawText("�����", 35, 765, text1);
                    } else {
                        canvas.drawText("�������", 35, 765, text1);
                    }
                }
            }
            text1.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("���������", 240, 765, text1);
            text1.setTextAlign(Paint.Align.LEFT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 480, 800, frame);
        if (item) {
            drawItem(canvas);
        } else if (gear && !sorted) {
            drawGear(canvas);
        } else {
            if (!sorted) drawFlags(canvas);
            drawList(canvas);
        }
        text1.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(getContext().getString(R.string.back_label), 445, 765, text1);
        text1.setTextAlign(Paint.Align.LEFT);
        postInvalidate();
    }

    private InvItem findItem(int id) {
        int c = 0;
        for (InvItem cur = list; cur != null; cur = cur.nextList)
            if (c++ == id)
                return cur;
        return null;
    }

    private void onTouchGear(int sx, int sy) {
        if (parm.contains(sx, sy)) {
            if (Global.hero.equipmentList[0] == null) {
                fillList(true, false, false, false, false);
                sorted = scrollPermission = true;
            } else {
                curItem = Global.hero.equipmentList[0];
                img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                item = true;
                scrollPermission = false;
            }
        }
        if (sarm.contains(sx, sy)) {
            if (Global.hero.equipmentList[1] == null) {
                fillList(false, true, false, false, false);
                sorted = scrollPermission = true;
            } else {
                curItem = Global.hero.equipmentList[1];
                img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                item = true;
                scrollPermission = false;
            }
        }
        if (body.contains(sx, sy)) {
            if (Global.hero.equipmentList[2] == null) {
                fillList(false, false, true, false, false);
                sorted = scrollPermission = true;
            } else {
                curItem = Global.hero.equipmentList[2];
                img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                item = true;
                scrollPermission = false;
            }
        }
        if (sy > 720) {
            if (sx > 320)
                Global.game.changeScreen(0);
            if (sx < 160) {
                gear = false;
                curScroll = 0;
                scrollPermission = true;
            }
        }
    }

    public void onTouchItem(int sx, int sy) {
        if (gear) {
            if (sorted) {
                if (sy > 720) {
                    if (sx > 320) {
                        item = false;
                        scrollPermission = true;
                    }
                    if (sx < 160) {
                        Global.hero.equipItem(curItem);
                        scrollPermission = item = sorted = false;
                        Global.game.changeScreen(0);
                    }
                }
            } else {
                if (sy > 720) {
                    if (sx > 320)
                        item = false;
                    if (sx < 160) {
                        Global.hero.takeOffItem(curItem);
                        scrollPermission = item = sorted = false;
                        Global.game.changeScreen(0);
                    }
                }
            }
        } else {
            if (sy > 720) {
                switch (sx / 160) {
                    case 0:
                        if (curItem.item.isConsumable()) {
                            Global.hero.modifyStat(curItem.item.val1, curItem.item.val2, 1);
                            Global.mapview.addLine(curItem.item.n + " использован" + curItem.item.n1);
                            Global.hero.deleteItem(curItem);
                        } else {
                            if (Global.hero.equipmentList[curItem.item.type - 1] == null) {
                                Global.hero.equipItem(curItem);
                            } else {
                                if (curItem == Global.hero.equipmentList[curItem.item.type - 1]) {
                                    Global.hero.takeOffItem(curItem);
                                } else {
                                    Global.hero.takeOffItem(curItem.item.type - 1);
                                    Global.hero.equipItem(curItem);
                                }

                            }
                        }
                        item = false;
                        Global.game.changeScreen(0);
                        break;
                    case 1:
                        Global.hero.dropItem(curItem);
                        Game.v.vibrate(30);
                        fillList();
                        item = false;
                        Global.game.changeScreen(0);
                        break;
                    case 2:
                        item = false;
                        break;
                }
                scrollPermission = true;
            }
        }
    }

    public void onTouchInv(int sx, int sy) {
        if (sorted) {
            if (sy > 720 && sx > 320) {
                scrollPermission = false;
                fillList();
                sorted = false;
            }
            if (itemsR.contains(sx, sy)) {
                if (maxScroll == 0) {
                    id = (sy - 72) / itemSize;
                    if (id < curItemsOnScreen) {
                        curItem = findItem(id);
                        img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                        scrollPermission = false;
                        item = true;
                    }
                } else {
                    id = (sy - 72 + (-savedScroll) % itemSize) / itemSize + (-savedScroll) / itemSize;
                    curItem = findItem(id);
                    img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                    scrollPermission = false;
                    item = true;
                }
            }
        } else {
            if (sy < 60) { //������� ������
                switch (sx / 96) {
                    case 0:
                        fweapon = !fweapon;
                        break;
                    case 1:
                        fshield = !fshield;
                        break;
                    case 2:
                        farmor = !farmor;
                        break;
                    case 3:
                        fgear = !fgear;
                        break;
                    case 4:
                        fconsumable = !fconsumable;
                        break;
                }
                fillList();
                savedScroll = curScroll = 0;
            }
            if (itemsR.contains(sx, sy)) {
                if (maxScroll == 0) {
                    id = (sy - 72) / itemSize;
                    if (id < curItemsOnScreen) {
                        curItem = findItem(id);
                        img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                        scrollPermission = false;
                        item = true;
                    }
                } else {
                    id = (sy - 72 + (-savedScroll) % itemSize) / itemSize + (-savedScroll) / itemSize;
                    curItem = findItem(id);
                    img = Bitmap.createScaledBitmap(curItem.item.getImage(), 168, 168, false);
                    scrollPermission = false;
                    item = true;
                }
            }
            if (sy > 720) {
                if (sx > 320)
                    Global.game.changeScreen(0);
                if (sx < 160) {
                    gear = true;
                    curScroll = 0;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lx = sx = (int) event.getX();
                ly = sy = (int) event.getY();
                tap = true;
                break;
            case MotionEvent.ACTION_MOVE:
                lx = (int) event.getX();
                ly = (int) event.getY();
                if (Math.abs(lx - sx) > deadZone || Math.abs(ly - sy) > deadZone) {
                    if (tap)
                        tap = false;
                    if (scrollPermission && !scroll) {
                        scroll = true;
                        curScroll = 0;
                        sx = lx;
                        sy = ly;
                    }
                }
                if (scroll) {
                    if (maxScroll != 0) {
                        if ((ly - sy) + savedScroll < maxScroll) {
                            curScroll = maxScroll - savedScroll;
                        } else if ((ly - sy) + savedScroll > 0) {
                            curScroll = -savedScroll;
                        } else {
                            curScroll = (ly - sy);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!scroll) {
                    if (tap)
                        if (item) {
                            onTouchItem(sx, sy);
                        } else if (gear && !sorted) {
                            onTouchGear(sx, sy);
                        } else
                            onTouchInv(sx, sy);
                } else {
                    savedScroll += curScroll;
                    curScroll = 0;
                }
                scroll = tap = false;
                break;
        }
        return true;
    }

}