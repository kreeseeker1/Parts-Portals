package com.tesc.sos2014.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.tesc.sos2014.managers.SceneManager;
import com.tesc.sos2014.managers.SceneManager.SceneType;

public class OptionsMenu extends BaseScene implements IOnMenuItemClickListener{
	
	//---------------------------------------------
		// VARIABLES
		//---------------------------------------------
		
		private MenuScene menuChildScene;
		
		private final int LEVEL_ONE = 0;
		private final int LEVEL_TWO = 1;
		
		
		//---------------------------------------------
		// METHODS FROM SUPERCLASS
		//---------------------------------------------

		@Override
		public void createScene()
		{
			createBackground();
			createMenuChildScene();
		}

		@Override
		public void onBackKeyPressed()
		{
			System.exit(0);
		}

		@Override
		public SceneType getSceneType()
		{
			return SceneType.SCENE_MENU;
		}
		

		@Override
		public void disposeScene()
		{
			// TODO Auto-generated method stub
		}
		
		public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
		{
			switch(pMenuItem.getID())
			{
				case LEVEL_ONE:
					//Load Game Scene!
					SceneManager.getInstance().loadGameScene(engine);
					return true;
				case LEVEL_TWO:
					return true;
				default:
					return false;
			}
		}
		
		//---------------------------------------------
		// CLASS LOGIC
		//---------------------------------------------
		
		private void createBackground()
		{
			attachChild(new Sprite(400, 240, resourcesManager.menu_background_region, getVbom())
			{
	    		@Override
	            protected void preDraw(GLState pGLState, Camera pCamera) 
	    		{
	                super.preDraw(pGLState, pCamera);
	                pGLState.enableDither();
	            }
			});
		}
		
		private void createMenuChildScene()
		{
			menuChildScene = new MenuScene(camera);
			menuChildScene.setPosition(300, 240);
			
			final IMenuItem levelone = new ScaleMenuItemDecorator(new SpriteMenuItem(LEVEL_ONE, resourcesManager.play_region, getVbom()), 1.2f, 1);
			final IMenuItem leveltwo = new ScaleMenuItemDecorator(new SpriteMenuItem(LEVEL_TWO, resourcesManager.options_region, getVbom()), 1.2f, 1);
			
			menuChildScene.addMenuItem(levelone);
			menuChildScene.addMenuItem(leveltwo);
			menuChildScene.buildAnimations();
			menuChildScene.setBackgroundEnabled(false);
			//No getters for these values in original code 
			//fixed by correct importation and implementation of andengine version
			levelone.setPosition(super.getWidth() + 100, super.getHeight() - 100);
			leveltwo.setPosition(super.getWidth() + 100, super.getHeight() - 25);
			
			menuChildScene.setOnMenuItemClickListener(this);
			
			setChildScene(menuChildScene);
		
	}
}