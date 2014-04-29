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
		// TODO Auto-generated method stub
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
		Log.v("Bullet Pool", "Bullet recycled" + b.sprite.getTag());
		b.sprite.clearEntityModifiers();
		b.sprite.clearUpdateHandlers();
		b.sprite.setVisible(false);
		b.sprite.detachSelf();
		/*
		b.bulletBody.setLinearVelocity(0, 0);
		b.bulletBody.setAngularVelocity(0);
		b.bulletBody.setType(BodyType.StaticBody);
		b.bulletBody.setActive(false);
		b.bulletBody.setTransform(-5, -5, 0);*/
		
		
		
	}

}
