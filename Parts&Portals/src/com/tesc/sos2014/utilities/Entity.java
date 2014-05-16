package com.tesc.sos2014.utilities;

import android.graphics.Color;


public class Entity {
	
	private Coordinate coordinate;
	private int pixelColor;
	private String typeID;
	
	public Entity(Coordinate coordinate, int pixelColor, String typeID) {
		this.coordinate = coordinate;
		this.pixelColor = pixelColor;
		this.typeID = typeID;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public int getColor() {
		return pixelColor;
	}

	public void setColor(int pixelColor) {
		this.pixelColor = pixelColor;
	}
	
	
	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
	
	
}
