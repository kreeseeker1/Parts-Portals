package com.tesc.sos2014.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;


import com.tesc.sos2014.managers.SceneManager.SceneType;



public class LoadingScene extends BaseScene
{
	@Override
	public void createScene()
	{
		//HACK!!!!! May not Work!!!
		setBackground(new Background(Color.BLACK));
		Text loading = new Text(400, 240, resourcesManager.font, "Loading Resources...", getVbom());
		//loading.setScale(.3f);
		attachChild(loading);
	
		//HACK!!!!!
		
		
		//attachChild(new Text(400, 240, resourcesManager.font, "Loading Resources...", getVbom()));
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene()
	{

	}
}
