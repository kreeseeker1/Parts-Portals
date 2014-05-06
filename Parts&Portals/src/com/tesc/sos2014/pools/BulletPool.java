package com.tesc.sos2014.pools;

import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

import com.tesc.sos2014.objects.Bullet;

public class BulletPool  extends GenericPool<Bullet>
{
	public static BulletPool instance;
	
	public static BulletPool sharedBulletPool()
	{
		if(instance == null)
		{
			instance = new BulletPool();
		}
		return instance;
	}
	
	@Override
	public synchronized void batchAllocatePoolItems(int pCount)
	{
		super.batchAllocatePoolItems(pCount);
	}

	private BulletPool()
	{
		super();
	}
	
	@Override
	public synchronized Bullet obtainPoolItem()
	{
		
		return super.obtainPoolItem();
	}

	@Override
	protected Bullet onAllocatePoolItem()
	{
		Log.v("Bullet", "BulletPool onAllocatePoolItem");
		Bullet b = new Bullet();
		
		b.sprite.setVisible(true);
	
		
		return b;
		
	}
	
	protected void onHandleRecycleItem(final Bullet b)
	{
		
		super.onHandleRecycleItem(b);
		b.sprite.setVisible(false);
		b.sprite.setIgnoreUpdate(true);
		
		b.bulletBody.setActive(false);
		
	}

}
