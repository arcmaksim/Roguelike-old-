package ru.MeatGames.roguelike.tomb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

/**
 * ����� ������������� ������.
 */

public class StatsView extends View{
	/**
	 * @uml.property  name="global"
	 * @uml.associationEnd  readOnly="true"
	 */
	public Global global;
	private Paint hud;
	private Paint text;
	private Paint frame;
	private Paint fbluel;
	private Paint fblued;
	
	public StatsView(Context c){
		super(c);
	    Global.game = (Game)c;
    	setFocusable(true);
    	setFocusableInTouchMode(true);
        fbluel = new Paint();
        fbluel.setColor(getResources().getColor(R.color.fbluel));
        fblued = new Paint();
        fblued.setColor(getResources().getColor(R.color.fblued));
		hud = new Paint();
		hud.setColor(getResources().getColor(R.color.hud));
		frame = new Paint();
		frame.setColor(getResources().getColor(R.color.frame));
		text = new Paint(Paint.ANTI_ALIAS_FLAG);
		text.setColor(getResources().getColor(R.color.white));
    	text.setStyle(Style.FILL);
    	text.setTextSize(16);
    	text.setTextScaleX(1);
    	text.setTextAlign(Paint.Align.CENTER);
    	text.setTypeface(Typeface.createFromAsset(Global.game.getAssets(),"fonts/Bulgaria_Glorious_Cyr.ttf"));
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawRect(0,0,480,800,frame);
		/*float r = (float) Global.hero.getStat(20)/Global.hero.getStat(21);
		canvas.drawRect(150,136,150+Math.round(100*r),144,fbluel);*/
		text.setTextSize(24);
    	text.setTextAlign(Paint.Align.LEFT);
    	canvas.drawText("������� "+Global.hero.getStat(31),70,120,text);
        canvas.drawText("���� "+Global.hero.getStat(0),70,160,text);
        canvas.drawText("�������� "+Global.hero.getStat(1),70,190,text);
        canvas.drawText("��������� "+Global.hero.getStat(2),70,220,text);
        canvas.drawText("������������ "+Global.hero.getStat(3),70,250,text);
        canvas.drawText("���������� "+Global.hero.getStat(4),70,280,text);
    	canvas.drawText("�������� "+Global.hero.getStat(5)+" / "+Global.hero.getStat(6),70,320,text);
    	canvas.drawText("���� "+Global.hero.getStat(7)+" / "+Global.hero.getStat(8),70,350,text);
    	canvas.drawText("����� ��� "+Global.hero.getStat(9)+" / "+Global.hero.getStat(10),70,380,text);
    	canvas.drawText("����� +"+Global.hero.getStat(11),70,420,text);
    	canvas.drawText("���� "+Global.hero.getStat(12)+" - "+Global.hero.getStat(13),70,450,text);
    	canvas.drawText("������ "+Global.hero.getStat(19),70,480,text);
    	canvas.drawText("����� "+Global.hero.getStat(22),70,510,text);
		canvas.drawBitmap(Global.game.c,384,742,null);
		postInvalidate();
	}

	protected void drawLines(Canvas canvas){
		canvas.drawLine(0,720,480,720,frame);
		canvas.drawLine(160,720,160,800,frame);
		canvas.drawLine(320,720,320,800,frame);
		canvas.drawLine(480,720,480,800,frame);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				int x = (int)event.getX();
				int y = (int)event.getY();
				if(y>720 && x>320)
					Global.game.changeScreen(0);
				break;
		}
		return true;
	}
}
