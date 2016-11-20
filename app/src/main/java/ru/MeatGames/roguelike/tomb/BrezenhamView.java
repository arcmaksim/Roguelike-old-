package ru.MeatGames.roguelike.tomb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

/**
 * Ёкран вывода исследованой области карты.
 */

public class BrezenhamView extends View{
	private Paint white;
	private Paint blue;
	private Paint red;
	private Paint grn;
	private Paint frame;
    private Paint hud;
    private Paint text;
    /**
	 * @uml.property  name="x1"
	 */
    private int x1;
	/**
	 * @uml.property  name="y1"
	 */
	private int y1;
	
	public BrezenhamView(Context c){
	    super(c);
	    Global.game = (Game)c;
    	setFocusable(true);
    	setFocusableInTouchMode(true);
    	frame = new Paint();
		frame.setColor(getResources().getColor(R.color.frame));
		hud = new Paint();
		hud.setColor(getResources().getColor(R.color.hud));
		white = new Paint();
		white.setColor(getResources().getColor(R.color.white));
		blue = new Paint();
		blue.setColor(getResources().getColor(R.color.fbluel));
		red = new Paint();
		red.setColor(getResources().getColor(R.color.fredl));
		grn = new Paint();
		grn.setColor(getResources().getColor(R.color.grs));
		text = new Paint(Paint.ANTI_ALIAS_FLAG);
		text.setColor(getResources().getColor(R.color.cell));
    	text.setStyle(Style.FILL_AND_STROKE);
    	text.setTextScaleX(1);
    	text.setTextSize(16);
    	text.setTypeface(Typeface.createFromAsset(Global.game.getAssets(),"fonts/Bulgaria_Glorious_Cyr.ttf"));
	}

	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawRect(0, 0, 480, 800, frame);
        for(int x=0;x<Global.game.mw;x++)
            for(int y=0;y<Global.game.mh;y++){
                if(Global.map[x][y].dis){
                    switch(Global.map[x][y].o){
                        case 0:
                            canvas.drawRect(x*5,5+5*y,x*5+5,10+5*y,white);
                            break;
                        case 31:
                        case 32:
                            canvas.drawRect(x*5,5+5*y,x*5+5,10+5*y,hud);
                            break;
                        case 40:
                            canvas.drawRect(x*5,5+5*y,x*5+5,10+5*y,grn);
                            break;
                    }
                    /*if(Global.map[x][y].hasMob())
                        canvas.drawRect(x*5,5+5*y,x*5+5,10+5*y,blue);*/
                }
            }
        canvas.drawRect(Global.hero.mx*5,5+Global.hero.my*5,5+Global.hero.mx*5,10+Global.hero.my*5,red);
        text.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Ќазад",435,765,text);
		postInvalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
            case MotionEvent.ACTION_UP:
                x1 = (int)event.getX();
                y1 = (int)event.getY();
                if(y1>720 && x1>320)
                    Global.game.changeScreen(0);
                break;
		}
		return true;
	}
}