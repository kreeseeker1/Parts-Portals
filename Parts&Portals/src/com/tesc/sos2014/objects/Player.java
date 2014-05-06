package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.pools.BulletPool;
import com.tesc.sos2014.scenes.GameScene;


public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	
	private boolean right = false;
	private boolean left = false;
	private boolean stop = false;
	private int footContacts = 0;
	
	public Body body;
	public AnimatedSprite as =null;
	public static Player instance;



	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
		{
			super(pX, pY, com.tesc.sos2014.managers.ResourcesManager.getInstance().player_region.deepCopy(), vbo);
			createPhysics(camera, physicsWorld);
			camera.setChaseEntity(this);
		}
	
	

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		as = new AnimatedSprite(mHeight, mHeight, ResourcesManager.getInstance().player_region.deepCopy(), MainGameEngineActivity.getSharedInstance().getSharedInstance().getVertexBufferObjectManager());

		body.setUserData(as);
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
			{
				@Override
				public void onUpdate(float pSecondsElapsed)
				{
					super.onUpdate(pSecondsElapsed);
					camera.onUpdate(0.1f);

					if (getY() <= 0)
					{
						onDie();
					}

					if (right)
					{
						body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));
					}
					if (left)
					{
						body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y));
					}

					if (stop)
					{
						body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
					}
				}
			});
	}
	

	public void runRight()
	{
		right = true;
		left = false;
		stop = false;

		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100 };

		animate(PLAYER_ANIMATE, 0, 6, true);
	}
	
	public void runLeft()
	{
		right = false;
		left = true;
		stop = false;
		
		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100 };

		animate(PLAYER_ANIMATE, 0, 6, true);

	}

	public void shoot()
	{
		Log.v("Player", "PLayer getting MainGameEngineActivity.getSharedInstance().mCurrentScene");
			
		GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
		Log.v("Player", "PLayer got MainGameEngineActivity.getSharedInstance().mCurrentScene");
	
		Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
		Log.v("Player", "Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();");
		Log.v("Physics World", scene.getPhysicsWorld().toString());
		
		b.sprite.setPosition(this.getX() ,this.getY());
		b.sprite.setVisible(true);
		b.sprite.detachSelf();
		
		scene.bulletCount++;
		scene.bulletList.add(b);
		scene.attachBullet(b);	

	}

	private static Player getSharedInstance()
	{
		
		return instance;
	}

	public boolean isFacingLeft()
	{
		return left;
	}

	public boolean isFacingRight()
	{
		return right;
	}

	public void stop()
	{
		right = false;
		left = false;
		stop = true;
	}

	public void jump()
	{
		if (footContacts < 1)
		{
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
			return;
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
	}

	public void increaseFootContacts()
	{
		footContacts++;
	}

	public void decreaseFootContacts()
	{
		footContacts--;
	}

	public abstract void onDie();
}
