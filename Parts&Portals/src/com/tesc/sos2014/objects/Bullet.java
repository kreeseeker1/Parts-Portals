package com.tesc.sos2014.objects;

//import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.Sprite;

import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.scenes.GameScene;

public class Bullet 
{

	public Sprite sprite;
	public Bullet()
	{
		sprite = new Sprite(0,0,ResourcesManager.getInstance().bullet.deepCopy(),GameScene.getSharedInstance().getVbom());

		//Object bulletBody = PhysicsFactory.createCircleBody(this.physicsWorld, bullet, BodyType.DynamicBody, fd);
		//this.physicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet,bulletBody,true,true));
		//final Vector2 speed = Vector2Pool.obtain(50,0);
		
		//bulletBody.setLinearVelocity(speed);
		
	}

}
