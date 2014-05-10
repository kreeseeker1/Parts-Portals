package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;

public class DemiEnemy
{

	final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	public Body body;
	private boolean goRight = false;
	private boolean goLeft = false;
	private int footContacts = 0;
	private int life = 100;
	public int speed = 3;
	public AnimatedSprite aSprite;


	public DemiEnemy()
		{

			aSprite = new AnimatedSprite(0, 0, ResourcesManager.getInstance().enemy, MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
			aSprite.setSize(50, 45);
			aSprite.animate(ENEMY_ANIMATE);
		}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	public void takeDamage(int dmg)
	{
		this.life += dmg;
	}


	/*private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, aSprite, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(-53, 0, 0));
		// dynamic bodies can collide with each other and kinematic and static
		// bodies

		body.setUserData("enemy"); // Set the sprite image
		body.setFixedRotation(false); // wont tumble I assume
	}*/
		/*physicsWorld.registerPhysicsConnector(new PhysicsConnector(aSprite, body, true, false)
			{
				@Override
				public void onUpdate(float pSecondsElapsed)
				{

					super.onUpdate(pSecondsElapsed);// This is very important to
													// be in this exact spot
					// camera.onUpdate(0.1f);

					if (aSprite.getY() <= 0) // Body falls below bottom of scene
					{
						onDie();
					}

					if (goRight)
					{
						// super.onUpdate(pSecondsElapsed);
						body.setLinearVelocity(new Vector2(speed, body.getLinearVelocity().y));// with
																								// the
																								// speed
																								// of
																								// 3
																								// move
																								// right
						// I think that this is where we could add code to get
						// the character to face the right direction
						// animate(ENEMY_ANIMATE, 0, 2,true);
					}
					if (goLeft)
					{
						// super.onUpdate(pSecondsElapsed);
						body.setLinearVelocity(new Vector2(-speed, body.getLinearVelocity().y));// with
																								// the
																								// speed
																								// f
																								// 3
																								// move
																								// left
						// I think that this is where we could add code to get
						// the character to face the right direction
						// animate(ENEMY_ANIMATE, 0, 2, true);
					}
				}
			});
	}*/

	public void runRight()
	{
		goRight = true;
		goLeft = false;
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };

	}

	public void runLeft()
	{
		goRight = false;
		goLeft = true;
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
		// animate(ENEMY_ANIMATE, 0, 2, true);

	}
	
	public void animateMe()
	{
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	}

	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public void jump()
	{
		if (footContacts < 1)
		{
			return;
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
	}

	public void squish()
	{
		body.setActive(false);
		aSprite.setIgnoreUpdate(true);
		aSprite.setVisible(false);
	}

	public boolean isGoRight()
	{
		return goRight;
	}

	public void setGoRight(boolean goRight)
	{
		this.goRight = goRight;
	}

	public boolean isGoLeft()
	{
		return goLeft;
	}

	public void setGoLeft(boolean goLeft)
	{
		this.goLeft = goLeft;
	}

	public int setFootContactsZero()
	{
		footContacts = 0;
		return footContacts;
	}

	public int setFootContactsOne()
	{
		footContacts = 1;
		return footContacts;
	}

	public void increaseFootContacts()
	{
		footContacts++;
	}

	public void decreaseFootContacts()
	{
		footContacts--;
	}

	
}