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
import ru.MeatGames.roguelike.tomb.R;

public class BrezenhamView extends View {
    private Paint white;
    private Paint blue;
    private Paint red;
    private Paint grn;
    private Paint frame;
    private Paint hud;
    private Paint text;
    private int x1;
    private int y1;

    public BrezenhamView(Context c) {
        super(c);
        Global.INSTANCE.setGame((Game) c);
        setFocusable(true);
        setFocusableInTouchMode(true);
        frame = new Paint();
        frame.setColor(getResources().getColor(R.color.mainBackground));
        hud = new Paint();
        hud.setColor(getResources().getColor(R.color.hud));
        white = new Paint();
        white.setColor(getResources().getColor(R.color.white));
        blue = new Paint();
        blue.setColor(getResources().getColor(R.color.lightBlue));
        red = new Paint();
        red.setColor(getResources().getColor(R.color.fredl));
        grn = new Paint();
        grn.setColor(getResources().getColor(R.color.grs));
        text = new Paint(Paint.ANTI_ALIAS_FLAG);
        text.setColor(getResources().getColor(R.color.cell));
        text.setStyle(Style.FILL_AND_STROKE);
        text.setTextScaleX(1);
        text.setTextSize(16);
        text.setTypeface(Typeface.createFromAsset(Global.INSTANCE.getGame().getAssets(), "fonts/Bulgaria_Glorious_Cyr.ttf"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 480, 800, frame);
        for (int x = 0; x < Global.INSTANCE.getGame().mw; x++)
            for (int y = 0; y < Global.INSTANCE.getGame().mh; y++) {
                if (Global.INSTANCE.getMap()[x][y].dis) {
                    switch (Global.INSTANCE.getMap()[x][y].o) {
                        case 0:
                            canvas.drawRect(x * 5, 5 + 5 * y, x * 5 + 5, 10 + 5 * y, white);
                            break;
                        case 31:
                        case 32:
                            canvas.drawRect(x * 5, 5 + 5 * y, x * 5 + 5, 10 + 5 * y, hud);
                            break;
                        case 40:
                            canvas.drawRect(x * 5, 5 + 5 * y, x * 5 + 5, 10 + 5 * y, grn);
                            break;
                    }
                    /*if(Global.map[x][y].hasMob())
                        canvas.drawRect(x*5,5+5*y,x*5+5,10+5*y,blue);*/
                }
            }
        canvas.drawRect(Global.INSTANCE.getHero().mx * 5, 5 + Global.INSTANCE.getHero().my * 5, 5 + Global.INSTANCE.getHero().mx * 5, 10 + Global.INSTANCE.getHero().my * 5, red);
        text.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(getContext().getString(R.string.back_label), 435, 765, text);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                x1 = (int) event.getX();
                y1 = (int) event.getY();
                if (y1 > 720 && x1 > 320)
                    Global.INSTANCE.getGame().changeScreen(0);
                break;
        }
        return true;
    }
}