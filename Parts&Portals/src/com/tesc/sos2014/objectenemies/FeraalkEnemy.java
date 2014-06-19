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
import com.tesc.sos2014.objects.Player;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;

public class FeraalkEnemy extends BaseEnemy
{

	final static long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	public Body body;
	private boolean goRight = false;
	private boolean goLeft = false;
	
	int pacingcountl = 15;
	int pacingcountr = 15;
	
	Random r = new Random();
	private int runRight = r.nextInt(50);
	private int runLeft = r.nextInt(50);
	private boolean canJump = true;
	private int footContacts = 0;
	private int life = 100;
	public int speed = 6;
	public int jumpert = 100;
	public AnimatedSprite aSprite;
	
	private boolean isDead = false;


	public FeraalkEnemy()
		{

			super(ResourcesManager.getInstance().enemy.deepCopy(), ENEMY_ANIMATE);
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
	
	public boolean canSee(Player player)
		{
			if(player.getX() < aSprite.getX()-250 || player.getX() > aSprite.getX() + 250)
			{
				return false;
			}
			else return true;
		}
	
	public boolean onMyLeft(Player player)
		{
			if(player.getX() < aSprite.getX())
			{
				return true;
			}
			else return false;
		}
	
	public boolean onMyRight(Player player)
		{
			if(player.getX() > aSprite.getX())
			{
				return true;
			}
			else return false;
		}
	
	public void jumpAt(Player player)
		{
			if(player.getX() > aSprite.getX())
			{
			
				body.setLinearVelocity(speed*3, body.getLinearVelocity().y+.5f);
				aSprite.setFlippedHorizontal(true);
			}
			else if(player.getX() < aSprite.getX())
			{
				body.setLinearVelocity(-1 *speed*3,body.getLinearVelocity().y+.5f);
				aSprite.setFlippedHorizontal(false);
			}
		}
	
	public void jumpBack(Player player)
		{
			if(player.getX() > aSprite.getX())
			{
			
				body.setLinearVelocity(-1 *speed*3, body.getLinearVelocity().y+.5f);
				aSprite.setFlippedHorizontal(true);
			}
			else if(player.getX() < aSprite.getX())
			{
				body.setLinearVelocity(speed*3,body.getLinearVelocity().y+.5f);
				aSprite.setFlippedHorizontal(false);
			}
		}
	

	public void paceFacing(Player player)
		{
			if(player.getX() > aSprite.getX())
			{
				aSprite.setFlippedHorizontal(true);
				
			}
			else if(player.getX() < aSprite.getX())
			{
				aSprite.setFlippedHorizontal(false);
			}
			
			if (pacingcountl > 0)
			{
				body.setLinearVelocity(new Vector2(speed/2, body.getLinearVelocity().y));
				pacingcountl --;
			}
			else if(pacingcountl <= 0 && pacingcountr > 0)
			{
				body.setLinearVelocity(new Vector2(-1 * speed/2, body.getLinearVelocity().y));
				pacingcountr --;
			}
			else if(pacingcountl <=0 && pacingcountr <= 0)
			{
				pacingcountl = 15;
				pacingcountr = 15;
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