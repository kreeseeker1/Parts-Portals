package com.tesc.sos2014.scenes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.text.format.DateFormat;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.managers.SceneManager;
import com.tesc.sos2014.managers.SceneManager.SceneType;
import com.tesc.sos2014.objects.Health;
import com.tesc.sos2014.objects.Monkey;

import com.tesc.sos2014.objects.Player;

public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	long newTime = System.nanoTime();
	long oldTime;

	private int life = 1000;

	private HUD gameHUD;
	private Text scoreText;
	private PhysicsWorld physicsWorld;
	

	// private LevelCompleteWindow levelCompleteWindow;

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

	private Player player;
	private Monkey enemy;
	private Health  health;
	
	private Sprite bullet;
	private Body bulletBody =null;
	private FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

	private Text gameOverText;
	private boolean gameOverDisplayed = false;

	private boolean firstTouch = false;
	private boolean playerIsDead = false;

	private boolean goingLeft = true, goingRight = false;

	@Override
	public void createScene()
	{
		createBackground();
		createHUD();
		//fire();
		createPhysics();
		loadLevel(1);
		createGameOverText();

		// levelCompleteWindow = new LevelCompleteWindow(vbom);

		setOnSceneTouchListener(this);
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void fire(IEntity entity)
	{

		//Body[] ba = new Body[this.physicsWorld.getBodyCount() ];
		
		/*Vector<Body> ba = new Vector();
		
		while(this.physicsWorld.getBodies().hasNext())
		{
			
			ba.add(this.physicsWorld.getBodies().next());
		}*/
		
		
		//this.physicsWorld.getBodies().equals(bullet);
		
		Vector <Sprite> bul = new Vector(100);
		
		for(int i = 0; i<= bul.size();i ++)
		{
		int lifespan = 100;
		float x = entity.getX();
		float y = entity.getY();
	
		bullet = new Sprite(x,y,ResourcesManager.getInstance().bullet.deepCopy(),vbom);

		bulletBody = PhysicsFactory.createCircleBody(this.physicsWorld, bullet, BodyType.DynamicBody, fd);
		this.physicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet,bulletBody,true,true));
		final Vector2 speed = Vector2Pool.obtain(50,0);
		
		bulletBody.setLinearVelocity(speed);
		
		bul.add(bullet);
		}
		
		this.attachChild(bullet);
		
		/*for(int i =0; i<= ba.size() ; i++)
		{
			Log.d("Bodies", "Bodies" + ba.get(i).toString());
		}*/
			

		
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
		camera.setChaseEntity(null); // TODO
		camera.setCenter(400, 240);

		// TODO code responsible for disposing scene
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
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
			{
				public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
				{
					final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
					final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

					camera.setBounds(0, 0, width, height); // here we set camera
															// bounds
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

					/*if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
					{
						levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
						PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
					}*/

					 if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATL))
					{
						levelObject = new Sprite(x, y, resourcesManager.platformleft, vbom);
						PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformleft");
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATM))
					{
						levelObject = new Sprite(x, y, resourcesManager.platformmiddle, vbom);
						PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformmiddle");
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATR))
					{
						levelObject = new Sprite(x, y, resourcesManager.platformright, vbom);
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
						
						health = new Health(x, y, vbom, camera, physicsWorld)
						{
							
							@Override
							protected void onManagedUpdate(float pSecondsElapsed)
							{
								super.onManagedUpdate(pSecondsElapsed);
								this.animateMe();
								
								
								
								if(player.collidesWith(this) || this.collidesWith(player))
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
									//this.getParent().detachChild(this);
									
									Log.d("ChildCount :" + childCount, "Total Number of children");
									
									
									}
								
							}

							

							@Override
							public void onDie()
							{
								// TODO Auto-generated method stub
								
							}
						
						};
						
						
						levelObject = health; 
							//this codes causes auto-scaling of the object
						//levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
					} 
					
					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
					{
						player = new Player(x, y, vbom, camera, physicsWorld)
							{

								@Override
								public void onDie()
								{
									if (!gameOverDisplayed)
									{
										displayGameOverText();
									}
								}
							};
						levelObject = player;
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY))
					{
						enemy = new Monkey(x, y, vbom, camera, physicsWorld)
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
									
									if(bullet.collidesWith(this))
									{
										this.takeDamage(-50);
										if(this.getLife() <= 0)
										{
											this.onDie();
											this.setVisible(false);
											this.setIgnoreUpdate(true);
											this.squish();
											
										}
									}

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
										// levelCompleteWindow.display(StarsCount.TWO,
										// GameScene.this, camera);
										// this.setVisible(false);
										// this.setIgnoreUpdate(true);
										// player.onDie();
									}
								}

								private void setsp()
								{
									// TODO Auto-generated method stub

								}

								private double getDiff(long oldTime, long newTime)
								{
									return newTime - oldTime;
								}

								@Override
								public void onDie()
								{
									// TODO Auto-generated method stub

								}
							};
						enemy.setSize(75, 50);
						levelObject = enemy;
					} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
					{
						levelObject = new Sprite(x, y, resourcesManager.complete_stars_region, vbom)
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
		gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
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

		scoreText = new Text(20, 420, resourcesManager.font, "Life: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		scoreText.setAnchorCenter(0, 0);
		scoreText.setText("Score: 100");

		final Rectangle left = new Rectangle(20, 90, 60, 60, vbom)
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						player.runLeft();
						//fire(player);
						
					}

					return true;
				};
			};

		final Rectangle right = new Rectangle(camera.getWidth() - 60, 90, 60, 60, vbom)
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
			
			final Rectangle fire = new Rectangle(camera.getWidth() - 60, 180, 60, 60, vbom)
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						fire(player);
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