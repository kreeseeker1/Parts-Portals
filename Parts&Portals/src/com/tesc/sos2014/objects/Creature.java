package com.tesc.sos2014.objects;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;

public abstract class Creature
	{

		long[] CREATURE_ANIMATE = new long[] { 100, 100, 100 };
		public Body body;
		private boolean goRight = false;
		private boolean goLeft = false;
		private int life = 100;
		public int speed = 10;
		public int jumpert = 100;
		public AnimatedSprite aSprite;

		private boolean isDead = false;

		public ITiledTextureRegion texture;

		public Creature(ITiledTextureRegion ttr)
			{
				aSprite = new AnimatedSprite(0, 0, ttr.deepCopy(),
						MainGameEngineActivity.getSharedInstance()
								.getVertexBufferObjectManager());
				aSprite.animate(CREATURE_ANIMATE);
			}

		public void setAnimateFrames(long[] fn)
			{
				CREATURE_ANIMATE = fn;
			}

		public void setTexture(ITiledTextureRegion ttr)
			{
				this.texture = ttr;
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
				body.setLinearVelocity(new Vector2(-speed, body
						.getLinearVelocity().y));// with

				goRight = true;
				goLeft = false;

				aSprite.setFlippedHorizontal(false);
				//final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };

			}

		public void runLeft()
			{
				body.setLinearVelocity(new Vector2(speed, body
						.getLinearVelocity().y));// with

				goRight = false;
				goLeft = true;
				aSprite.setFlippedHorizontal(true);
			//	final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
				// animate(ENEMY_ANIMATE, 0, 2, true);

			}
		
		
		

		public void animateMe()
			{
				//final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
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

				body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,
						10));

				// body.setLinearVelocity(new
				// Vector2(body.getLinearVelocity().x, 10));
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

	}
