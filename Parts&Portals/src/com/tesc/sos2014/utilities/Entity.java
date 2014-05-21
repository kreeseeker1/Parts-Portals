package com.tesc.sos2014.utilities;

public class Entity {
	
	private Coordinate coordinate;
	private MyColor color;
	
	public Entity(Coordinate coordinate, MyColor color) {
		this.coordinate = coordinate;
		this.color = color;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public MyColor getColor() {
		return color;
	}

	public void setColor(MyColor color) {
		this.color = color;
	}

	
}
