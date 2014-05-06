package com.tesc.sos2014.objects;

//import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.physics.box2d.Body;
import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;

public class Bullet 
{
	public float oldX = 0;
	public float getOldX()
	{
		return oldX;
	}

	public void setOldX(float oldX)
	{
		this.oldX = oldX;
	}

	public Sprite sprite;
	public Body bulletBody;
	public int bulletLife = 100;
	
	public Bullet()
	{
		
		sprite = new Sprite(0,0,ResourcesManager.getInstance().bullet.deepCopy(),MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite.setVisible(true);
	}

}
