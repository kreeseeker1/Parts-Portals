package com.tesc.sos2014.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.tesc.sos2014.managers.ResourcesManager;
import com.tesc.sos2014.managers.SceneManager.SceneType;




public abstract class BaseScene extends Scene
{
	
	//---------------------------------------------
		// VARIABLES
		//---------------------------------------------
		
		protected Engine engine;
		protected Activity activity;
		protected ResourcesManager resourcesManager;
		protected VertexBufferObjectManager vbom;
		protected BoundCamera camera;
		
		//---------------------------------------------
		// CONSTRUCTOR
		//---------------------------------------------
		
		public BaseScene()
		{
			this.resourcesManager = ResourcesManager.getInstance();
			this.engine = resourcesManager.engine;
			this.activity = resourcesManager.activity;
			this.vbom = resourcesManager.vbom;
			this.camera = resourcesManager.camera;
			createScene();
		}
		
		//---------------------------------------------
		// ABSTRACTION
		//---------------------------------------------
		
		public abstract void createScene();
		
		public abstract void onBackKeyPressed();
		
		public abstract SceneType getSceneType();
		
		public abstract void disposeScene();

}
