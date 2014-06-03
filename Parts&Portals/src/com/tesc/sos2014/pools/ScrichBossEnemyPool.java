package com.tesc.sos2014.pools;

import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

import com.tesc.sos2014.objectenemies.ScrichBossEnemy;
import com.tesc.sos2014.objects.Bullet;

public class ScrichBossEnemyPool  extends GenericPool<ScrichBossEnemy>
{
	public static ScrichBossEnemyPool instance;
	
	public static ScrichBossEnemyPool sharedBulletPool()
	{
		if(instance == null)
		{
			instance = new ScrichBossEnemyPool();
		}
		return instance;
	}
	
	@Override
	public synchronized void batchAllocatePoolItems(int pCount)
	{
		super.batchAllocatePoolItems(pCount);
	}

	private ScrichBossEnemyPool()
	{
		super();
	}
	
	@Override
	public synchronized ScrichBossEnemy obtainPoolItem()
	{
		
		return super.obtainPoolItem();
	}

	@Override
	protected ScrichBossEnemy onAllocatePoolItem()
	{
		Log.v("Bullet", "BulletPool onAllocatePoolItem");
		ScrichBossEnemy b = new ScrichBossEnemy();
		
		b.aSprite.setVisible(true);
	
		
		return b;
		
	}
	
	protected void onHandleRecycleItem(final ScrichBossEnemy b)
	{
		
		super.onHandleRecycleItem(b);
		b.aSprite.setVisible(false);
		b.aSprite.setIgnoreUpdate(true);
		
		
		b.body.setActive(false);
		
	}

}
