package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;


public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	private Body body;
	
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
				if(left)
				{
					body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y)); 
				}
				
				if(stop)
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
		
		final long[] PLAYER_ANIMATE = new long[] { 100,100,100,100,100,100,100 };
		
		animate(PLAYER_ANIMATE, 0, 6, true);
	}
	
	/*public Body fireGun(Scene s)
	{
		float x = this.getX();
		float y = this.getY();
		
		Body b = null;
		
		b.setBullet(true);
		
		b.setActive(true);
		
		
		//CircleShape bullet = null;
		
		Vector2 v = new Vector2(x,y);
		bullet.setRadius(4l);
		bullet.setPosition(v);
		
		//s.attachChild((b);
		
		if(facingLeft())
		{
			b.setLinearVelocity(-3, 0);
			
		}
		else if(facingRight())
		{

		return b;}*/
		
		
		
	//}
	
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
	
	
	public void runLeft() {
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
