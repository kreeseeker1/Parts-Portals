package com.tesc.sos2014.objects;

//import org.andengine.entity.sprite.Sprite;
import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityComparator;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.transformation.Transformation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.scenes.GameScene;

public class Bullet 
{
	final long[] ENEMY_ANIMATE = new long[] { 100, 100, 100 };
	public Sprite sprite;
	public AnimatedSprite asprite = null;
	public Body bulletBody= null;
	
	public Bullet()
	{
		sprite = new Sprite(0,0,ResourcesManager.getInstance().bullet.deepCopy(),MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());

		/*GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
		
		
		//FixtureDef fd = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		
		
		 bulletBody = PhysicsFactory.createCircleBody(scene.physicsWorld, sprite, BodyType.DynamicBody, fd);
		bulletBody.setActive(true);
		
		
		//scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite,bulletBody,true,true));
		final Vector2 speed = Vector2Pool.obtain(50,0);
		
		//bulletBody.setUserData(sprite.getTextureRegion());
		
		bulletBody.setLinearVelocity(speed);
		Vector2Pool.recycle(speed);*/
		
		//sprite.setUserData(ResourcesManager.getInstance().bullet.deepCopy());
		//asprite.animate(ENEMY_ANIMATE);
		
		//bulletBody.setUserData(sprite);
		
		
		
		
	}

}
