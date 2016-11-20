package ru.MeatGames.roguelike.tomb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

/**
 * ����� ��������� ����. ����������� ������ ����� ����, ���� ������ �� ����.
 */

public class MainMenu extends View{

    public Paint frame;
	public Paint text;

    public MainMenu(Context c){
        super(c);
        Global.game = (Game)c;
        setFocusable(true);
        setFocusableInTouchMode(true);
        frame = new Paint();
        frame.setColor(getResources().getColor(R.color.frame));
        text = new Paint(Paint.ANTI_ALIAS_FLAG);
        text.setColor(getResources().getColor(R.color.cell));
        text.setStyle(Style.FILL_AND_STROKE);
        text.setTextScaleX(1);
        text.setTypeface(Typeface.createFromAsset(Global.game.getAssets(),"fonts/Bulgaria_Glorious_Cyr.ttf"));
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRect(0,0,480,800,frame);
        text.setTextAlign(Paint.Align.CENTER);
        text.setTextSize(32);
        canvas.drawText("Yet Another",240,300,text);
        canvas.drawText("Roguelike",240,350,text);
        text.setTextSize(16);
        text.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("����� ����",35,765,text);
        text.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("�����",445,765,text);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x1,y1;
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:
                x1 = (int)event.getX();
                y1 = (int)event.getY();
                if(y1>720){
                    if(x1>320)
                        Global.game.exitGame();
                    if(x1<160)
                        Global.game.changeScreen(0);
                }
                break;
        }
        return true;
    }
}
