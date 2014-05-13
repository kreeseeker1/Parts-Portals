package com.tesc.sos2014.pools;

import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

import com.tesc.sos2014.objects.Bullet;
import com.tesc.sos2014.objects.DemiEnemy;

public class DemiEnemyPool extends GenericPool <DemiEnemy>
{

public static DemiEnemyPool instance;
	
	public static DemiEnemyPool sharedDemiEnemyPool()
	{
		if(instance == null)
		{
			instance = new DemiEnemyPool();
		}
		return instance;
	}
	
	@Override
	public synchronized void batchAllocatePoolItems(int pCount)
	{
		super.batchAllocatePoolItems(pCount);
	}

	private DemiEnemyPool()
	{
		super();
	}
	
	@Override
	public synchronized DemiEnemy obtainPoolItem()
	{
		
		return super.obtainPoolItem();
	}

	@Override
	protected DemiEnemy onAllocatePoolItem()
	{
		//Log.v("Bullet", "BulletPool onAllocatePoolItem");
		DemiEnemy de = new DemiEnemy();
		de.setDead(false);
		
		de.aSprite.setVisible(true);
	
		
		return  de;
		
	}
	
	protected void onHandleRecycleItem(final DemiEnemy b)
	{
		
		super.onHandleRecycleItem(b);
		b.aSprite.setVisible(false);
		b.aSprite.setIgnoreUpdate(true);
		b.setDead(true);
		b.body.setActive(false);
		
	}
}
