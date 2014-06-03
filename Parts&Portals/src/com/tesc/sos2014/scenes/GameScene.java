package com.tesc.sos2014.scenes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import com.badlogic.gdx.physics.box2d.Body;
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
import com.tesc.sos2014.objectenemies.FeraalkEnemy;
import com.tesc.sos2014.objects.Bullet;
import com.tesc.sos2014.objects.Health;
import com.tesc.sos2014.objects.Player;
import com.tesc.sos2014.pools.BulletPool;
import com.tesc.sos2014.pools.FeraalkEnemyPool;
import com.tesc.sos2014.utilities.Entity;
import com.tesc.sos2014.utilities.ParsePngFile;

public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	long newTime = System.nanoTime();
	long oldTime;
	private int life = 1000;
	private HUD gameHUD;
	private Text healthText;
	public Text fuelText;
	public Text scoreText;
	
	public Text xText;
	public Text yText;
	//public Text scoreText;
	public PhysicsWorld physicsWorld;
	
	public int score = 0;

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
	private int playerIndex =0;
	private FeraalkEnemy DE;
	
	private Health health;
	public LinkedList<Bullet> bulletList ;
	public ArrayList<FeraalkEnemy> feraalkList;
	

	//Entity Lists
	public List<Entity> eList;
	public List<Entity> floorList;
	public List<Entity> wallList;
	public List<Entity> itemList;
	public List<Entity> blackList;
	
	//Sprite Lists
	public List<Sprite> floorSpriteList;
	public List<Sprite> wallSpriteList;
	public List<Sprite> itemSpriteList;
	
	public int bulletCount = 0;
	private Text gameOverText;
	
	private boolean fire = true, noFire = false;

	private boolean gameOverDisplayed = false;
	private boolean firstTouch = false;
	private boolean playerIsDead = false;
	private boolean goingLeft = true, goingRight = false;
	public int demiEnemyCount = 0;
	public int mapNum = 1;
	

//	ParsePngFile ppf;
	
	public List<Entity> GDObjects;
	
	//GDObjects = PNGParser.ParseMap("Level1.png");

	public GameScene()
		{
			engine.registerUpdateHandler(new com.tesc.sos2014.managers.GameLoopUpdateHandler());
		}

	@Override
	public void createScene()
	{
		
		
		Log.v("CreateScene", "CreateScene Started");
		
		bulletList = new LinkedList<Bullet>();//These two LinkedList Declarations MUST be called right here.
		
		itemList = new ArrayList<Entity>();
		floorList = new ArrayList<Entity>();
		wallList = new ArrayList<Entity>();
		blackList = new ArrayList<Entity>();
		
		feraalkList = new ArrayList<FeraalkEnemy>();
		
		floorSpriteList = new ArrayList<Sprite>();
		wallSpriteList = new ArrayList<Sprite>();
		itemSpriteList = new ArrayList<Sprite>();
		
		createBackground();
		createHUD();
		createPhysics();
		ParsePngFile.parsePNGFile();
		
		eList = ParsePngFile.getEntities();
		//setData();
		splitList();
		GenerateMap();
		//loadLevel(1);
	//	createGameOverText();
		
		
		
		setOnSceneTouchListener(this);
		
	}

	private void splitList()
	{
		
		//Red:    255,0,0 	  (Item)
		//Green:  0,255,0 	  (Floor)
		//Blue:   0,0,255 	  (Wall)
		//Yellow: 255,255,0   (Item)
		//Orange: 255,165,0   (Item)
		//Purple: 128,0,128   (Item)
		//White:  255,255,255 (Item)
		//Black:  0,0,0       (EmptySpace)
		
		
		for(int i = 0; i<= eList.size()-1; i++)
		{
			if(eList.get(i).getColor().getRed() > 0 && eList.get(i).getColor().getGreen() == 0 && eList.get(i).getColor().getBlue() ==0 )
			{
				//Red
				itemList.add(eList.get(i));
			}
			else if(eList.get(i).getColor().getRed() == 0 && eList.get(i).getColor().getGreen() > 0 && eList.get(i).getColor().getBlue() ==0)
			{
				//Green
				floorList.add(eList.get(i));
			}
			else if(eList.get(i).getColor().getRed() ==0  && eList.get(i).getColor().getGreen() == 0 && eList.get(i).getColor().getBlue() > 0)
			{
				//Blue
				wallList.add(eList.get(i));
			}
			else if(eList.get(i).getColor().getRed() == 255 && eList.get(i).getColor().getGreen() == 255 && eList.get(i).getColor().getBlue() ==0)
			{
				//Yellow
				Log.v("itemList","Contents of eList.Get(" + i + "): " + eList.get(i));
				itemList.add(eList.get(i));
			}
			else if(eList.get(i).getColor().getRed() ==255 && eList.get(i).getColor().getGreen() == 165 && eList.get(i).getColor().getBlue() ==0)
			{
				//Orange
				itemList.add(eList.get(i));
			}
			else if(eList.get(i).getColor().getRed() > 0 && eList.get(i).getColor().getGreen() == 0 && eList.get(i).getColor().getBlue() ==0)
			{
				//Purple
				itemList.add(eList.get(i));
			}
			else if(eList.get(i).getColor().getRed() == 255 && eList.get(i).getColor().getGreen() == 255 && eList.get(i).getColor().getBlue() ==255)
			{
				//White
				//itemList.add(eList.get(i));
				playerIndex = i;
				
				
			}
			else if(eList.get(i).getColor().getRed() == 0 && eList.get(i).getColor().getGreen() == 0 && eList.get(i).getColor().getBlue() ==0)
			{
				//Black
				blackList.add(eList.get(i));
			}
		}

		
		
	}

	private void GenerateMap()
	{
		for(int i = 0; i<= floorList.size()-1;i++)
		{
			final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
		Sprite s = new Sprite(floorList.get(i).getCoordinate().getX()*50, floorList.get(i).getCoordinate().getY()*50, ResourcesManager.getInstance().platformmiddle.deepCopy(), getVbom());	
		PhysicsFactory.createBoxBody(physicsWorld, s, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformmiddle");
		
		floorSpriteList.add(s);
		
		this.attachChild(s);
		}
		
		for(int i =0; i<= wallList.size()-1;i++)
		{
			final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
			Sprite s = new Sprite(wallList.get(i).getCoordinate().getX()*50, wallList.get(i).getCoordinate().getY()*50, ResourcesManager.getInstance().platformmiddle.deepCopy(), getVbom());	
			PhysicsFactory.createBoxBody(physicsWorld, s, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformmiddle");
			this.attachChild(s);
			
			wallSpriteList.add(s);
		}
		
		for(int i=0; i<= itemList.size()-1;i++)
		{
			final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
			Sprite s = new Sprite(itemList.get(i).getCoordinate().getX()*50, itemList.get(i).getCoordinate().getY()*50, ResourcesManager.getInstance().platformmiddle.deepCopy(), getVbom());	
			PhysicsFactory.createBoxBody(physicsWorld, s, BodyType.StaticBody, FIXTURE_DEF).setUserData("platformmiddle");
			this.attachChild(s);
			
			itemSpriteList.add(s);
		}
		
		
		
		player = new Player((float)eList.get(playerIndex).getCoordinate().getX()*50,(float) eList.get(playerIndex).getCoordinate().getY()*50 , getVbom(), camera, physicsWorld)
		{
			
		};
		
		/*player= new Player(0,0,getVbom(),camera,physicsWorld)
			{
				
			};*/
		//Log.v("","");
		Log.v("Coordinate X","" + eList.get(playerIndex).getCoordinate().getX());
		Log.v("Coordinate Y","" + eList.get(playerIndex).getCoordinate().getY());
		camera.setCenter(player.getX() , player.getY());
		Log.v("Camera X","" + camera.getCenterX());
		Log.v("Camera Y","" + camera.getCenterY());
		//MassData data = new MassData();
		//data.mass = 200f;

		//player.body.setMassData(data);
		
		this.attachChild(player);
		camera.setChaseEntity(player);
		Log.v("Player X","" + player.getX());
		Log.v("Player Y","" + player.getY());
		
		//camera.setCenter(player.getX(), player.getY());
		
	}

	private void setData()
	{
        ParsePngFile.parsePNGFile();
		
		eList = ParsePngFile.getEntities();
		
	}

	// ==========================================================================================================================================
	// ATTACH BLOCK START
	// ==========================================================================================================================================

	public void attachBullet(Bullet b)
	{
		//////////////////////////////////////////////////////////////////////////////////////////////
		Log.v("Bullet Items Available", "Bullet Count" + BulletPool.instance.getAvailableItemCount());
		Log.v("Bullet Nums", "Num of Bullets" + BulletPool.instance.getUnrecycledItemCount());
		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		/////////////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//========================================================================================================================================
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (player.isFacingLeft())
		{
			b.sprite = new Sprite(player.getX() - 25, player.getY(), ResourcesManager.getInstance().bullet.deepCopy(), getVbom());// .setUserData(ResourcesManager.getInstance().bullet);
		}
		else if (player.isFacingRight())
		{
			b.sprite = new Sprite(player.getX() + 25, player.getY(), ResourcesManager.getInstance().bullet.deepCopy(), getVbom());// .setUserData(ResourcesManager.getInstance().bullet);
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//=========================================================================================================================================
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////////////////////////////
		b.sprite.setSize(20, 20);
		b.setOldX(b.getNewX());
		b.setNewX(b.sprite.getX());
		b.bulletBody = PhysicsFactory.createCircleBody(physicsWorld, b.sprite, BodyType.DynamicBody, fd);
		b.bulletBody.setActive(true);
		b.bulletLife = 100;
		b.bulletBody.setUserData(b.sprite);
		b.bulletBody.setActive(true);
		/////////////////////////////////////////////////////////////////////////////////////////////////

		
		//=======================================================================================================
		///////////////////////////////////////////////////////////////////////////////////////////////////////==
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(b.sprite, b.bulletBody, true, true));///////==
		///////////////////////////////////////////////////////////////////////////////////////////////////////==
		//=======================================================================================================

		////////////////////////////////////////////////////////////////////////////
		if (player.isFacingLeft())
		{
			b.bulletBody.setLinearVelocity(50f, b.bulletBody.getLinearVelocity().y);
		} else if (player.isFacingRight())
		{
			b.bulletBody.setLinearVelocity(-50f, b.bulletBody.getLinearVelocity().y);
		}
		/////////////////////////////////////////////////////////////////////////////
		

		Log.v("Bullet Pos", "PLayerX: " + player.getX());
		Log.v("Bullet Pos", "BulletX: " + b.sprite.getX());

		this.attachChild(b.sprite);
	}

	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void attachFeraalkEnemy(FeraalkEnemy de)
	{
		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		de.body = PhysicsFactory.createCircleBody(physicsWorld, de.aSprite, BodyType.DynamicBody, fd);
		de.body.setActive(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(de.aSprite, de.body, true, false));

		de.body.setUserData(de.aSprite);

		de.aSprite.setSize(75, 50);
		de.animateMe();

		try
		{
			feraalkList.add(DE);
			// this.DEList.add(de);
			Log.v("DEMI ENEMY LIST COUNT", "#" + feraalkList.size());
			this.attachChild(de.aSprite);
		} catch (NullPointerException jnpe)
		{
			Log.v("NPE", jnpe.toString());
		}

	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//======================================================================================================
	public void attachWall(Sprite s)
	{

		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		Body b;

		b = PhysicsFactory.createBoxBody(physicsWorld, s, BodyType.StaticBody, fd);
		b.setActive(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(s, b, true, false));

		// ?????? Future make a LinkedList for walls ???????????????????
		// possibly use a linked list of type sprite. May not need a wall object

	}
	//======================================================================================================
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//====================================================================================
	public void attachFloor(Sprite s)
	{

		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		Body b;

		b = PhysicsFactory.createBoxBody(physicsWorld, s, BodyType.StaticBody, fd);
		b.setActive(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(s, b, true, false));

		// ?????? Future make a LinkedList for floors ???????????????????
		// possibly use a linked list of type sprite. May not need a floor object

	}
	//====================================================================================
	
	


	// ==========================================================================================================================================
	// ATTACH BLOCK END
	// ==========================================================================================================================================

	public void cleaner()
	{
		synchronized (this)
		{
			//Player
			xText.setText("X: " + player.getX());
			yText.setText("Y: " + player.getY());
			
			Log.v("PLayer X","" + player.getX());
			Log.v("Player Y","" + player.getY());
			
			Log.v("Camera X","" + camera.getCenterX());
			Log.v("Camera Y","" + camera.getCenterY());
			
			Iterator<Sprite> fsl = floorSpriteList.iterator();
			while(fsl.hasNext())
			{
				if(player.collidesWith(fsl.next()) && fsl.next().getY() / 50 < player.getY())
				{
					player.rechargeJets();
				}
				/*else if(player.collidesWith(fsl.next()) && fsl.next().getY() / 50 != player.getY())
				{
					Log.v("rechargeJets","player Y: " + player.getY() + " floor Y: " + fsl.next().getY() );
				}*/
			}
			
			
			
			//Enemies and Bullets
			Iterator<Bullet> it = bulletList.iterator();
			Iterator<FeraalkEnemy> dl = feraalkList.iterator();
			while (it.hasNext())
			{
				Bullet b = (Bullet) it.next();

				while (dl.hasNext())
				{
					FeraalkEnemy fe = (FeraalkEnemy) dl.next();
					if (fe.aSprite.collidesWith(b.sprite) || b.sprite.collidesWith(fe.aSprite))
					{
						Log.v("SQUISH", "Squish Activated. DEList size: " + feraalkList.size() + ". demiEnemyCount: " + demiEnemyCount);
						fe.squish();
						dl.remove();
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						addToScore(10);
						scoreText.setText("Score:" + score);
					}
					 if( fe.aSprite.getY() <= 0)
					{
						Log.v("SQUISH", "Squish Activated. DEList size: " + feraalkList.size() + ". demiEnemyCount: " + demiEnemyCount);
						fe.squish();
						dl.remove();
					}
					 if (player.collidesWith(fe.aSprite) || fe.aSprite.collidesWith(player))
					{
						addToLife(-1);
					}

				}

				// Log.v("NO SQUISH" , "Squish Not Called EVER in main loop " +
				// DEList.size());
				b.setNewX(b.sprite.getX());

				if (b.bulletLife <= 0 || b.sprite.getY() <= -b.sprite.getHeight() || !camera.isEntityVisible(b.sprite) || (b.getOldX() == b.getNewX())) // )
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
					continue;
				}
			}
			// ////////////////////////////////////Begin AI Loop
			// //////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			while (dl.hasNext())
			{
				FeraalkEnemy d = (FeraalkEnemy) dl.next();

				if (d.isDead())
				{

					d.aSprite.setVisible(false);
					FeraalkEnemyPool.sharedDemiEnemyPool().recyclePoolItem(d);
				} else if (!d.isDead())
				{
					if (d.aSprite.getX() - 230 < player.getX())
					{
						d.runLeft();
					} else if (d.aSprite.getX() + 230 > player.getX())
					{
						d.runRight();
					}
					if (d.jumpert <= 0 && player.getY() > d.aSprite.getY())
					{

						d.jump(); //
						d.jumpert = 100;
					} else if (d.jumpert > 0)
					{
						d.jumpert = d.jumpert - 10;
					}

				}
			}
			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ////////////////////////////////////////////////////////End AI
			// Loop
			// //////////////////////////////////////////////////////////////////////////////
		}
	}

	public static GameScene getSharedInstance()
	{
		return instance;
	}

	private double getDiff(long oldTime, long newTime)
	{
		return newTime - oldTime;
	}

	@Override
	public void onBackKeyPressed()
	{
		bulletList.clear();

		
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
		//camera.setChaseEntity(null);
		//camera.setCenter(400, 240);
		//Bullet Activity continues like normal after a GameScene -> Menu -> GameScene reset, but the sprites are not reinitialized.
		//bulletList.clear();

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
			
			
				
				player.setJumping(true);
			player.jps.setParticlesSpawnEnabled(true);
			//fuelText.setText("Fuel: " + player.getJumpTimer());
				
			
		}
		else if (pSceneTouchEvent.isActionUp())
		{
			
				
				player.setJumping(false);
				//player.setParticleSpawned(noFire);
				player.jps.setParticlesSpawnEnabled(false);
				
				
			
		}
		return false;
	}

	public Text getFuelText()
	{
		return fuelText;
	}

	public void setFuelText(Text fuelText)
	{
		this.fuelText.setText((CharSequence) fuelText)  ;
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
										addToLife(100);
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

								/*@Override
								public void onDie()
								{
									if (!gameOverDisplayed)
									{
										displayGameOverText();
									}
									this.body.setActive(false);// removes body
									this.setPosition(2000, 1000);// causes
*/																	// enemies
																	// to run to
																	// different
																	// part of
																	// screen
									// this.detachSelf(); //This works I think
									// by setting your x,y to 0,0
								//}
							};
						MassData data = new MassData();
						data.mass = 2000f;

						player.body.setMassData(data);
						levelObject = player;
					}

					else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY))
					{

						// Log.v("Demi Enemy Added " , "Creating DemiEnemy");
						DE = FeraalkEnemyPool.sharedDemiEnemyPool().obtainPoolItem();
						// Log.v("Demi Enemy Added 2" , "Creating DemiEnemy");

						DE.aSprite.setPosition(x, y);
						// Log.v("Demi Enemy Added 3" , "Creating DemiEnemy");
						DE.aSprite.setVisible(true);
						// Log.v("Demi Enemy Added 4" , "Creating DemiEnemy");
						DE.aSprite.detachSelf();
						Log.v("Demi Enemy Added 5", "Creating DemiEnemy");
						DE.aSprite.setSize(40, 40);
						DE.animateMe();
						Log.v("DEList", "Demi Enemy Count Size: " + this.toString());
						Log.v("Demi Enemy Add ", "Demi Enemy Count Size: " + demiEnemyCount);
						demiEnemyCount++;

						attachFeraalkEnemy(DE);
						// Log.v("Demi Enemy Add " , "Demi Enemy List Size: " +
						// demiEnemyList.size());
						// demiEnemyList.add(DE);
						// GameScene.instance.attachDemiEnemy(DE);

						// }
						// DE =
						// DemiEnemyPool.sharedBulletPool().obtainPoolItem();

						/*
						 * //enemy = new Monkey(x, y, getVbom(), camera,
						 * physicsWorld) {
						 * 
						 * @Override protected void onManagedUpdate(float
						 * pSecondsElapsed) {
						 * super.onManagedUpdate(pSecondsElapsed);
						 * 
						 * this.setSpeed((int) (Math.random() * 15));
						 * 
						 * this.animateMe();
						 * 
						 * // time delta oldTime = newTime; newTime =
						 * System.nanoTime();
						 * 
						 * // float lastPos = this.getY(); if (getDiff(oldTime,
						 * newTime) > 1000000 && player.getY() > this.getY() +
						 * 5) { this.setFootContactsOne(); this.jump(); //
						 * this.setFootContactsZero(); }
						 * 
						 * // According to tutorial this following code //
						 * should be checked in the cleaner method
						 * 
						 * if(bullet.collidesWith(this)) { this.takeDamage(-50);
						 * if(this.getLife() <= 0) { this.onDie();
						 * this.setVisible(false); this.setIgnoreUpdate(true);
						 * this.squish();
						 * 
						 * } }
						 * 
						 * 
						 * if (life <= 0) {
						 * 
						 * player.onDie(); player.setVisible(false);
						 * player.setIgnoreUpdate(true); player.onDetached();
						 * playerIsDead = true; } if (player.getX() <
						 * this.getX() - Math.random() * 10) {
						 * 
						 * this.setFlippedHorizontal(false); this.runLeft();
						 * goingLeft = true; goingRight = false;
						 * 
						 * } else if (player.getX() > this.getX() +
						 * Math.random() * 10) {
						 * 
						 * this.setFlippedHorizontal(true); this.runRight();
						 * goingRight = true; goingLeft = false;
						 * 
						 * } if (player.collidesWith(this) ||
						 * this.collidesWith(player)) { addToScore(-1);
						 * 
						 * }
						 * 
						 * Iterator<Bullet> it2 = bulletList.iterator();
						 * 
						 * if (it2 != null) {
						 * 
						 * while (it2.hasNext()) { Bullet b2 = (Bullet)
						 * it2.next(); if (this.collidesWith(b2.sprite)) {
						 * this.onDie(); } } }
						 * 
						 * }
						 * 
						 * private double getDiff(long oldTime, long newTime) {
						 * return newTime - oldTime; }
						 * 
						 * @Override public void onDie() {
						 * this.setVisible(false); this.squish();
						 * 
						 * } };
						 */
						/*
						 * enemy.setSize(75, 50); enemy.setSpeed((int)
						 * (Math.random() * 15));
						 */
						FeraalkEnemy de2 = new FeraalkEnemy();

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

		fuelText = new Text(580,460,resourcesManager.font, "Fuel: 00000000 (Recharging)", new TextOptions(HorizontalAlign.RIGHT), getVbom());
		healthText = new Text(10, 460, resourcesManager.font, "Life: 0123456789", new TextOptions(HorizontalAlign.LEFT), getVbom());
		scoreText = new Text(340,460,resourcesManager.font, "Score:00000000", new TextOptions(HorizontalAlign.CENTER),getVbom());
		
		xText = new Text(340,360,resourcesManager.font, "Score:00000000", new TextOptions(HorizontalAlign.CENTER),getVbom());
		yText = new Text(340,260,resourcesManager.font, "Score:00000000", new TextOptions(HorizontalAlign.CENTER),getVbom());
		
		fuelText.setColor(Color.GREEN);
		scoreText.setColor(0, 0, 100);
		healthText.setColor(100, 0, 0);
		
		fuelText.setScale(.4f);
		healthText.setScale(.5f);
		scoreText.setScale(.5f);
		
		scoreText.setAnchorCenter(0, 0);
		healthText.setAnchorCenter(0, 0);
		fuelText.setAnchorCenter(0, 0);
		
		scoreText.setText("Score:0");
		healthText.setText("Health: 1000");
		fuelText.setText("Fuel: 150");
		
		xText.setText("X: " );
		xText.setText("Y: " );
		

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

		gameHUD.attachChild(healthText);
		gameHUD.attachChild(fuelText);
		gameHUD.attachChild(scoreText);
		
		gameHUD.attachChild(xText);
		gameHUD.attachChild(yText);

		camera.setHUD(gameHUD);
	}

	public int getMapNum()
	{
		return mapNum;
	}

	public void setMapNum(int mapNum)
	{
		this.mapNum = mapNum;
	}

	private void createBackground()
	{
		setBackground(new Background(Color.CYAN));
	}

	private void addToLife(int i)
	{
		if (playerIsDead)
		{
			life = 0;
			healthText.setText("Life: " + life + "hp");
		} else
		{
			life += i;
			healthText.setText("Life: " + life + "hp");
		}
	}
	
	private void addToScore(int i)
	{
		score += i;
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