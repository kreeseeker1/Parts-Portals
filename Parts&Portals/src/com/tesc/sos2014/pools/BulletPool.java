package com.tesc.sos2014.pools;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.objects.Bullet;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.scenes.GameScene;

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
	
	private BulletPool()
	{
		super();
	}
	

	@Override
	protected Bullet onAllocatePoolItem()
	{
		Log.v("Bullet", "BulletPool onAllocatePoolItem");
		Bullet b = new Bullet();
		
       GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
		
		
		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		
		
		 Body bulletBody = PhysicsFactory.createCircleBody(scene.physicsWorld, b.sprite, BodyType.DynamicBody, fd);
		bulletBody.setActive(true);
		
		
		//scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite,bulletBody,true,true));
		final Vector2 speed = Vector2Pool.obtain(50,0);
		
		//bulletBody.setUserData(sprite.getTextureRegion());
		
		bulletBody.setLinearVelocity(speed);
		Vector2Pool.recycle(speed);
		
		
	//	bulletBody.setUserData(b.sprite);
		bulletBody.setUserData(ResourcesManager.getInstance().bullet.deepCopy());
		//b.bulletBody.setLinearVelocity(4, 5);
		
		
		return  b;
		
	}
	
	protected void onHandleRecycleItem(final Bullet b)
	{
		/*Log.v("Bullet Pool", "Bullet recycled" + b.sprite.getX());
		b.sprite.clearEntityModifiers();
		b.sprite.clearUpdateHandlers();
		b.sprite.setVisible(false);
		b.sprite.detachSelf();
		
		b.bulletBody.setLinearVelocity(0, 0);
		b.bulletBody.setAngularVelocity(0);
		b.bulletBody.setType(BodyType.StaticBody);
		b.bulletBody.setActive(false);
		b.bulletBody.setTransform(-5, -5, 0);*/
		
		
		
	}

}
