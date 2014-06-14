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
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
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
import com.tesc.sos2014.objectenemies.BeriusEnemy;
import com.tesc.sos2014.objectenemies.BeriusLEnemy;
import com.tesc.sos2014.objectenemies.EthsersEnemy;
import com.tesc.sos2014.objectenemies.EthsersHiveMind;
import com.tesc.sos2014.objectenemies.FeraalkEnemy;
import com.tesc.sos2014.objectenemies.ScrichBossEnemy;
import com.tesc.sos2014.objects.Bullet;
import com.tesc.sos2014.objects.Health;
import com.tesc.sos2014.objects.Player;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.pools.BulletPool;
import com.tesc.sos2014.pools.FeraalkEnemyPool;
import com.tesc.sos2014.utilities.Entity;
import com.tesc.sos2014.utilities.ParsePngFile;

public class GameScene extends BaseScene implements IOnSceneTouchListener
{
	long newTime = System.nanoTime();
	long oldTime;
	private int life = 100;
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
	public ArrayList<BeriusEnemy> beriusList;
	public ArrayList<EthsersEnemy> ethsersList;
	public ArrayList<EthsersHiveMind> ethsersHiveList;
	public ArrayList<BeriusLEnemy> beriusLEnemyList;

	
	public ArrayList<BeriusLEnemy> beriusLList;
	public ArrayList<ScrichBossEnemy> scrichList;
	

	//Entity Lists
	public List<Entity> eList;
	public List<Entity> floorList;
	public List<Entity> wallList;
	public List<Entity> itemList;
	public List<Entity> blackList;
	public List<Entity> enemyList;
	
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
	


	public GameScene()
		{
			engine.registerUpdateHandler(new com.tesc.sos2014.managers.GameLoopUpdateHandler());
		}

	@Override
	public void createScene()
	{
		
		
		Log.v("CreateScene", "CreateScene Started");
		
		bulletList = new LinkedList<Bullet>();//These two LinkedList Declarations MUST be called right here.
		//BulletPool.sharedBulletPool().batchAllocatePoolItems(50);
		itemList = new ArrayList<Entity>();
		floorList = new ArrayList<Entity>();
		wallList = new ArrayList<Entity>();
		blackList = new ArrayList<Entity>();
		enemyList = new ArrayList<Entity>();
		
		feraalkList = new ArrayList<FeraalkEnemy>();
		ethsersList = new ArrayList<EthsersEnemy>();
		ethsersHiveList = new ArrayList<EthsersHiveMind>();
		beriusList = new ArrayList<BeriusEnemy>();
		beriusLEnemyList = new ArrayList<BeriusLEnemy>();
		scrichList = new ArrayList<ScrichBossEnemy>();
		
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
		
		//Red:    255,0,0 	  (Enemy)
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
				
				
				enemyList.add(eList.get(i));
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
		
		
		
		
		for(int i=0; i<= enemyList.size()-1;i++)
		{
		if(enemyList.get(i).getColor().getRed() == 255)
		{
			FeraalkEnemy fe = new FeraalkEnemy();
			fe.aSprite = new AnimatedSprite(enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50, ResourcesManager.getInstance().enemy.deepCopy(), MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
		//	fe.aSprite.setPosition();
			
			
			//feraalkList.add(fe);
			attachFeraalkEnemy(fe);
		}
		else if(enemyList.get(i).getColor().getRed() == 254)
		{
			EthsersHiveMind e = new EthsersHiveMind(enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50);
			e.aSprite =  new AnimatedSprite(enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50, ResourcesManager.getInstance().ethsers.deepCopy(), MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
			e.aSprite.setSize(10, 10);
			
			attachEthsersHive(e, enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50);
		}
		else if(enemyList.get(i).getColor().getRed() == 253)
		{
			BeriusLEnemy bel = new BeriusLEnemy(enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50);
			bel.aSprite = new AnimatedSprite(enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50, ResourcesManager.getInstance().beriusl.deepCopy(),MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
			bel.aSprite.setSize(55, 55);
			
			attachBeriusLeader(bel, enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50 );
		}
		else if(enemyList.get(i).getColor().getRed() == 252)
		{
			ScrichBossEnemy s = new ScrichBossEnemy();
			
			attachScrich(s , enemyList.get(i).getCoordinate().getX()*50,enemyList.get(i).getCoordinate().getY()*50);
		}
		else if(enemyList.get(i).getColor().getRed() == 251)
		{
			
		}
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
		MassData data = new MassData();
		data.mass = 200f;

		player.body.setMassData(data);
		
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

		Log.v("Bullet ADDED", "Bullet set to visible ");
		b.sprite.setVisible(true);
		bulletList.add(b);
		this.attachChild(b.sprite);
	}

	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void attachFeraalkEnemy(FeraalkEnemy fe)
	{
			fe.aSprite.setSize(45, 50);
		FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		fe.body = PhysicsFactory.createCircleBody(physicsWorld, fe.aSprite, BodyType.DynamicBody, fd);
		fe.body.setActive(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(fe.aSprite, fe.body, true, false));

		fe.body.setUserData(fe.aSprite);

		fe.aSprite.setSize(75, 50);
		fe.animateMe();
		
		Log.v("DE ", "DE to string " + fe.
				aSprite.
				toString());
		feraalkList.add(fe);
		this.attachChild(fe.aSprite);

		/*try
		{
			feraalkList.add(DE);
			// this.DEList.add(de);
			Log.v("DEMI ENEMY LIST COUNT", "#" + feraalkList.size());
			this.attachChild(de.aSprite);
		} catch (NullPointerException jnpe)
		{
			Log.v("NPE", jnpe.toString());
		}*/

	}
	
	public void attachScrich(ScrichBossEnemy s, float x, float y)
		{
			MassData data = new MassData();
			data.mass = 8000f;
			
			s.aSprite = new AnimatedSprite(x,y,ResourcesManager.getInstance().scrich, getVbom());
			
			s.aSprite.setSize(190, 190);
			FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
			s.body = PhysicsFactory.createCircleBody(physicsWorld, s.aSprite, BodyType.DynamicBody, fd);
			s.body.setActive(true);
			s.body.setMassData(data);
			
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(s.aSprite, s.body, true, false));
			s.body.setUserData(s.aSprite);
			s.aSprite.setSize(200, 200);
			s.aSprite.animate(s.SCRICH_ANIMATE);
			
			scrichList.add(s);
			attachChild(s.aSprite);
			
			
		}
	
	public void attachBeriusLeader(final BeriusLEnemy bel, final float x, final float y)
		{
			FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
			MassData data = new MassData();
			data.mass = 4000f;
			
			
			
			bel.aSprite = new AnimatedSprite(x,y,ResourcesManager.getInstance().beriusl.deepCopy(),getVbom());
			bel.aSprite.setSize(45, 45);
			bel.body = PhysicsFactory.createCircleBody(physicsWorld, bel.aSprite, BodyType.DynamicBody, fd);
			
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(bel.aSprite, bel.body, true, false));
			 
			bel.body.setUserData(bel.aSprite);
			bel.aSprite.setSize(50, 50);
			//bel.body.setMassData(data);
			bel.aSprite.animate(bel.BL_ANIMATE);
			bel.pace();
			
			beriusLEnemyList.add(bel);
			
			for(int i=0; i<= 10;i++)
			{
				
				 final BeriusEnemy be1 = new BeriusEnemy();
				 be1.myleader = beriusLEnemyList.indexOf(bel);
					be1.aSprite =  new AnimatedSprite(x+10*i,y+10*i, ResourcesManager.getInstance().berius.deepCopy(), MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
					be1.aSprite.setSize(40,40);
					
					be1.body = PhysicsFactory.createCircleBody(physicsWorld, be1.aSprite, BodyType.DynamicBody, fd);
					
					//physicsWorld.registerPhysicsConnector(new PhysicsConnector(be1.aSprite, be1.body, true, false));
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(be1.aSprite, be1.body, true, false));
					//e1.body.setLinearVelocity(-1*5, 0);
					be1.aSprite.animate(bel.BL_ANIMATE);
					be1.aSprite.setSize(50, 50);
					beriusList.add(be1);
					be1.pace();
					
					attachChild(be1.aSprite);
			}
			
			attachChild(bel.aSprite);
			
			
			
			
			
		}
	
	public void attachEthsersHive(final EthsersHiveMind e, final float x ,float y)
		{
			FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		
			e.body = PhysicsFactory.createCircleBody(physicsWorld, e.aSprite, BodyType.KinematicBody, fd);
			//e.body.setLinearVelocity(-1*5, 0);
			e.aSprite.animate(e.ENEMY_ANIMATE);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(e.aSprite,e.body,true,false)
				{
					 @Override
					    public void onUpdate(float pSecondsElapsed)
					    {
					        super.onUpdate(pSecondsElapsed);
					               
					        if (e.aSprite.getX() <= x - e.maxMovement)
					        {
					            e.body.setLinearVelocity(e.body.getLinearVelocity().x * -1, 0);
					        }
					        if (e.aSprite.getX() >= x + e.maxMovement)
					        {
					            e.body.setLinearVelocity(e.body.getLinearVelocity().x * -1, 0);
					        }
					    }
				}
					);
			
			
			
			for(int i =0; i<= 20;i++)
			{
				 final EthsersEnemy e1 = new EthsersEnemy();
				e1.aSprite =  new AnimatedSprite(x-10*i,y+10*i, ResourcesManager.getInstance().ethsers.deepCopy(), MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
				e1.aSprite.setSize(5, 5);
				
				e1.body = PhysicsFactory.createCircleBody(physicsWorld, e1.aSprite, BodyType.DynamicBody, fd);
				physicsWorld.registerPhysicsConnector(new PhysicsConnector(e1.aSprite, e1.body, true, false));
				//e1.body.setLinearVelocity(-1*5, 0);
				e1.aSprite.animate(e.ENEMY_ANIMATE);
				e1.aSprite.setSize(40, 40);
				ethsersList.add(e1);
				
		
				
				
				attachChild(e1.aSprite);
				
			}
			
			
			
			
			ethsersHiveList.add(e);
			attachChild(e.aSprite);
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
	
	
	//*******************************************************************************************************************************************
	// POOL/LOOP LOGIC BLOCK START
	//*******************************************************************************************************************************************
	public void cleaner()
	{
		synchronized (this)
		{
			//Player
		//	xText.setText("X: " + player.getX());
		//	yText.setText("Y: " + player.getY());
			
			Log.v("PLayer X","" + player.getX());
			Log.v("Player Y","" + player.getY());
			
			Log.v("Camera X","" + camera.getCenterX());
			Log.v("Camera Y","" + camera.getCenterY());
			
			
			for(int i=0; i<=ethsersList.size()-1;i++)
			{
				Log.v("EthsersList", "Loop # " + i);
				if(ethsersList.get(i).aSprite.getX() < ethsersHiveList.get(0).aSprite.getX()-10)
				{
					Log.v("EthsersList", "Running Left");
					ethsersList.get(i).runLeft();
				}
				else if(ethsersList.get(i).aSprite.getX()  > ethsersHiveList.get(0).aSprite.getX() + 10)
				{
					Log.v("EthsersList", "Running right");
					ethsersList.get(i).runRight();
				}
				
				if(ethsersList.get(i).aSprite.getY()  < ethsersHiveList.get(0).aSprite.getY() - 10
						)
				{
					Log.v("EthsersList", "Jumping");
					ethsersList.get(i).jump();
				}
			}
			
			
			
			Iterator<Sprite> fsl = floorSpriteList.iterator();
			while(fsl.hasNext())
			{
				if(player.collidesWith(fsl.next()) && fsl.next().getY() / 50 < player.getY() + 20)
				{
					xText.setText("Player Y" + player.getY());
					yText.setText("Floor Y " + fsl.next().getY()/50);
					player.rechargeJets();
				}
		
			}
			
			
			//This code works just like the above code but produced a noticeable lag
			/*for(int i= 0; i<= floorSpriteList.size()-1;i++)
			{
				if(player.collidesWith(floorSpriteList.get(i)) && floorSpriteList.get(i).getY() / 50 < player.getY() + 20)
				{
					
					//xText.setText("Player Y" + player.getY()/50);
					//yText.setText("Floor Y " + floorSpriteList.get(i).getY()/50);
					player.rechargeJets();
					return;
				}
				else if(player.collidesWith(floorSpriteList.get(i)) && floorSpriteList.get(i).getY() / 50 > player.getY() - 20)
				{
					return;
				}
				Log.v("rechargeJets","I: " + i );
			}*/
			
			
			
			
			//Enemies and Bullets
			Iterator<Bullet> bl = bulletList.iterator();
			
			
			
			Iterator<FeraalkEnemy> fel = feraalkList.iterator();
			Iterator<EthsersEnemy> ee = ethsersList.iterator();
			Iterator<BeriusEnemy> be = beriusList.iterator();
			Iterator<BeriusLEnemy> bel = beriusLEnemyList.iterator();
			Iterator<ScrichBossEnemy> sbe = scrichList.iterator();
			
			while(sbe.hasNext())
			{
				ScrichBossEnemy sb = sbe.next();
				if(!sb.isDead())
				{
					sb.pace();
				}
			}
			
			
			
			
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????
			//BULLET LOOP START
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????
			while (bl.hasNext())
			{
				Bullet b = (Bullet) bl.next();
				
				
				
				while(ee.hasNext())
				{
					EthsersEnemy e =  (EthsersEnemy)ee.next();
					
					if(b.sprite.collidesWith(e.aSprite))
					{
						Log.v("SQUISH", "Squish Activated. Ethserslist size: " + ethsersList.size());
						e.squish();
						ee.remove();
						b.bulletBody.setTransform(-1000, -1000, 0);
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						addToScore(10);
						scoreText.setText("Score:" + score);
					}	
					
					if(player.collidesWith(e.aSprite) || e.aSprite.collidesWith(player))
					{
						addToLife(-2);
					}
				}
				
				while(be.hasNext())
				{
					BeriusEnemy ber = be.next();
					
					if(b.sprite.collidesWith(ber.aSprite))
					{
						ber.squish();
						be.remove();
						b.bulletBody.setTransform(-1000, -1000, 0);
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						addToScore(5);
						scoreText.setText("Score:" + score);
					}
					if(player.collidesWith(ber.aSprite) || ber.aSprite.collidesWith(player))
					{
						addToLife(-1);
					}
					if(beriusLEnemyList.get(ber.myleader).aSprite.collidesWith(ber.aSprite))
					{
						ber.jump();
					}
				}
				
				while(bel.hasNext())
				{
					
					BeriusLEnemy berl = bel.next();
					int leaderIndex = beriusLEnemyList.indexOf(berl);
					if(b.sprite.collidesWith(berl.aSprite) )
					{
						berl.squish();
						bel.remove();
						b.bulletBody.setTransform(-1000, -1000, 0);
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						addToScore(15);
						scoreText.setText("Score:" + score);
						for(int i=0; i<= beriusList.size()-1;i++)
						{
							if(beriusList.get(i).myleader == leaderIndex)
							{
								beriusList.get(i).myleader =0;
							}
						}
						
					}
					
				}
				
				

				while (fel.hasNext())
				{
					FeraalkEnemy fe = (FeraalkEnemy) fel.next();
					
					Log.v("BL", "BL HasNext: " + bl.hasNext());
					Log.v("DEL", "DEL HasNext" + fel.hasNext());
					Log.v("FE ", "FE to string " + fe.aSprite.toString());
					
					if(b.sprite.collidesWith(fe.aSprite))
					{
						Log.v("SQUISH", "Squish Activated. DEList size: " + feraalkList.size() + ". demiEnemyCount: " + demiEnemyCount);
						fe.squish();
						fe.body.setActive(false);
						fel.remove();
						b.bulletBody.setTransform(-1000, -1000, 0);
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						addToScore(10);
						scoreText.setText("Score:" + score);
					}
					 if( fe.aSprite.getY() <= 0)
					{
						Log.v("SQUISH", "Squish Activated. DEList size: " + feraalkList.size() + ". demiEnemyCount: " + demiEnemyCount);
						fe.squish();
						fel.remove();
					}
					 if (player.collidesWith(fe.aSprite) || fe.aSprite.collidesWith(player))
					{
						addToLife(-1);
					}

				}

				
				b.setNewX(b.sprite.getX());

				if (b.bulletLife <= 0 || b.sprite.getY() <= -b.sprite.getHeight() || !camera.isEntityVisible(b.sprite) || (b.getOldX() == b.getNewX())) // )
				{
					Log.v("Cleaner", "Bullet Removed.");
					Log.v("Children", "Number of Children" + this.getChildCount());

					b.bulletBody.setTransform(-1000, -1000, 0);
					BulletPool.sharedBulletPool().recyclePoolItem(b);
					bl.remove();
					continue;
				} else
				{
					b.bulletLife--;
					b.setOldX(b.getNewX());
					b.setNewX(b.sprite.getX());
					continue;
				}
			}

			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????
			//BULLET LOOP STOP
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????
			
			
			
			
			
			
			
			// ////////////////////////////////////Begin AI Loop
			// //////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			if(fel != null)
			{
				
			
			while (fel.hasNext())
			{
				FeraalkEnemy fe = (FeraalkEnemy) fel.next();

				if(fe != null)
				{
				if (fe.isDead())
				{

					fe.aSprite.setVisible(false);
					fe.body.setActive(false);
					FeraalkEnemyPool.sharedDemiEnemyPool().recyclePoolItem(fe);
				} 
				else if (!fe.isDead())
				{
					if(!fe.canSee(player) )//&& player.getY() < fe.aSprite.getY() + 100 )
					 {
					  fe.pace();
					 }
					else
					 {
						if(fe.canSee(player) && fe.onMyLeft(player))
						{
							fe.paceFacing(player);
							//fe.paceFacing(player);
							//fe.jumpAt(player);
						}
						else if(fe.canSee(player) &&fe.onMyRight(player))
						{
							fe.paceFacing(player);
							//fe.paceFacing(player);
							//fe.jumpAt(player);
						}
					 } 
				}
			}
				
			}
			
			while(bel.hasNext())
			{
				BeriusLEnemy bel1 = bel.next();
				
				if(!bel1.isDead())
				{
					bel1.pace();
				}
			}
			
			while(be.hasNext())
			{
				BeriusEnemy be1 = be.next() ;
				if(be1.isDead())
				{
					
				}
				else if(!be1.isDead() && beriusList.size() > 1)
				{
					Log.v("BE BERIUS" , " Berius size: " + beriusList.size());
					be1.pace();
				}
				else
				{
					Log.v("BE BERIUS" , " Berius size: " + beriusList.size());
				}
			}
			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ////////////////////////////////////////////////////////End AI
			// Loop
			// //////////////////////////////////////////////////////////////////////////////
		}
		}
	}
	
	//*******************************************************************************************************************************************
		// POOL/LOOP LOGIC BLOCK END
		//*******************************************************************************************************************************************

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
		//bulletList.clear();

		
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
		bulletList.clear();

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
		
		xText = new Text(340,360,resourcesManager.font, "Score:0000000000000000000000000000", new TextOptions(HorizontalAlign.CENTER),getVbom());
		yText = new Text(340,260,resourcesManager.font, "Score:0000000000000000000000000000", new TextOptions(HorizontalAlign.CENTER),getVbom());
		
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
		healthText.setText("Health: 100");
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

		final Sprite left = new Sprite(40, 90, 60, 60,ResourcesManager.getInstance().green_button.deepCopy(), getVbom())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						player.runLeft();
						player.setFlippedHorizontal(true);

					}
					else if(touchEvent.isActionUp())
					{
						player.stop();
						player.stopAnimation(0);
					}
					

					return true;
				};
			};

		final Sprite right = new Sprite(camera.getWidth() - 60, 90, 60, 60, ResourcesManager.getInstance().green_button.deepCopy(), getVbom())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						player.runRight();
						player.setFlippedHorizontal(false);
					}
					else if(touchEvent.isActionUp())
					{
						player.stop();
						player.stopAnimation(0);
					}
					
					return true;
				};
			};

		final Sprite fire = new Sprite(camera.getWidth() - 60, 180, 60, 60, ResourcesManager.getInstance().red_button.deepCopy(), getVbom())
			{
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
				{
					if (touchEvent.isActionDown())
					{
						synchronized (this)
						{
							player.shoot();
							//right.onAreaTouched( touchEvent.isActionDown() , 0f, 0f);
						}

					}
					/*
					 * else { player.stop(); }
					 */
					return true;
				};
			};
			
			final Sprite launch = new Sprite(40, 180, 60, 60, ResourcesManager.getInstance().yellow_button.deepCopy(), getVbom())
				{
					public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
					{
						if (touchEvent.isActionDown())
						{
							player.setJumping(true);
							player.jps.setParticlesSpawnEnabled(true);
							//fuelText.setText("Fuel: " + player.getJumpTimer());

						}
						else if(touchEvent.isActionUp())
						{
							player.setJumping(false);
							player.jps.setParticlesSpawnEnabled(false);
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
		gameHUD.registerTouchArea(launch);

		// gameHUD.registerTouchArea(stick);
		// gameHUD.attachChild(stick);

		gameHUD.attachChild(left);
		gameHUD.attachChild(right);
		gameHUD.attachChild(fire);
		gameHUD.attachChild(launch);

		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		gameHUD.attachChild(healthText);
		gameHUD.attachChild(fuelText);
		gameHUD.attachChild(scoreText);
		
		//gameHUD.attachChild(xText);
	//	gameHUD.attachChild(yText);

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

        final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
    	setBackground(autoParallaxBackground);
    	
    	final Sprite parallaxLayerBackSprite = new Sprite(0, 0, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		
		final Sprite parallaxLayerCenterSprite = new Sprite(250, 400, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		
		final Sprite parallaxLayeTopSprite = new Sprite(250, 200, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		
		final Sprite parallaxLayeESprite = new Sprite(0, 400, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		
		final Sprite parallaxLayeESprite1 = new Sprite(0, 230, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		
		final Sprite parallaxLayeESprite2 = new Sprite(100, 270, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		//
		
		final Sprite parallaxLayeESprite3 = new Sprite(0, 200, ResourcesManager.getInstance().gbg.deepCopy(), getVbom());
		parallaxLayerBackSprite.setOffsetCenter(0, 0);
		
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerBackSprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerCenterSprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayeTopSprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayeESprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayeESprite1));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayeESprite2));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayeESprite3));
		
		
		
			setBackground(autoParallaxBackground);
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
		return null;}
}