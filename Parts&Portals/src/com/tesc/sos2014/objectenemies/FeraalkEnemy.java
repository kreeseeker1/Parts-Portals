package com.tesc.sos2014.objectenemies;

import java.util.Random;

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

public class FeraalkEnemy
{

	final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	public Body body;
	private boolean goRight = false;
	private boolean goLeft = false;
	
	Random r = new Random();
	private int runRight = r.nextInt(50);
	private int runLeft = r.nextInt(50);
	private boolean canJump = true;
	private int footContacts = 0;
	private int life = 100;
	public int speed = 10;
	public int jumpert = 100;
	public AnimatedSprite aSprite;
	
	private boolean isDead = false;


	public FeraalkEnemy()
		{

			
			aSprite = new AnimatedSprite(0, 0, ResourcesManager.getInstance().enemy.deepCopy(), MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
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
		body.setLinearVelocity(new Vector2(-speed, body.getLinearVelocity().y));// with
		
		goRight = true;
		goLeft = false;
		
		aSprite.setFlippedHorizontal(false);
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };

	}

	public void runLeft()
	{
		body.setLinearVelocity(new Vector2(speed, body.getLinearVelocity().y));// with
		
		goRight = false;
		goLeft = true;
		aSprite.setFlippedHorizontal(true);
		final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
		// animate(ENEMY_ANIMATE, 0, 2, true);

	}
	
	public void animateMe()
	{
			aSprite.animate(ENEMY_ANIMATE);
		//final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	}
	
	public void pace()
		{
			if(runLeft > 0)
			{
				runLeft();
				runLeft --;
			}
			if(runLeft <= 0 && runRight > 0)
			{
				runRight();
				runRight --;
			}
			if(runLeft <=0 && runRight <= 0)
			{
				runLeft = 50;
				runRight = 50;
			}
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
		if (canJump)
		{
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
			return;
		}
		
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

	public boolean isDead()
	{
		return isDead;
	}

	public void setDead(boolean isDead)
	{
		this.isDead = isDead;
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