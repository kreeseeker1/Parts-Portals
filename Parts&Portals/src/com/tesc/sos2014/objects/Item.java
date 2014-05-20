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


public abstract class Item  {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	public final long[] ITEM_ANIMATE = new long[] { 0 };
	public Body body;
	public AnimatedSprite as;
	public String itemType = null;
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	public Item()
	{
		
	}
	
	
	public Item(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		//x = com.tesc.sos2014.managers.ResourcesManager.getInstance().enemy.deepCopy()
		
		as = new AnimatedSprite(pX, pY, ResourcesManager.getInstance().health.deepCopy(), vbo);
		as.setSize(50, 45);
		//this.setSize(75, 50);
		createPhysics(camera, physicsWorld);
		//as.animate(ITEM_ANIMATE,0,1,true);
		
		//camera.setChaseEntity(this);

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void createPhysics(final Camera camera, PhysicsWorld physicsWorld) 
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, as, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(-5, 0, 0));
		//dynamic bodies can collide with each other and kinematic and static bodies

		body.setUserData(itemType);    //Set the sprite image
		body.setFixedRotation(false); //wont tumble I assume
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(as, body, true, false) {
		
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
