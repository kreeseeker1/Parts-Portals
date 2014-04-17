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


public abstract class BaseEnemy extends AnimatedSprite {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	private Body body;

	private boolean goRight = false;
	private boolean goLeft = false;

	private int footContacts = 0;
	

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public BaseEnemy(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, com.tesc.sos2014.managers.ResourcesManager.getInstance().enemy.deepCopy(), vbo);
		createPhysics(camera, physicsWorld);
		this.animate(ENEMY_ANIMATE,0,2,true);
		//camera.setChaseEntity(this);

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) 
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		//dynamic bodies can collide with each other and kinematic and static bodies

		body.setUserData("enemy");    //Set the sprite image
		body.setFixedRotation(true); //wont tumble I assume

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) 
			{
				
				super.onUpdate(pSecondsElapsed);//This is very important to be in this exact spot
				//camera.onUpdate(0.1f);

				if (getY() <= 0) //Body falls below bottom of scene
				{
					onDie();
				}

				if (goRight) 
				{
					//super.onUpdate(pSecondsElapsed);
					body.setLinearVelocity(new Vector2(3, body.getLinearVelocity().y));//with the speed of 3 move right
					//I think that this is where we could add code to get the character to face the right direction
					//animate(ENEMY_ANIMATE, 0, 2,true);
				}
				if (goLeft) 
				{
					//super.onUpdate(pSecondsElapsed);
					body.setLinearVelocity(new Vector2(-3, body.getLinearVelocity().y));//with the speed f 3 move left
					//I think that this is where we could add code to get the character to face the right direction
					//animate(ENEMY_ANIMATE, 0, 2, true);
				}
			}
		});
	}

	public void runRight() {
		goRight = true;
		goLeft =false;
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
		//animate(ENEMY_ANIMATE, 0, 2, true);

		//final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };
		//super.onUpdate(pSecondsElapsed);
	//	animate(ENEMY_ANIMATE, 0, 2, true);
	}

	public void animateMe() {
		//final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
		//animate(ENEMY_ANIMATE, 0, 2, true);
	}//

	public void runLeft() {
		goRight = false;
		goLeft = true;
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	//	animate(ENEMY_ANIMATE, 0, 2, true);

	}

	public void jump() {
		if (footContacts < 1) {
			return;
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
	}

	public void increaseFootContacts() {
		footContacts++;
	}

	public void decreaseFootContacts() {
		footContacts--;
	}

	public abstract void onDie();
}
