package com.tesc.sos2014.objectenemies;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.tesc.sos2014.managers.ResourcesManager;

public class BeriusLEnemy extends BaseEnemy
	{

	public BeriusLEnemy()
		{
			super(ResourcesManager.getInstance().beriusl.deepCopy(), new long[] {100,100,100});
			
			
		}

		
		
		
	}
