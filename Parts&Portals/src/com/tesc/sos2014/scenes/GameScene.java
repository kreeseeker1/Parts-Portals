package com.tesc.sos2014.scenes;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.managers.SceneManager;
import com.tesc.sos2014.managers.SceneManager.SceneType;
import com.tesc.sos2014.objects.Bullet;
import com.tesc.sos2014.objects.DemiEnemy;
import com.tesc.sos2014.objects.Health;
import com.tesc.sos2014.objects.Monkey;
import com.tesc.sos2014.objects.Player;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.pools.BulletPool;
import com.tesc.sos2014.pools.DemiEnemyPool;

public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	long newTime = System.nanoTime();
	long oldTime;
	private int life = 1000;
	private HUD gameHUD;
	private Text scoreText;
	public PhysicsWorld physicsWorld;

	private AnalogOnScreenControl stick;

	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATL = "platformleft";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATM = "platformmiddle";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATR = "platformright";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEALTH = "health";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY = "enemy";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";

	private static GameScene instance;
	
	private Player player;
	private DemiEnemy DE;
	private Monkey enemy;
	private Health health;
	public LinkedList<Bullet> bulletList = new LinkedList<Bullet>();
	public LinkedList<DemiEnemy> DEList ;
	//public LinkedList<DemiEnemy>demiEnemyList = new LinkedList();
	
	
	
	public int bulletCount = 0;
	private Text gameOverText;

	private boolean gameOverDisplayed = false;
	private boolean firstTouch = false;
	private boolean playerIsDead = false;
	private boolean goingLeft = true, goingRight = false;
	public int demiEnemyCount = 0;
	

	public GameScene()
		{
			engine.registerUpdateHandler(new com.tesc.sos2014.managers.GameLoopUpdateHandler());
		}

	@Override
	public void createScene()
	{
		//BulletPool.sharedBulletPool().batchAllocatePoolItems(50);
		Log.v("CreateScene", "CreateScene Started");
		DEList = new LinkedList<DemiEnemy>();
		//Log.v("CreateScene", "dDemi Enemy List Size" + demiEnemyList.size());
		createBackground();
		createHUD();
		createPhysics();
		loadLevel(1);
		createGameOverText();
		setOnSceneTouchListener(this);
		//demiEnemyList.add(new DemiEnemy());
	}

	public void attachBullet(Bullet b)
	{
		Log.v("Bullet Items Available", "Bullet Count" + BulletPool.instance.getAvailableItemCount());
		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		if (player.isFacingLeft())
		{
			b.sprite = new Sprite(player.getX() - 25, player.getY(), ResourcesManager.getInstance().bullet.deepCopy(), getVbom());// .setUserData(ResourcesManager.getInstance().bullet);
		} else if (player.isFacingRight())
		{
			b.sprite = new Sprite(player.getX() + 25, player.getY(), ResourcesManager.getInstance().bullet.deepCopy(), getVbom());// .setUserData(ResourcesManager.getInstance().bullet);
		}

		b.setOldX(b.getNewX());
		b.setNewX(b.sprite.getX());
		
		b.bulletBody = PhysicsFactory.createCircleBody(physicsWorld, b.sprite, BodyType.DynamicBody, fd);
		b.bulletBody.setActive(true);
		b.bulletLife = 100;

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(b.sprite, b.bulletBody, true, true));

		if (player.isFacingLeft())
		{
			b.bulletBody.setLinearVelocity(50f, b.bulletBody.getLinearVelocity().y);
		} else if (player.isFacingRight())
		{
			b.bulletBody.setLinearVelocity(-50f, b.bulletBody.getLinearVelocity().y);
		}

		b.bulletBody.setUserData(b.sprite);
		b.bulletBody.setActive(true);

		Log.v("Bullet Pos", "PLayerX: " + player.getX());
		Log.v("Bullet Pos", "BulletX: " + b.sprite.getX());

		b.sprite.setSize(10f, 10f);
		this.attachChild(b.sprite);
	}
	
	public void attachDemiEnemy(DemiEnemy de)
	{
		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		de.body = PhysicsFactory.createCircleBody(physicsWorld, de.aSprite, BodyType.DynamicBody, fd);
		de.body.setActive(true);
		
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(de.aSprite, de.body, true, false));
		
		de.body.setUserData(de.aSprite);
		
		de.aSprite.setSize(75, 50);
		de.animateMe();
		
		
		
		
		
		
		//b.bulletLife = 100;

		/*physicsWorld.registerPhysicsConnector(new PhysicsConnector(de.aSprite, de.body, true, false)
		{
			
			
			
		}*/
			/*@Override
			public void onUpdate(float pSecondsElapsed)
			{

				de.aSprite.setUserData(ResourcesManager.getInstance().enemy.deepCopy());
				super.onUpdate(pSecondsElapsed);// This is very important to
												// be in this exact spot
				// camera.onUpdate(0.1f);

				if (de.aSprite.getY() <= 0) // Body falls below bottom of scene
				{
					//de.onDie();
					de.squish();
				}

				if (de.isGoRight())
				{
					// super.onUpdate(pSecondsElapsed);
					de.body.setLinearVelocity(new Vector2(3, de.body.getLinearVelocity().y));
																							
				}
				if (de.isGoLeft())
				{
					// super.onUpdate(pSecondsElapsed);
					de.body.setLinearVelocity(new Vector2(-3, de.body.getLinearVelocity().y));
				}
			}
		});*/
		//GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
		//Log.v("Scene Validity" , scene.toString());
		try
		{
		DEList.add(DE);
		//this.DEList.add(de);
		Log.v("DEMI ENEMY LIST COUNT", "#" + DEList.size());
		this.attachChild(de.aSprite);
		}
		catch(NullPointerException jnpe)
		{
			Log.v("NPE" , jnpe.toString());
		}
		
		
}
		


	public void cleaner()
	{
		synchronized (this)
		{
			Iterator<Bullet> it = bulletList.iterator();
			Iterator<DemiEnemy> dl = DEList.iterator();
			while (it.hasNext())
			{
				Bullet b = (Bullet) it.next();
				
			
				
				
				while(dl.hasNext())
				{
					DemiEnemy d = (DemiEnemy) dl.next();
					if(d.aSprite.collidesWith(b.sprite) || player.collidesWith(d.aSprite) || b.sprite.collidesWith(d.aSprite))
					{
						Log.v("SQUISH", "Squish Activated");
						d.squish();		
						addToScore(-1);
						}
					else
					{
						Log.v("NO SQUISH" , "Squish Not Called");
					}
					
					
				}
				
				Log.v("NO SQUISH" , "Squish Not Called EVER in main loop " + 	DEList.size());
				b.setNewX(b.sprite.getX());

				if (b.bulletLife <= 0 || b.sprite.getY() <= -b.sprite.getHeight() || !camera.isEntityVisible(b.sprite) ||(b.getOldX() == b.getNewX()) )																																																			// )
				{
					Log.v("Cleaner", "Bullet Removed.");
					Log.v("Children", "Number of Children" + this.getChildCount());
					
					BulletPool.sharedBulletPool().recyclePoolItem(b);
					it.remove();
					continue;
				} else
				{
					b.bulletLife--;
					b.setOldX(b.getNewX());
					b.setNewX(b.sprite.getX());
				}
			}
		}
	}

	public static GameScene getSharedInstance()
	{
		return instance;
	}

	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene()
	{
		camera.setHUD(null);
		camera.setChaseEntity(null);
		camera.setCenter(400, 240);
		

		// code responsible for disposing scene
		// removing all game scene objects.
	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (pSceneTouchEvent.isActionDown())
		{
			if (pSceneTouchEvent.getX() > player.getX())
			{
				player.runRight();
				player.setFlippedHorizontal(false);
				firstTouch = true;
			} else
			{
				player.runLeft();
				player.setFlippedHorizontal(true);
			}
			if (pSceneTouchEvent.getY() > player.getY())
			{
				player.jump();
			}
		}
		return false;
	}

	private void loadLevel(int levelID)
	{
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(getVbom());

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
			{
				public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
				{
					final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
					final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

					camera.setBounds(0, 0, width, height); // here we set camera bounds
					camera.setBoundsEnabled(true);

					return GameScene.this;
				}
			});

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
			{
				public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
				{
					final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
					final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);

					final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

					final Sprite levelObject;

					
					if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATL))
					{
						levelObject = new Sprite(x, y, resourcesManager.platformleft, getVbom());
						PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformleft");
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATM))
					{
						levelObject = new Sprite(x, y, resourcesManager.platformmiddle, getVbom());
						PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformmiddle");
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATR))
					{
						levelObject = new Sprite(x, y, resourcesManager.platformright, getVbom());
						PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformright");
					}
					/*
					 * else if
					 * (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
					 * { levelObject = new Sprite(x, y,
					 * resourcesManager.platform2_region, vbom); final Body body
					 * = PhysicsFactory.createBoxBody(physicsWorld, levelObject,
					 * BodyType.StaticBody, FIXTURE_DEF);
					 * body.setUserData("platform2");
					 * physicsWorld.registerPhysicsConnector(new
					 * PhysicsConnector(levelObject, body, true, false)); } else
					 * if
					 * (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
					 * { levelObject = new Sprite(x, y,
					 * resourcesManager.platform3_region, vbom); final Body body
					 * = PhysicsFactory.createBoxBody(physicsWorld, levelObject,
					 * BodyType.StaticBody, FIXTURE_DEF);
					 * body.setUserData("platform3");
					 * physicsWorld.registerPhysicsConnector(new
					 * PhysicsConnector(levelObject, body, true, false)); }
					 */
					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEALTH))
					{

						health = new Health(x, y, getVbom(), camera, physicsWorld)
							{

								@Override
								protected void onManagedUpdate(float pSecondsElapsed)
								{
									super.onManagedUpdate(pSecondsElapsed);
									this.animateMe();

									if (player.collidesWith(this) || this.collidesWith(player))
									{
										this.setWidth(0);
										this.setHeight(0);
										addToScore(100);
										this.setVisible(false);
										this.squish();

										this.setIgnoreUpdate(true);
										this.clearEntityModifiers();
										this.clearUpdateHandlers();

										int childCount = this.getParent().getChildCount();

										Log.d("ChildCount :" + childCount, "Total Number of children");

									}

								}

								@Override
								public void onDie()
								{

								}

							};

						levelObject = health;

						// following codes causes auto-scaling loop of the
						// object
						// levelObject.registerEntityModifier(new
						// LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
					{
						player = new Player(x, y, getVbom(), camera, physicsWorld)
							{

								@Override
								public void onDie()
								{
									if (!gameOverDisplayed)
									{
										displayGameOverText();
									}
									this.body.setActive(false);// removes body
									this.setPosition(2000, 1000);// causes
																	// enemies
																	// to run to
																	// different
																	// part of
																	// screen
									// this.detachSelf(); //This works I think
									// by setting your x,y to 0,0
								}
							};
						MassData data = new MassData();
						data.mass = 2000f;

						player.body.setMassData(data);
						levelObject = player;
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY))
					{
						
						//Log.v("Demi Enemy Added " , "Creating DemiEnemy");
							DE = DemiEnemyPool.sharedDemiEnemyPool().obtainPoolItem();
						//	Log.v("Demi Enemy Added 2" , "Creating DemiEnemy");
						
							DE.aSprite.setPosition(x ,y);
							//Log.v("Demi Enemy Added 3" , "Creating DemiEnemy");
							DE.aSprite.setVisible(true);
							//Log.v("Demi Enemy Added 4" , "Creating DemiEnemy");
							DE.aSprite.detachSelf();
							Log.v("Demi Enemy Added 5" , "Creating DemiEnemy");
							DE.aSprite.setSize(40, 40);
							DE.animateMe();
							Log.v("DEList" , "Demi Enemy Count Size: " + this.toString());
					        Log.v("Demi Enemy Add " , "Demi Enemy Count Size: " + demiEnemyCount);
							demiEnemyCount ++;
							
							attachDemiEnemy(DE);
							//Log.v("Demi Enemy Add " , "Demi Enemy List Size: " + demiEnemyList.size());
							//demiEnemyList.add(DE);
							//GameScene.instance.attachDemiEnemy(DE);	
							
						//}
						//DE = DemiEnemyPool.sharedBulletPool().obtainPoolItem();
						
						/*//enemy = new Monkey(x, y, getVbom(), camera, physicsWorld)
							{
								@Override
								protected void onManagedUpdate(float pSecondsElapsed)
								{
									super.onManagedUpdate(pSecondsElapsed);

									this.setSpeed((int) (Math.random() * 15));

									this.animateMe();

									// time delta
									oldTime = newTime;
									newTime = System.nanoTime();

									// float lastPos = this.getY();
									if (getDiff(oldTime, newTime) > 1000000 && player.getY() > this.getY() + 5)
									{
										this.setFootContactsOne();
										this.jump();
										// this.setFootContactsZero();
									}

									// According to tutorial this following code
									// should be checked in the cleaner method
									
									 * if(bullet.collidesWith(this)) {
									 * this.takeDamage(-50); if(this.getLife()
									 * <= 0) { this.onDie();
									 * this.setVisible(false);
									 * this.setIgnoreUpdate(true);
									 * this.squish();
									 * 
									 * } }
									 

									if (life <= 0)
									{

										player.onDie();
										player.setVisible(false);
										player.setIgnoreUpdate(true);
										player.onDetached();
										playerIsDead = true;
									}
									if (player.getX() < this.getX() - Math.random() * 10)
									{

										this.setFlippedHorizontal(false);
										this.runLeft();
										goingLeft = true;
										goingRight = false;

									} else if (player.getX() > this.getX() + Math.random() * 10)
									{

										this.setFlippedHorizontal(true);
										this.runRight();
										goingRight = true;
										goingLeft = false;

									}
									if (player.collidesWith(this) || this.collidesWith(player))
									{
										addToScore(-1);

									}

									Iterator<Bullet> it2 = bulletList.iterator();

									if (it2 != null)
									{

										while (it2.hasNext())
										{
											Bullet b2 = (Bullet) it2.next();
											if (this.collidesWith(b2.sprite))
											{
												this.onDie();
											}
										}
									}

								}

								private double getDiff(long oldTime, long newTime)
								{
									return newTime - oldTime;
								}

								@Override
								public void onDie()
								{
									this.setVisible(false);
									this.squish();

								}
							};*/
				/*		enemy.setSize(75, 50);
						enemy.setSpeed((int) (Math.random() * 15));*/
							DemiEnemy de2 = new DemiEnemy();
							
							de2.aSprite.setUserData(ResourcesManager.getInstance().bullet);
							
						levelObject = de2.aSprite;
					} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
					{
						levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, getVbom())
							{
								@Override
								protected void onManagedUpdate(float pSecondsElapsed)
								{
									super.onManagedUpdate(pSecondsElapsed);

									if (player.collidesWith(this))
									{
										// levelCompleteWindow.display(StarsCount.TWO,
										// GameScene.this, camera);
										// this.setVisible(false);
										// this.setIgnoreUpdate(true);
									}
								}
							};
						levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
					} else
					{
						throw new IllegalArgumentException();
					}

					levelObject.setCullingEnabled(true);

					return levelObject;
				}
			});

		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
	}

	

	private void createGameOverText()
	{
		gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", getVbom());
	}

	private void displayGameOverText()
	{
		camera.setChaseEntity(null);
		gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
	}

	private void createHUD()
	{
		gameHUD = new HUD();

		scoreText = new Text(20, 420, resourcesManager.font, "Life: 0123456789", new TextOptions(HorizontalAlign.LEFT), getVbom());
		scoreText.setAnchorCenter(0, 0);
		scoreText.setText("Score: 100");

		stick = new AnalogOnScreenControl(75f, camera.getHeight() - 300, camera, ResourcesManager.getInstance().control_base_region.deepCopy(), ResourcesManager.getInstance().bullet.deepCopy(), .1f, getVbom(), new IAnalogOnScreenControlListener()
			{

				@Override
				public void onControlChange(BaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY)
				{
					if (pValueX == -1)
					{
						player.runLeft();
					}
					if (pValueX == 1)
					{
						player.runRight();
					} else
					{
						player.stop();
					}

				}

				@Override
				public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl)
				{
					// player.shoot();

				}

			});

		stick.getControlBase().setAlpha(0.5f);
		stick.getControlKnob().setAlpha(0.5f);

		final Rectangle left = new Rectangle(20, 90, 60, 60, getVbom())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						player.runLeft();
						// fire(player);

					}

					return true;
				};
			};

		final Rectangle right = new Rectangle(camera.getWidth() - 60, 90, 60, 60, getVbom())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						player.runRight();
					}
					/*
					 * else { player.stop(); }
					 */
					return true;
				};
			};

		final Rectangle fire = new Rectangle(camera.getWidth() - 60, 180, 60, 60, getVbom())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						synchronized (this)
						{
							player.shoot();
						}

					}
					/*
					 * else { player.stop(); }
					 */
					return true;
				};
			};

		gameHUD.registerTouchArea(left);
		gameHUD.registerTouchArea(right);
		gameHUD.registerTouchArea(fire);

		// gameHUD.registerTouchArea(stick);
		// gameHUD.attachChild(stick);

		gameHUD.attachChild(left);
		gameHUD.attachChild(right);
		gameHUD.attachChild(fire);

		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		gameHUD.attachChild(scoreText);

		camera.setHUD(gameHUD);
	}

	private void createBackground()
	{
		setBackground(new Background(Color.BLACK));
	}

	private void addToScore(int i)
	{
		if (playerIsDead)
		{
			life = 0;
			scoreText.setText("Life: " + life + "hp");
		} else
		{
			life += i;
			scoreText.setText("Life: " + life + "hp");
		}
	}

	private void createPhysics()
	{
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}

	public PhysicsWorld getPhysicsWorld()
	{
		return this.physicsWorld;
	}

	/*
	 * public static void setPhysicsWorld(PhysicsWorld physicsWorld) {
	 * GameScene.physicsWorld = physicsWorld; }
	 */

	// ---------------------------------------------
	// INTERNAL CLASSES
	// ---------------------------------------------

	private ContactListener contactListener()
	{
		ContactListener contactListener = new ContactListener()
			{
				public void beginContact(Contact contact)
				{
					final Fixture x1 = contact.getFixtureA();
					final Fixture x2 = contact.getFixtureB();
					// error checking for non-existent objects
					if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
					{
						if (x2.getBody().getUserData().equals("player"))
						{// player
							player.increaseFootContacts();
						}
						/*
						 * if (x2.getBody().getUserData().equals("enemy")) {
						 * //enemy
						 * 
						 * enemy.increaseFootContacts(); }
						 */

						if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player"))
						{
							engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
								{
									public void onTimePassed(final TimerHandler pTimerHandler)
									{
										pTimerHandler.reset();
										engine.unregisterUpdateHandler(pTimerHandler);
										x1.getBody().setType(BodyType.DynamicBody);
									}
								}));
						}

						if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("player"))
						{
							x1.getBody().setType(BodyType.DynamicBody);
						}
					}
				}

				public void endContact(Contact contact)
				{
					final Fixture x1 = contact.getFixtureA();
					final Fixture x2 = contact.getFixtureB();

					if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
					{
						if (x2.getBody().getUserData().equals("player"))
						{
							player.decreaseFootContacts();
						}

						/*
						 * if (x2.getBody().getUserData().equals("enemy")) {
						 * enemy.decreaseFootContacts(); }
						 */
					}
				}

				public void preSolve(Contact contact, Manifold oldManifold)
				{

				}

				public void postSolve(Contact contact, ContactImpulse impulse)
				{

				}
			};
		return contactListener;
	}
}