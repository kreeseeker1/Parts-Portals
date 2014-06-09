package com.tesc.sos2014.objectenemies;

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

public class EthsersEnemy extends BaseEnemy
{

	public final long[] ENEMY_ANIMATE = new long[] { 100, 100 };
	public Body body;
	public int maxMovement = 200;
	private boolean goRight = false;
	private boolean goLeft = false;
	private int footContacts = 0;
	private int life = 100;
	public int speed = 10;
	public int jumpert = 100;
	public AnimatedSprite aSprite;
	
	public float x,y;
	
	private boolean isDead = false;


	public EthsersEnemy(float x, float y)
		{

			
			super(ResourcesManager.getInstance().ethsers.deepCopy(), new long[]{100,100});
			this.x =x;
			this.y =y;
			//aSprite.setPosition(x,y);
		}
	
	public EthsersEnemy()
		{
			super(ResourcesManager.getInstance().ethsers.deepCopy(), new long[]{100,100});
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


	public void runRight()
	{
		body.setLinearVelocity(new Vector2(-speed, body.getLinearVelocity().y));// with
		
		goRight = true;
		goLeft = false;
		
		aSprite.setFlippedHorizontal(false);
		

	}

	public void runLeft()
	{
		body.setLinearVelocity(new Vector2(speed, body.getLinearVelocity().y));// with
		
		goRight = false;
		goLeft = true;
		aSprite.setFlippedHorizontal(true);
		

	}
	
	public void animateMe()
	{
		
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
		
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
			return;
		
		
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