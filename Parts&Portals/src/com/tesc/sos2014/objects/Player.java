package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.pools.BulletPool;
import com.tesc.sos2014.scenes.GameScene;

public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;
	
	public static Player instance;

	private boolean right = false;
	private boolean left = false;
	private boolean stop = false;

	private int footContacts = 0;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
		{
			super(pX, pY, com.tesc.sos2014.managers.ResourcesManager.getInstance().player_region, vbo);
			createPhysics(camera, physicsWorld);
			camera.setChaseEntity(this);
		}
	
	

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("player");
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

	public void shoot()
	{
		GameScene scene = GameScene.getSharedInstance();
		Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();

		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		Body bulletBody = PhysicsFactory.createCircleBody(GameScene.getSharedInstance().physicsWorld, (IEntity) b, BodyType.DynamicBody, fd);
		GameScene.getSharedInstance().physicsWorld.registerPhysicsConnector(new PhysicsConnector((IEntity) b, bulletBody, true, true));
		final Vector2 speed = Vector2Pool.obtain(50, 0);

		bulletBody.setLinearVelocity(speed);
		
		
		b.sprite.setPosition(Player.getSharedInstance().getX(),Player.getSharedInstance().getY());
		b.sprite.setVisible(true);
		b.sprite.detachSelf();
		scene.attachChild(b.sprite);
		scene.bulletList.add(b);
		
		MoveYModifier mod = new MoveYModifier(1.5f,b.sprite.getY(),-b.sprite.getHeight());
		
		b.sprite.registerEntityModifier(mod);
		scene.bulletCount++;

	}

	private static Player getSharedInstance()
	{
		
		return instance;
	}

	/*
	 * public Body fireGun(Scene s) { float x = this.getX(); float y =
	 * this.getY();
	 * 
	 * Body b = null;
	 * 
	 * b.setBullet(true);
	 * 
	 * b.setActive(true);
	 * 
	 * 
	 * //CircleShape bullet = null;
	 * 
	 * Vector2 v = new Vector2(x,y); bullet.setRadius(4l);
	 * bullet.setPosition(v);
	 * 
	 * //s.attachChild((b);
	 * 
	 * if(facingLeft()) { b.setLinearVelocity(-3, 0);
	 * 
	 * } else if(facingRight()) {
	 * 
	 * return b;}
	 */

	// }

	public boolean facingLeft()
	{
		return left;
	}

	public boolean facingRight()
	{
		return right;
	}

	public void stop()
	{
		right = false;
		left = false;
		stop = true;
	}

	public void runLeft()
	{
		right = false;
		left = true;
		stop = false;

	}

	public void jump()
	{
		if (footContacts < 1)
		{
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
