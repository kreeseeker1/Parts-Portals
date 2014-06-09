package com.tesc.sos2014.objectenemies;

import com.tesc.sos2014.managers.ResourcesManager;

public class BeriusEnemy extends BaseEnemy
{

		public float x,y;
		public int myleader = 0;
		
	public BeriusEnemy()
		{
			super(ResourcesManager.getInstance().berius.deepCopy(), new long[] {100,100,100});
			
		}
	
	public BeriusEnemy(float x, float y)
		{
			super(ResourcesManager.getInstance().berius.deepCopy(), new long[]{100,100, 100});
			this.x = x;
			this.y = y;
		
		}


}