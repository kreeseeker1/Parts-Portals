package com.tesc.sos2014.pools;


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
