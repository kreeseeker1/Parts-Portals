package com.tesc.sos2014.objects;

//import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.Sprite;

import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.partsportals.MainGameEngineActivity;

public class Bullet 
{
	public Sprite sprite;
	
	public Bullet()
	{
		sprite = new Sprite(0,0,ResourcesManager.getInstance().bullet.deepCopy(),MainGameEngineActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite.setVisible(true);
	}

}
