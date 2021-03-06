package com.tesc.sos2014.objectenemies;

import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.utilities.Coordinate;
import com.tesc.sos2014.utilities.Tiles;

public class BaseEnemy
{

	long[] ENEMY_ANIMATE ;
	ITiledTextureRegion texture;
	public Body body;
	private boolean goRight = false;
	private boolean goLeft = false;
	private int footContacts = 0;
	private int life = 100;
	public int speed = 10;
	public int jumpert = 100;
	public AnimatedSprite aSprite;
	
	public Tiles t = new Tiles();
	
	
	public int runRight,runLeft = 50;
	
	public int aStarIndex = 0;
	
	private boolean isDead = false;


	public BaseEnemy(ITiledTextureRegion t, long[] l)
		{

			ENEMY_ANIMATE = l;
			this.texture = t.deepCopy();
			aSprite = new AnimatedSprite(0, 0, this.texture, MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
			aSprite.setSize(50, 45);
			aSprite.animate(ENEMY_ANIMATE);
			
		}
	
	public void getNextTiles()
	{
		ArrayList<Coordinate> tl = t.getFourTiles(this);
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
		
		//body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 10));
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