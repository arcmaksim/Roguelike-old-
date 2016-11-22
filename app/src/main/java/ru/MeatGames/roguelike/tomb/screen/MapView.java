package ru.MeatGames.roguelike.tomb.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import ru.MeatGames.roguelike.tomb.Game;
import ru.MeatGames.roguelike.tomb.Global;
import ru.MeatGames.roguelike.tomb.model.Item;
import ru.MeatGames.roguelike.tomb.LogClass;
import ru.MeatGames.roguelike.tomb.R;

public class MapView extends View {
    public final int maxLines = 8;
    public long time2;
    public long animTime;
    public int camx;
    public int camy;
    public boolean pickup = false;
    public boolean exit = false;
    public boolean line = true;
    public boolean prgb = false;
    public boolean death = false;
    public boolean win = false;
    public LogClass[] log;
    public int n;
    public float r;
    public int mx;
    public int my;
    public int mc;
    public int timeLine;
    public Paint dark1;
    public Paint dark2;
    private int x1;
    private int y1;
    private Paint frame;
    private Paint text;
    private Paint black;
    private Paint bground;
    private Paint asd;
    private Paint hud;
    private Paint fbluel;
    private Paint fblued;
    private Paint bg;
    private boolean circle = false;

    public MapView(Context c) {
        super(c);
        setFocusable(true);
        setFocusableInTouchMode(true);
        fbluel = new Paint();
        fbluel.setColor(getResources().getColor(R.color.fbluel));
        fblued = new Paint();
        fblued.setColor(getResources().getColor(R.color.fblued));
        bg = new Paint();
        bg.setColor(getResources().getColor(R.color.wbg1));
        frame = new Paint();
        frame.setColor(getResources().getColor(R.color.frame));
        text = new Paint(Paint.ANTI_ALIAS_FLAG);
        text.setColor(getResources().getColor(R.color.white));
        text.setStyle(Style.FILL);
        text.setTextSize(16);
        text.setTextScaleX(1);
        text.setTextAlign(Paint.Align.LEFT);
        text.setTypeface(Typeface.createFromAsset(Global.game.getAssets(), "fonts/Bulgaria_Glorious_Cyr.ttf"));
        hud = new Paint();
        hud.setColor(getResources().getColor(R.color.hud));
        black = new Paint();
        black.setColor(getResources().getColor(R.color.black));
        bground = new Paint();
        bground.setColor(getResources().getColor(R.color.bground));
        asd = new Paint();
        asd.setColor(getResources().getColor(R.color.asd));
        dark1 = new Paint();
        dark1.setColor(getResources().getColor(R.color.dark1));
        dark2 = new Paint();
        dark2.setColor(getResources().getColor(R.color.dark2));
        log = new LogClass[maxLines];
        for (int x = 0; x < maxLines; x++)
            log[x] = new LogClass();
    }

    public void initPrgb(int mc, int time) {
        timeLine = time;
        time2 = Math.abs(System.currentTimeMillis()) / 10;
        this.mc = mc;
        prgb = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 480, 800, frame);
        if (!death && !win) {
            animTime = Math.abs(System.currentTimeMillis());
            drawMap(canvas);
            if (Global.hero.side)
                canvas.drawBitmap(Game.getHeroImg((int) (animTime / 600 % 2)), Global.hero.x, Global.hero.y, null);
            else
                canvas.drawBitmap(Game.getHeroImg((int) (animTime / 600 % 2 + 2)), Global.hero.x, Global.hero.y, null);
            drawHUD(canvas);
            if (Global.game.lines) drawLines(canvas);
            if (circle) drawActions(canvas, n);
            if (exit) drawExit(canvas);
            r = (float) Global.hero.getStat(20) / Global.hero.getStat(21);
            canvas.drawRect(4, 789, Math.round(472 * r), 796, fblued);
            if (prgb) drawProgBar(canvas);
        } else
            drawFinal(canvas);
        if (line) drawLog(canvas);
        postInvalidate();
    }

    protected void drawMap(Canvas canvas) {
        text.setTextAlign(Paint.Align.LEFT);
        for (int x = camx; x < camx + 9; x++) {
            int cx = x - camx;
            for (int y = camy; y < camy + 9; y++) {
                int cy = y - camy;
                if (x > -1 && y > -1 && x < Global.game.mw && y < Global.game.mh && Global.map[x][y].see) {
                    canvas.drawBitmap(Global.map[x][y].getFloorImg(), Global.game.step * cx + 24, Global.game.step * cy + 184, null);
                    canvas.drawBitmap(Global.map[x][y].getObjectImg(), Global.game.step * cx + 24, Global.game.step * cy + 184, null);
                    if (Global.map[x][y].hasItem())
                        canvas.drawBitmap(Global.map[x][y].getItemImg(), Global.game.step * cx + 24, Global.game.step * cy + 184, null);
                    if (Global.map[x][y].hasMob())
                        canvas.drawBitmap(Global.map[x][y].mob.getImg((int) (animTime / 600 % 2)), Global.game.step * cx + 24, Global.game.step * cy + 184, null);
                    if (((cx - 4 == 0 || cy - 4 == 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 3) || (cx - 4 != 0 && cy - 4 != 0 && Math.abs(cx - 4) + Math.abs(cy - 4) == 4))
                        canvas.drawRect(Global.game.step * cx + 24, Global.game.step * cy + 184, Global.game.step * (cx + 1) + 24, Global.game.step * (cy + 1) + 184, dark1);
                    if (((Math.abs(cx - 4) == 0 || Math.abs(cy - 4) == 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 4) || ((Math.abs(cx - 4) != 0 || Math.abs(cy - 4) != 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 5) || (Math.abs(cx - 4) == Math.abs(cy - 4) && Math.abs(cx - 4) == 3))
                        canvas.drawRect(Global.game.step * cx + 24, Global.game.step * cy + 184, Global.game.step * (cx + 1) + 24, Global.game.step * (cy + 1) + 184, dark2);
                }
            }
        }
        text.setTextAlign(Paint.Align.CENTER);
    }

    protected void drawHUD(Canvas canvas) {
        text.setTextAlign(Paint.Align.CENTER);
        canvas.drawBitmap(Global.game.b, 43, 745, null);
        canvas.drawText("HP  " + Global.hero.getStat(5) + " / " + Global.hero.getStat(6), 240, 755, text);
        canvas.drawText("MP  " + Global.hero.getStat(7) + " / " + Global.hero.getStat(8), 240, 778, text);
        canvas.drawBitmap(Global.game.j, 404, 743, null);
        text.setTextAlign(Paint.Align.LEFT);
    }

    protected void drawActions(Canvas canvas, int n) {
        float z = 360 / n;
        float r = 190;
        int x, y;
        canvas.drawRect(0, 0, 480, 800, bground);
        for (int c = 0; c < n; c++) {
            x = (int) (r * Math.cos(Math.toRadians(270 + z * c)));
            y = (int) (r * Math.sin(Math.toRadians(270 + z * c)));
            canvas.drawRect(240 + x - 36, 400 + y - 36, 240 + x + 36, 400 + y + 36, black);
            canvas.drawRect(240 + x - 30, 400 + y - 30, 240 + x + 30, 400 + y + 30, fbluel);
        }
    }

    protected void drawLines(Canvas canvas) {
        for (int xq = 1; xq < 3; xq++) {
            canvas.drawLine(160 * xq, 48, 160 * xq, 720, frame);
            canvas.drawLine(0, 224 * xq + 48, 480, 224 * xq + 48, frame);
        }
        canvas.drawLine(0, 48, 480, 48, frame);
        canvas.drawLine(240, 0, 240, 48, frame);
        canvas.drawLine(0, 720, 480, 720, frame);
        canvas.drawLine(120, 720, 120, 800, frame);
        canvas.drawLine(360, 720, 360, 800, frame);
    }

    protected void drawExit(Canvas canvas) {
        canvas.drawRect(0, 0, 480, 800, asd);
        canvas.drawRect(40, 320, 440, 472, bground);
        text.setTextAlign(Paint.Align.CENTER);
        text.setTextSize(24);
        canvas.drawText(getContext().getString(R.string.exit_game_message), 240, 370, text);
        text.setTextSize(16);
        canvas.drawText(getContext().getString(R.string.yes), 140, 444, text);
        canvas.drawText(getContext().getString(R.string.No), 340, 444, text);
    }

    protected void drawLog(Canvas canvas) {
        text.setTextSize(16);
        text.setTextAlign(Paint.Align.LEFT);
        for (int c = 0; c < maxLines; c++)
            canvas.drawText(log[c].getT(), 5, 20 + 21 * c, text);
    }

    protected void drawProgBar(Canvas canvas) {
        if (Math.abs(System.currentTimeMillis()) / 10 - time2 > timeLine) {
            prgb = false;
            line = true;
            afterPrgb(mc);
        }
        canvas.drawRect(168, Global.hero.y - 35, 312, Global.hero.y - 11, text);
        canvas.drawRect(168, Global.hero.y - 35, 168 + (Math.abs(System.currentTimeMillis()) / 10 - time2) * (144f / timeLine), Global.hero.y - 11, fbluel);
        text.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getContext().getString(R.string.searching_label), 240, Global.hero.y - 16, text);
    }

    protected void drawFinal(Canvas canvas) {
        text.setTextAlign(Paint.Align.CENTER);
        text.setTextSize(24);
        if (death) {
            canvas.drawText(getContext().getString(R.string.death_from_label), 240, 320, text);
            canvas.drawBitmap(Global.game.lastAttack, 204, 340, null);
        }
        if (win) {
            canvas.drawText(getContext().getString(R.string.victory_label), 240, 320, text);
            canvas.drawText(getContext().getString(R.string.king_was_slain_label), 240, 360, text);
        }
        text.setTextSize(16);
        canvas.drawText(getContext().getString(R.string.to_menu_label), 240, 765, text);
    }

    public void afterPrgb(int mc) {
        switch (mc) {
            case 33:
                Global.game.fillArea(Global.hero.mx + mx, Global.hero.my + my, 1, 1, Game.getFloor(Global.hero.mx + mx, Global.hero.my + my), 35);
                Global.mapview.addLine(getContext().getString(R.string.search_chest_message));
                Global.game.createItem(Global.hero.mx + mx, Global.hero.my + my);
                break;
            case 36:
                Global.game.fillArea(Global.hero.mx + mx, Global.hero.my + my, 1, 1, Game.getFloor(Global.hero.mx + mx, Global.hero.my + my), 37);
                addLine(getContext().getString(R.string.search_bookshelf_message));
                if (Global.game.rnd.nextInt(3) != 0) {
                    addLine(getContext().getString(R.string.experience_earned_message));
                    Global.hero.modifyStat(20, Global.game.rnd.nextInt(4) + 2, 1);
                } else {
                    addLine(getContext().getString(R.string.nothing_interesting_message));
                }
                break;
        }
    }

    public void clearLog() {
        for (int c = 0; c < maxLines; c++)
            log[c].setT("");
    }

    public void addLine(String s) {
        for (int c = 0; c < maxLines; c++)
            if (log[c].getT().equals("")) {
                log[c].setT(s);
                break;
            }
    }

    public void addLine1(String s) {
        boolean t = true;
        for (int c = 0; c < maxLines; c++)
            if (log[c].getT().equals(s)) {
                t = false;
                break;
            }
        if (t) addLine(s);
    }

    private int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }

    public void line(int xstart, int ystart, int xend, int yend) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;
        boolean v = true;
        dx = xend - xstart;
        dy = yend - ystart;
        incx = sign(dx);
        incy = sign(dy);
        if (dx < 0)
            dx = -dx;
        if (dy < 0)
            dy = -dy;
        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;
        }
        x = xstart;
        y = ystart;
        err = el / 2;
        Global.map[x][y].see = true;
        for (int t = 0; t < el; t++) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;
                y += incy;
            } else {
                x += pdx;
                y += pdy;
            }
            if (x > -1 && y > -1 && x < Global.game.mw && y < Global.game.mh) {
                if (!Global.map[x][y].see)
                    Global.map[x][y].see = v;
                if (v)
                    Global.map[x][y].dis = true;
                if (!Global.map[x][y].vis)
                    v = false;
            }
        }
    }

    public void los(int x, int y) {
        int cm = (camx < 0) ? 0 : camx;
        int cm1 = (camy < 0) ? 0 : camy;
        for (int c = cm; c < ((cm + 9 >= Global.game.mw) ? Global.game.mw : cm + 9); c++)
            for (int c1 = cm1; c1 < ((cm1 + 9 >= Global.game.mw) ? Global.game.mw : cm1 + 9); c1++)
                Global.map[c][c1].see = false;
        for (int c = x - 1; c < x + 2; c++)
            for (int c1 = y - 1; c1 < y + 2; c1++)
                Global.map[c][c1].see = true;
        for (int c = -1; c < 2; c++) {
            line(x, y, x + c, y - 4);
            line(x, y, x + c, y + 4);
        }
        for (int c = -3; c < 4; c++) {
            line(x, y, x + c, y - 3);
            line(x, y, x + c, y + 3);
            line(x, y, x + c, y - 2);
            line(x, y, x + c, y + 2);
        }
        for (int c = -4; c < -1; c++) {
            line(x, y, x + c, y - 1);
            line(x, y, x + c, y + 1);
            line(x, y, x + Math.abs(c), y - 1);
            line(x, y, x + Math.abs(c), y + 1);
        }
        line(x, y, x - 4, y);
        line(x, y, x + 4, y);
    }

    public void onTouchMain() {
        clearLog();
        if (y1 < 48 && x1 > 240)
            Global.game.changeScreen(3);
        if (y1 < 48 && x1 < 240)
            Global.game.lines = !Global.game.lines;
        if (y1 > 48 && y1 < 720)
            switch (((y1 - 48) / 224) * 3 + x1 / 160) {
                case 0:
                    Global.game.move(-1, -1);
                    break;
                case 1:
                    Global.game.move(0, -1);
                    break;
                case 2:
                    Global.game.move(1, -1);
                    break;
                case 3:
                    Global.game.move(-1, 0);
                    break;
                case 4:
                    if (Global.map[Global.hero.mx][Global.hero.my].o == 40) {
                        Game.curLvls++;
                        Global.mapg.mapGen();
                        Global.game.move(0, 0);
                    }
                    if (Global.map[Global.hero.mx][Global.hero.my].hasItem()) {
                        Item item = Global.map[Global.hero.mx][Global.hero.my].getItem();
                        Global.hero.addItem(item);
                        addLine(item.n + " подобран" + item.n1);
                        Game.v.vibrate(30);
                        Global.game.move(0, 0);
                    }
                    break;
                case 5:
                    Global.game.move(1, 0);
                    break;
                case 6:
                    Global.game.move(-1, 1);
                    break;
                case 7:
                    Global.game.move(0, 1);
                    break;
                case 8:
                    Global.game.move(1, 1);
                    break;
            }
        if (y1 > 720) {
            if (x1 < 120)
                Global.game.changeScreen(1);
            if (x1 > 120 && x1 < 360)
                Global.game.changeScreen(2);
            if (x1 > 360) {
                Global.game.move(0, 0);
                Global.mapview.addLine(getContext().getString(R.string.turn_passed_message));
            }
        }
    }

    public void onTouchExit() {
        if (y1 > 385 && y1 < 472 && x1 > 40 && x1 < 440)
            if (x1 < 240)
                Global.game.exitGame();
            else
                exit = false;
    }

    public void onTouchFinal() {
        if (y1 > 720) {
            death = win = false;
            Global.game.newGame();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (Global.game.tap) {
                    x1 = (int) event.getX();
                    y1 = (int) event.getY();
                    if (!prgb && !death && !win) {
                        if (!pickup && !exit) {
                            onTouchMain();
                        } else {
                            if (pickup)
                                if (x1 > 45 && x1 < 435 && y1 > 448 && y1 < 520) {
                                    if (x1 < 240) Global.game.pickupItem();
                                    pickup = false;
                                }
                            if (exit) onTouchExit();
                        }
                    }
                    if (death || win) onTouchFinal();
                }
        }
        return true;
    }

}