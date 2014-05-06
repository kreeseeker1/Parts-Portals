package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tesc.sos2014.managers.ResourcesManager;


public abstract class Health extends AnimatedSprite {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	final long[] HEALTH_ANIMATE = new long[] { 100, 100 };
	private Body body;
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Health(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		//x = com.tesc.sos2014.managers.ResourcesManager.getInstance().enemy.deepCopy()
		
		super(pX, pY, ResourcesManager.getInstance().health.deepCopy(), vbo);
		super.setSize(50, 45);
		//this.setSize(75, 50);
		createPhysics(camera, physicsWorld);
		this.animate(HEALTH_ANIMATE,0,1,true);
		
		//camera.setChaseEntity(this);

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) 
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(-5, 0, 0));
		//dynamic bodies can collide with each other and kinematic and static bodies

		body.setUserData("health");    //Set the sprite image
		body.setFixedRotation(false); //wont tumble I assume
		

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) 
			{
				
				super.onUpdate(pSecondsElapsed);//This is very important to be in this exact spot
				
			}
		});
	}


	public void animateMe() {
		
		final long[] HEALTH_ANIMATE = new long[] { 100, 100 };
		
	}//
	
	public void squish()
	{
		body.setActive(false);
	}

	public abstract void onDie();
}
