package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
	 public BatchedPseudoSpriteParticleSystem jps;//jumpjps
	
	private int jumpTimer = 0;
	private int fuelLevel = 150;
	private int jumpRecharge = 75;
	private int dblJumpTimer = 50;
	private boolean jumping = false;
	public boolean isParticleSpawned = true;
	PointParticleEmitter pe;
	boolean jetfireCalled = false;
	
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
			
			//jetfire();
		}
	
	

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void jetfire()
	{
		GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
		
		pe = new PointParticleEmitter(this.getX(), this.getY());
		 
        jps =  new BatchedPseudoSpriteParticleSystem(pe, 8, 12, 200, ResourcesManager.getInstance().jetfire, scene.getVbom());
		jps.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		jps.addParticleInitializer(new VelocityParticleInitializer<Entity>(0,0,0,-25));
		jps.addParticleInitializer(new AccelerationParticleInitializer<Entity>(5, -11));
		jps.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
		jps.addParticleInitializer(new ColorParticleInitializer<Entity>(1.0f, 1.0f, 0.0f));
		jps.addParticleInitializer(new ExpireParticleInitializer<Entity>(.8f,2f));

		jps.addParticleModifier(new ScaleParticleModifier<Entity>(0, 5, 2.5f, 2.0f));
		jps.addParticleModifier(new ColorParticleModifier<Entity>(1.5f, 2.5f, 1.0f, 2.0f, 1.0f, 1.0f, 0.0f, 1.0f));
		jps.addParticleModifier(new AlphaParticleModifier<Entity>(1.5f, 2.5f, 1.0f, 0.0f));

		scene.attachChild(jps);
		jps.setParticlesSpawnEnabled(false);   
    	   
    	
    
        
        
        //BOOLEAN TO USE FOR START STO EMITION
       
	}
	
	
	public int getJumpTimer()
	{
		return jumpTimer;
	}



	public void setJumpTimer(int jumpTimer)
	{
		this.jumpTimer = jumpTimer;
	}



	public boolean isParticleSpawned()
	{
		return isParticleSpawned;
	}



	public void setParticleSpawned(boolean isParticleSpawned)
	{
		this.isParticleSpawned = isParticleSpawned;
	}



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

					if(jetfireCalled == false)
					{
						jetfire();
						jetfireCalled = true;
						jumpTimer = fuelLevel;
					}
					if (getY() <= 0)
					{
						onDie();
					}
					
					if(jumping)
					{
						
						jumpTimer --;
						
						if(jumpTimer >= 1)
						{
							
							GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
						
							scene.fuelText.setText(("Fuel: "+ jumpTimer));
							if(jumpTimer > fuelLevel /2)
							{
								scene.fuelText.setColor(Color.rgb(0, 255, 0));
							}
							else if (jumpTimer > fuelLevel/3 && jumpTimer< fuelLevel/ 2)
							{
								scene.fuelText.setColor(Color.rgb(255, 255, 0));
							}
							else
							{
								scene.fuelText.setColor(Color.rgb(255, 0, 0));
							}
						
						body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 4));
						pe.setCenter(getX(), getY());
						
						
						}
						else if(jumpTimer <= 0)
						{
							GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
							
							
							scene.fuelText.setText(("Fuel: "+ 0 + "(Charging)" ));
							jumpRecharge --;
							pe.setCenter(-1000, -1000);
							
						}
						if(jumpRecharge <=0 && jumpTimer <=0)
						{
							pe.setCenter(-1000, -1000);
							jumpRecharge = 75;
							jumpTimer = fuelLevel;
						}
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

		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100,100,100 };

		animate(PLAYER_ANIMATE, 0, 8, true);
	}
	
	public void runLeft()
	{
		right = false;
		left = true;
		stop = false;
		
		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100, 100 ,100,100 };

		animate(PLAYER_ANIMATE, 0, 8, true);

	}

	public boolean isJumping()
	{
		return jumping;
	}



	public void setJumping(boolean jumping)
	{
		this.jumping = jumping;
	}



	public void shoot()
	{
		//Log.v("Player", "PLayer getting MainGameEngineActivity.getSharedInstance().mCurrentScene");
			
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
		jumping = true;
		/*if (footContacts < 1)
		{
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
			return;
		}
		*///body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
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
