package com.tesc.sos2014.objects;

public class GameData
{

	int x = 0,y = 0;
	String typeID= null;
	
	
	public GameData(int x, int y, String type)
	{
		this.x = x;
		this.y= y;
		this.typeID = type;
	}
	
	public int getX()
	{
		return x;
	}


	public void setX(int x)
	{
		this.x = x;
	}


	public int getY()
	{
		return y;
	}


	public void setY(int y)
	{
		this.y = y;
	}


	public String getTypeID()
	{
		return typeID;
	}


	public void setTypeID(String typeID)
	{
		this.typeID = typeID;
	}
	


	
	
	
	
}
