package com.tesc.sos2014.spawners;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.objectenemies.FeraalkEnemy;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.pools.FeraalkEnemyPool;
import com.tesc.sos2014.scenes.GameScene;

public class DemiESpawner
{
	public float x;
	public float y;
	public Sprite s;
	public int groupNum = 5;

	public DemiESpawner(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{

		this.x = pX;
		this.y = pY;
		s= new Sprite(pX, pY, ResourcesManager.getInstance().bullet, vbo);
		s.setScale(0.000001f);
		s.setVisible(false);
		Body body = PhysicsFactory.createBoxBody(physicsWorld, s, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		
	}
	
	public void SpawnE()
	{
		for(int i =0; i <= groupNum; i++)
		{
			GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
			
			

			FeraalkEnemy DE = FeraalkEnemyPool.sharedDemiEnemyPool().obtainPoolItem();
			

			DE.aSprite.setPosition(x, y);
			
			DE.aSprite.setVisible(true);
		
			DE.aSprite.detachSelf();
			
			DE.aSprite.setSize(40, 40);
			DE.animateMe();
			
			scene.demiEnemyCount++;

			scene.attachDemiEnemy(DE);
			
			//Get Enemy from Pool
			//Attach to Scene
			//Add to enemyList in GameScene
		}
	}
}
