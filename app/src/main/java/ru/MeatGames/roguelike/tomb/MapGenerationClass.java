package ru.MeatGames.roguelike.tomb;

import java.util.Random;

public class MapGenerationClass {

	public RoomClass[] room;
	public RoomDBClass[] room1;
	public int[][] zone;
	public Random rnd;
    public int m;
	public int n;
	public int z=0;
	public int z1=0;
	public int rc;
	public int mr=70;
	public int xl;
	public int xr;
	public int yl;
	public int yr;

	public MapGenerationClass() {
		rnd = new Random();
		room1 = new RoomDBClass[mr];
		loadingRooms();
	}
	
	public void loadingRooms(){
		room = new RoomClass[16];
		zone = new int[][]{
			{5030,5000,5041},
			{5041,5000,5038},
			{5038,5000,5041},
			{5041,5000,5030}
		};
		room[0] = new RoomClass(zone);
		zone = new int[][]{
			{5030 ,11036,12036,11036,5030},
			{11036,12000,11000,12000,11036},
			{12036,11000,12000,11000,12036},
			{11036,12000,11000,12000,11036},
			{5030 ,11036,12036,11036,5030}
		};
		room[1] = new RoomClass(zone);
		zone = new int[][]{
			{5030,5030,5000,5000,5041,5030},
			{5000,5000,5000,5041,5038,5041},
			{5000,5041,5000,5000,5041,5000},
			{5041,5038,5041,5000,5000,5000},
			{5041,5038,5041,5000,5000,5000},
			{5000,5041,5000,5000,5041,5000},
			{5000,5000,5000,5041,5038,5041},
			{5030,5030,5000,5000,5041,5030},
		};
		room[2] = new RoomClass(zone);
		zone = new int[][]{
			{5030,5030,5033,5030,5030},
			{5033,5000,5000,5000,5033},
			{5030,5000,5042,5000,5030},
			{5033,5000,5000,5000,5033},
			{5030,5030,5033,5030,5030},	
			};
		room[3] = new RoomClass(zone);
		zone = new int[][]{
			{5000,5000,5000,5000,5000},
			{5000,6000,6000,6000,5000},
			{5000,6000,6033,6000,5000},
			{5000,6000,6000,6000,5000},
			{5000,5000,5000,5000,5000}
		};
		room[4] = new RoomClass(zone);
		zone = new int[][]{
			{6036,6000,6041,6030},
			{6036,6000,6038,6041},
			{6036,6000,6000,6000},
			{6030,6036,6036,6036}
		};
		room[5] = new RoomClass(zone);
		zone = new int[][]{
			{11030,12036,11036,12036,11036,12036,11036,12036,11030},
			{12036,11000,12000,11000,12000,11000,12000,11000,12036},
			{11036,12000,11036,12036,11000,12036,11036,12000,11036},
			{12036,11000,12036,11000,12000,11000,12036,11000,12036},
			{11036,12000,11000,12000,11036,12000,11000,12000,11036},
			{12036,11000,12036,11000,12000,11000,12036,11000,12036},
			{11036,12000,11036,12036,11000,12036,11036,12000,11036},
			{12036,11000,12000,11000,12000,11000,12000,11000,12036},
			{11030,12036,11036,12036,11036,12036,11036,12036,11030}
		};
		room[6] = new RoomClass(zone);
		zone = new int[][]{
			{5030,5000,5000,5000,5030},
			{5000,5000,5000,5000,5000},
			{5000,5000,5030,5000,5000},
			{5000,5000,5000,5000,5000},
			{5030,5000,5000,5000,5030},
			{5000,5000,5000,5000,5000},
			{5000,5000,5030,5000,5000},
			{5000,5000,5000,5000,5000},
			{5030,5000,5000,5000,5030}
		};
		room[7] = new RoomClass(zone);
		zone = new int[][]{
			{5030,5000,5000,5000,5000,5000,5030},
			{5000,5000,5000,5000,5000,5000,5000},
			{5000,5000,5000,5000,5000,5000,5000},
			{5000,5000,5000,5030,5000,5000,5000},
			{5000,5000,5000,5000,5000,5000,5000},
			{5000,5000,5000,5000,5000,5000,5000},
			{5030,5000,5000,5000,5000,5000,5030}
		};
		room[8] = new RoomClass(zone);
		zone = new int[][]{
			{5000,5000,5000,5000,5000,5000,5000,5000,5000},
			{5000,6000,6000,6000,6000,6000,6000,6000,5000},
			{5000,6000,6000,6000,6000,6000,6000,6000,5000},
			{5000,6000,6000,6000,6000,6000,6000,6000,5000},
			{5000,6000,6000,6000,6000,6000,6000,6000,5000},
			{5000,5000,5000,5000,5000,5000,5000,5000,5000}
		};
		room[9] = new RoomClass(zone);
		zone = new int[][]{
			{5030,5000,6000,5000,5030},
			{5000,5000,6000,5000,5000},
			{6000,6000,6000,6000,6000},
			{5000,5000,6000,5000,5000},
			{5030,5000,6000,5000,5030}
		};
		room[10] = new RoomClass(zone);
        zone = new int[][]{
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5030,5000,5000,5030,5000,5000,5030,5000,5000,5030,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5030,5000,5000,5030,5000,5000,5030,5000,5000,5030,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000}
        };
        room[11] = new RoomClass(zone);
        zone = new int[][]{
            {5030,5030,5030,5000,5000,5000,5000,5000,5000,5030,5030,5030},
            {5030,5030,5030,5000,5000,5000,5000,5000,5000,5030,5030,5030},
            {5030,5030,5000,5000,5000,5000,5000,5000,5000,5000,5030,5030},
            {5000,5000,5000,5030,5000,5000,5000,5000,5030,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000},
            {5000,5000,5000,5030,5000,5000,5000,5000,5030,5000,5000,5000},
            {5030,5030,5000,5000,5000,5000,5000,5000,5000,5000,5030,5030},
            {5030,5030,5030,5000,5000,5000,5000,5000,5000,5030,5030,5030},
            {5030,5030,5030,5000,5000,5000,5000,5000,5000,5030,5030,5030}
        };
        room[12] = new RoomClass(zone);
        zone = new int[][]{
            {11000,12000,11000,12000,11000},
            {12000,11038,12000,11036,12000},
            {11000,12000,11000,12036,11000},
            {12000,11036,12000,11036,12000},
            {11000,12036,11000,12000,11000},
            {12000,11036,12000,11038,12000},
            {11000,12000,11000,12000,11000}
        };
        room[13] = new RoomClass(zone);
        zone = new int[][]{
            {5030,12036,11036,12036},
            {12038,11000,12000,11000},
            {11045,12000,11000,12000},
            {12038,11000,12000,11000},
            {5030,12036,11036,12036}
        };
        room[14] = new RoomClass(zone);
        zone = new int[][]{
            {9000,10000,9000,10000,9000,10000,9000},
            {10000,9000,10000,9000,10000,9000,10000},
            {9000,10000,9000,10000,9000,10000,9000},
            {10000,9000,10000,9000,10000,9000,10000},
            {9000,10000,9000,10000,9000,10000,9000},
            {10000,9000,10000,9000,10000,9000,10000},
            {9000,10000,9000,10000,9000,10000,9000}
        };
        room[15] = new RoomClass(zone);
        zone = null;
	}
	
	public boolean correctPlace(int x,int y){
        return Global.map[x][y].isWall()&&((!Global.map[x][y-1].isWall()^!Global.map[x][y+1].isWall())^(!Global.map[x-1][y].isWall()^!Global.map[x+1][y].isWall()));
    }
	
	public boolean findCell(){
    	for(int z2=0;z2<20;z2++){
    		z=rnd.nextInt(xr-xl+1)+xl;
    		z1=rnd.nextInt(yr-yl-1)+yl;
    		if(correctPlace(z,z1))
    			return true;
    	}
    	return false;
    }
    
    public boolean checkZone(int n,int m,int ln,int lm){
    	if(n+ln>Global.game.mw-3 || m+lm>Global.game.mh-3 || n<2 || m<2)
    		return false;
    	for(int n1=n;n1<n+ln+1;n1++)
    		for(int m1=m;m1<m+lm+1;m1++)
                if(!Global.map[n1][m1].isWall())
    				return false;
    	return true;
    }
	
    public void fillArea(int sx,int sy,int lx1,int ly1,int f){
		for(int y=sy;y<sy+ly1;y++)
			for(int x=sx;x<sx+lx1;x++){
				Global.map[x][y].f = f/1000;
				Global.map[x][y].o = f%1000;
				modifyTile(x,y,f/1000,f%1000);
			}
	}
    
    public void modifyTile(int px,int py,int f,int o){
    	Global.map[px][py].psb = Global.tiles[o].psb;
    	Global.map[px][py].vis = Global.tiles[o].vis;
    	Global.map[px][py].use = Global.tiles[o].use;
    }
    
    public void deleteObjects(int x,int y,int lx,int ly){
    	for(int x1=0;x1<lx;x1++)
    		for(int y1=0;y1<ly;y1++)
                if(!Global.map[x+x1][y+y1].isWall()){
    				Global.map[x+x1][y+y1].o=0;
    				modifyTile(x+x1,y+y1,Global.map[x+x1][y+y1].f,0);
    			}
    }
    
    public void horizontalMirror(int lx,int ly){
    	int temp;
    	for(int y=0;y<ly/2;y++)
    		for(int x=0;x<lx;x++){
    			temp=zone[x][y];
    			zone[x][y]=zone[x][ly-1-y];
    			zone[x][ly-1-y]=temp;
    		}
    }
    
    public void verticalMirror(int lx,int ly){
    	int temp;
    	for(int x=0;x<lx/2;x++)
    		for(int y=0;y<ly;y++){
    			temp=zone[x][y];
    			zone[x][y]=zone[lx-1-x][y];
    			zone[lx-1-x][y]=temp;
    		}
    }
    
    public void newZone(int lx,int ly,int n){
    	zone = new int[lx][ly];
    	zone = room[n].map.clone();
    }

    public void newRotateZone(int lx,int ly,int n){
        zone = new int[ly][lx];
        int[][] temp;
        temp = room[n].map.clone();
        for(int x=0;x<lx;x++)
            for(int y=0;y<ly;y++)
                zone[y][x] = temp[x][y];
    }

    public int getRoom(int x,int y){
		int xx;
		for(xx=0;xx<room1.length;xx++)
			if(room1[xx]!=null && x>=room1[xx].x && y>=room1[xx].y && x<=room1[xx].x+room1[xx].lx-1 && y<=room1[xx].y+room1[xx].ly-1)
				return xx;
		return -1;
	}

	public void mapGen(){
    	rc = 0;
        int lx,ly;
        fillArea(0,0,Global.game.mw,Global.game.mh,5030);
        for(int i=0;i<rc;i++)
            room1[i] = null;
        for(int x=0;x<Global.game.mw;x++)
            for(int y=0;y<Global.game.mh;y++){
                Global.map[x][y].deleteItems();
                Global.map[x][y].dis = false;
                Global.map[x][y].see = false;
            }
        while(Global.game.firstMob!=null){
            Global.game.firstMob.map.deleteMob();
            Global.game.firstMob.mob = null;
            Global.game.firstMob = Global.game.firstMob.next;
        }
    	int x2 = 0;
    	int y2 = 0;
    	boolean up,down,left,right;
        lx = rnd.nextInt(Global.game.mw/2)+16;
        ly = rnd.nextInt(Global.game.mh/2)+16;
        Global.mapview.camx = lx-2;
        Global.mapview.camy = ly-2;
        Global.hero.mx = lx+2;
        Global.hero.my = ly+2;
        Global.hero.x=216;
        Global.hero.y=376;
        fillArea(lx, ly, 5, 5, 5000);
        fillArea(lx + 2, ly + 2, 1, 1, 5039);
        room1[rc] = new RoomDBClass(x2,y2,lx,ly);
        xl = lx-1;
        xr = lx+5;
        yl = ly-1;
        yr = ly+5;
    	while(rc<mr-1){
    		if(findCell()){
                right=Global.map[z-1][z1].psb;
                left=Global.map[z+1][z1].psb;
                down=Global.map[z][z1-1].psb;
                up=Global.map[z][z1+1].psb;
	    		if((right^left)^(down^up)){
		    		int n=0;
		    		int b=rnd.nextInt(100);
		    		if(b<7){
		    			lx=4;
		    			ly=3;
		    			n=0;
		    		}
		    		if(b>6 && b<12){
		    			lx=ly=5;
		    			n=1;
		    		}
		    		if(b>11 && b<16){
		    			lx=8;
		    			ly=6;
		    			n=2;
		    		}
		    		if(b>15 && b<19){
		    			lx=ly=5;
		    			n=3;
		    		}
		    		if(b>18 && b<22){
		    			lx=ly=5;
		    			n=4;
		    		}
		    		if(b>21 && b<26){
		    			lx=ly=4;
		    			n=5;
		    		}
		    		if(b>25 && b<30){
		    			lx=ly=9;
		    			n=6;
		    		}
		    		if(b>29 && b<35){
		    			lx=9;
			    		ly=5;
		    			n=7;
		    		}
		    		if(b>34 && b<41){
		    			lx=ly=5;
		    			n=7;
		    		}
		    		if(b>40 && b<47){
	    				lx=ly=4;
		    			n=8;
		    		}
		    		if(b>46 && b<52){
		    			lx=ly=7;
		    			n=8;
		    		}
		    		if(b>51 && b<61){
		    			lx=6;
		    			ly=9;
		    			n=9;
		    		}
		    		if(b>60 && b<70){
		    			lx=ly=5;
		    			n=10;
		    		}
                    if(b>69 && b<78){
                        lx=12;
                        ly=16;
                        n=11;
                    }
                    if(b>77 && b<86){
                        lx=ly=n=12;
                    }
                    if(b>85 && b<92){
                        lx=7;
                        ly=5;
                        n=13;
                    }
                    if(b>91 && b<96){
                        lx=5;
                        ly=4;
                        n=14;
                    }
		    		if(b>95){
		    			lx=rnd.nextInt(8)+3;
			    		ly=rnd.nextInt(8)+3;
			    		n=100;
		    		}
		    		if(n!=100){
                        int tmp;
						switch(rnd.nextInt(13)){
	    					case 0:
                                newZone(lx,ly,n);
	    						horizontalMirror(lx,ly);
	    						break;
	    					case 2:
                                newZone(lx,ly,n);
                                verticalMirror(lx,ly);
	    						break;
	    					case 4:
                                newZone(lx,ly,n);
	    						verticalMirror(lx,ly);
	    						horizontalMirror(lx,ly);
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
                                verticalMirror(lx,ly);
                                break;
                            case 10:
                                newRotateZone(lx, ly, n);
                                tmp = lx;
                                lx = ly;
                                ly = tmp;
                                horizontalMirror(lx,ly);
                                break;
                            case 12:
                                newRotateZone(lx, ly, n);
                                tmp = lx;
                                lx = ly;
                                ly = tmp;
                                verticalMirror(lx,ly);
                                horizontalMirror(lx,ly);
                                break;
                            default:
                                newZone(lx,ly,n);
                                break;
						}
		    		}
		    		if(up){
		    			y2=z1-ly;
		    			if(n!=100){
		    				do{
		    					x2=z-rnd.nextInt(lx);
		    				}while(zone[z-x2][ly-1]%1000==30);
		    			}else{
		    				x2=z-rnd.nextInt(lx);
		    			}
		    		}
		    		if(down){
		    			y2=z1+1;
		    			if(n!=100){
			    			do{
			    				x2=z-rnd.nextInt(lx);
			    			}while(zone[z-x2][0]%1000==30);
		    			}else{
			    			x2=z-rnd.nextInt(lx);
			    		}
		    		}
		    		if(left){
		    			x2=z-lx;
		    			if(n!=100){
		    				do{
			    				y2=z1-rnd.nextInt(ly);
		    				}while(zone[lx-1][z1-y2]%1000==30);
		    			}else{
		    				y2=z1-rnd.nextInt(ly);
		    			}
		    		}
		    		if(right){
		    			x2=z+1;
		    			if(n!=100){
		    				do{
			    				y2=z1-rnd.nextInt(ly);
		    				}while(zone[0][z1-y2]%1000==30);
		    			}else{
		    				y2=z1-rnd.nextInt(ly);
		    			}
		    		}
		    		if(checkZone(x2-1,y2-1,lx+1,ly+1)){
		    			rc++;
		    			if(n!=100){
		    				for(int x=0;x<lx;x++)
			    				for(int y=0;y<ly;y++){
			    					int v=zone[x][y];
			    					Global.map[x2+x][y2+y].f = v/1000;
			    					Global.map[x2+x][y2+y].o = v%1000;
			    					modifyTile(x2+x,y2+y,v/1000,v%1000);
			    				}
		    				if(up)deleteObjects(z,z1-1,1,1);
			    			if(down)deleteObjects(z,z1+1,1,1);
			    			if(right)deleteObjects(z+1,z1,1,1);
			    			if(left)deleteObjects(z-1,z1,1,1);
		    			}else fillArea(x2,y2,lx,ly,5000);
		    			fillArea(z,z1,1,1,5031);
		    			if(x2<xl)xl=x2-1;
		    			if(x2+lx>xr)xr=x2+lx+1;
		    			if(xl<2)xl=2;
		    			if(xr>Global.game.mw-2)xr=Global.game.mw-2;
		    			if(y2<yl)yl=y2-1;
		    			if(y2+ly>yr)yr=y2+ly+1;
		    			if(yl<2)yl=2;
		    			if(yr>Global.game.mh-2)yr=Global.game.mh-2;
		    			room1[rc] = new RoomDBClass(x2,y2,lx,ly);
		    			if(rnd.nextInt(2)==0){
		    				if(up){
			    				int r=getRoom(z,z1+1);
			    				for(int x=0;x<lx;x++)
			    					if(getRoom(x2+x,z1+1)==r && !Global.map[x2+x][z1+1].isWall() && !Global.map[x2+x][z1-1].isWall())
                                        if(Global.map[x2+x][z1+1].f == Global.map[x2+x][z1-1].f)
			    						    Global.game.fillArea(x2+x,z1,1,1,Global.map[x2+x][z1+1].f,0);
			    			}
			    			if(down){
			    				int r=getRoom(z,z1-1);
			    				for(int x=0;x<lx;x++)
			    					if(getRoom(x2+x,z1-1)==r && !Global.map[x2+x][z1+1].isWall() && !Global.map[x2+x][z1-1].isWall())
                                        if(Global.map[x2+x][z1+1].f == Global.map[x2+x][z1-1].f)
                                            Global.game.fillArea(x2+x,z1,1,1,Global.map[x2+x][z1+1].f,0);
			    			}
			    			if(right){
			    				int r=getRoom(z-1,z1);
			    				for(int y=0;y<ly;y++)
			    					if(getRoom(z-1,y2+y)==r && !Global.map[z+1][y2+y].isWall() && !Global.map[z-1][y2+y].isWall())
                                        if(Global.map[z+1][y2+y].f == Global.map[z-1][y2+y].f)
                                            Global.game.fillArea(z,y2+y,1,1,Global.map[z+1][y2+y].f,0);
			    			}
			    			if(left){
			    				int r=getRoom(z+1,z1);
			    				for(int y=0;y<ly;y++)
			    					if(getRoom(z+1,y2+y)==r && !Global.map[z+1][y2+y].isWall() && !Global.map[z-1][y2+y].isWall())
                                        if(Global.map[z+1][y2+y].f == Global.map[z-1][y2+y].f)
                                            Global.game.fillArea(z,y2+y,1,1,Global.map[z+1][y2+y].f,0);
			    			}
		    			}
                    }
	    		}
	    	}else
	    		rc++;
            if(Global.game.curLvls==Global.game.maxLvl-1 && rc==mr-2)
                placeFinalRoom();
    	}
    	Global.mapview.los(Global.hero.mx,Global.hero.my);
        Global.game.updateZone();
        int x4,y4;
        for(int x=0;x<30+Global.game.curLvls*7;x++){
            do{
                x4 = rnd.nextInt(Global.game.mw);
                y4 = rnd.nextInt(Global.game.mh);
            }while(!Global.map[x4][y4].psb || Global.map[x4][y4].see || Global.map[x4][y4].hasMob());
            int en=rnd.nextInt(Global.game.maxMobs-Global.game.curLvls-1)+Global.game.curLvls;
            if(en<3 && rnd.nextInt(3)==0){
                if(Global.map[x4-1][y4].psb && !Global.map[x4-1][y4].hasItem())Global.game.createMob(x4-1,y4,en);
                if(Global.map[x4+1][y4].psb && !Global.map[x4+1][y4].hasItem())Global.game.createMob(x4+1,y4,en);
            }
            Global.game.createMob(x4,y4,en);
        }
        if(Global.game.curLvls<Global.game.maxLvl-1){
            while(true){
                x4 = rnd.nextInt(Global.game.mw);
                y4 = rnd.nextInt(Global.game.mh);
                if(Global.map[x4][y4].o==0 && !Global.map[x4][y4].see){
                    Global.map[x4][y4].o=40;
                    m=x4-2;
                    n=y4-2;
                    break;
                }
            }
        }
    }

    private void placeFinalRoom(){
        int lx = 7,ly = 7,n = 15;
        int x2=0,y2=0;
        boolean right,left,up,down;
        newZone(lx,ly,n);
        while(true){
            if(findCell()){
                right=Global.map[z-1][z1].psb;
                left=Global.map[z+1][z1].psb;
                down=Global.map[z][z1-1].psb;
                up=Global.map[z][z1+1].psb;
                if((right^left)^(down^up)){
                    if(up){
                        y2=z1-ly;
                        x2=z-rnd.nextInt(lx);
                    }
                    if(down){
                        y2=z1+1;
                        x2=z-rnd.nextInt(lx);
                    }
                    if(left){
                        x2=z-lx;
                        y2=z1-rnd.nextInt(ly);
                    }
                    if(right){
                        x2=z+1;
                        y2=z1-rnd.nextInt(ly);
                    }
                    if(checkZone(x2-1,y2-1,lx+1,ly+1)){
                        rc++;
                        for(int x=0;x<lx;x++)
                            for(int y=0;y<ly;y++){
                                int v=zone[x][y];
                                Global.map[x2+x][y2+y].f = v/1000;
                                Global.map[x2+x][y2+y].o = v%1000;
                                modifyTile(x2+x,y2+y,v/1000,v%1000);
                            }
                        fillArea(z,z1,1,1,5031);
                        Global.game.createMob(x2+3,y2+3,5);
                        Global.game.createMob(x2+4,y2+3,4);
                        Global.game.createMob(x2+2,y2+3,4);
                        Global.game.createMob(x2+3,y2+4,4);
                        Global.game.createMob(x2+3,y2+2,4);
                        break;
                    }
                }
            }
        }
    }

}