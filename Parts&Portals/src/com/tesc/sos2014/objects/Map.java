package com.tesc.sos2014.objects;

public class Map
{

	int[][][] floorPlan;
	int[][][] wallPlan;
	int[][][] entityPlan;
	
	
	public Map(int[][][] floorPlan, int [][][] wallPlan, int [][][] entityPlan)
	{
		this.floorPlan = floorPlan;
		this.wallPlan = wallPlan;
		this.entityPlan = entityPlan;
	}
	
	public Map(int[][][] floorPlan, int [][][] wallPlan)
	{
		this.floorPlan = floorPlan;
		this.wallPlan = wallPlan;
	}


	public int[][][] getFloorPlan()
	{
		return floorPlan;
	}


	public int[][][] getWallPlan()
	{
		return wallPlan;
	}


	public int[][][] getEntityPlan()
	{
		return entityPlan;
	}
	
	
	
	
}
