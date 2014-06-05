package com.tesc.sos2014.objectenemies;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;

public class ScrichBossEnemy extends BaseEnemy
{

	public ScrichBossEnemy()
		{
			
			super(ResourcesManager.getInstance().scrich.deepCopy(), new long[] {100,100,100});
			ENEMY_ANIMATE = new long[] {100,100,100};
			// TODO Auto-generated constructor stub
		}

	
	
}