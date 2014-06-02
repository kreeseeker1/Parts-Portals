package com.tesc.sos2014.pools;

import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

import com.tesc.sos2014.objectenemies.FeraalkEnemy;
import com.tesc.sos2014.objects.Bullet;

public class FeraalkEnemyPool extends GenericPool <FeraalkEnemy>
{

public static FeraalkEnemyPool instance;
	
	public static FeraalkEnemyPool sharedDemiEnemyPool()
	{
		if(instance == null)
		{
			instance = new FeraalkEnemyPool();
		}
		return instance;
	}
	
	@Override
	public synchronized void batchAllocatePoolItems(int pCount)
	{
		super.batchAllocatePoolItems(pCount);
	}

	private FeraalkEnemyPool()
	{
		super();
	}
	
	@Override
	public synchronized FeraalkEnemy obtainPoolItem()
	{
		
		return super.obtainPoolItem();
	}

	@Override
	protected FeraalkEnemy onAllocatePoolItem()
	{
		//Log.v("Bullet", "BulletPool onAllocatePoolItem");
		FeraalkEnemy de = new FeraalkEnemy();
		de.setDead(false);
		
		de.aSprite.setVisible(true);
	
		
		return  de;
		
	}
	
	protected void onHandleRecycleItem(final FeraalkEnemy b)
	{
		
		super.onHandleRecycleItem(b);
		b.aSprite.setVisible(false);
		b.aSprite.setIgnoreUpdate(true);
		b.setDead(true);
		b.body.setActive(false);
		
	}
}
