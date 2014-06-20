package com.tesc.sos2014.utilities;


import java.util.ArrayList;

import com.tesc.sos2014.objectenemies.BaseEnemy;

public class Tiles
	{

	public ArrayList<Coordinate> getFourTiles(BaseEnemy baseEnemy)
	{
		ArrayList<Coordinate> ft = new ArrayList<Coordinate>();
		
		Coordinate cl = new Coordinate((int)baseEnemy.aSprite.getX()-50,(int) baseEnemy.aSprite.getY());
		
		Coordinate cr = new Coordinate((int)baseEnemy.aSprite.getX()+ 100,(int) baseEnemy.aSprite.getY());
		
		Coordinate cu = new Coordinate((int)baseEnemy.aSprite.getX(),(int) baseEnemy.aSprite.getY()+100);
		
		Coordinate cd = new Coordinate((int)baseEnemy.aSprite.getX(),(int) baseEnemy.aSprite.getY()-50);
		
		ft.add(cl);
		ft.add(cr);
		ft.add(cu);
		ft.add(cd);
		
		
		
		return ft;
		
		
	}
	
	public boolean isInRange(float ex, float ey, float tx, float ty)
	{
		
		float txc = tx+25;
		float tyc = ty + 25;
		
		
		if (txc <= ex && txc < ex + 50 && tyc <= ey && tyc < ey + 50)
		{
			return true;
		}
		
		else if(txc >= ex && txc > ex - 50 && tyc >= ey && tyc > ey - 50)
		{
			return true;
		}
		else return false;
		
	}
	
	

	}
