package com.tesc.sos2014.pools;

import org.andengine.util.adt.pool.GenericPool;

import com.tesc.sos2014.objects.Monkey;

public class MonkeyPool// extends GenericPool <Monkey>
{

	public static MonkeyPool instance;
	
	public static MonkeyPool sharedMonkeyPool()
	{
		if(instance == null)
		{
			instance = new MonkeyPool();
		}
		return instance;
	}
	
	private MonkeyPool()
	{
		super();
	}
	
	/*@Override
	protected Monkey onAllocatePoolItem()
	{
		
		return new Monkey();
	}*/

}
