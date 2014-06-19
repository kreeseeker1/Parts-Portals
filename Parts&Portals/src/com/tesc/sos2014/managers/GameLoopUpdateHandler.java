package com.tesc.sos2014.managers;

import org.andengine.engine.handler.IUpdateHandler;

import android.util.Log;

import com.tesc.sos2014.partsportals.MainGameEngineActivity;
import com.tesc.sos2014.scenes.GameScene;

public class GameLoopUpdateHandler implements IUpdateHandler
{

	public GameLoopUpdateHandler()
	{
		//onUpdate();
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		
		//GameScene scene = (GameScene) MainGameEngineActivity.getSharedInstance().mCurrentScene;
		//Log.v("Cleaner Called", "Cleaner is getting called from GameLoopUpdateHandler. Number of children: " + scene.getChildCount() );
		((GameScene)MainGameEngineActivity.getSharedInstance().mCurrentScene).cleaner();
		((GameScene)MainGameEngineActivity.getSharedInstance().mCurrentScene).doAILoop();
		//((GameScene)MainGameEngineActivity.getSharedInstance().mCurrentScene).EnemyUpdater();
		//((GameScene)MainGameEngineActivity.getSharedInstance().mCurrentScene).BulletInit();
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
