package com.tesc.sos2014.partsportals;

import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import org.andengine.*;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;

public class Main extends BaseGameActivity
	{
	
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	
	private Camera mCamera;
	private Texture mTexture;
	private TextureRegion mSplashTextureRegion;
	
	
	

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.f_main, container, false);
			return rootView;
		}
	}

	@Override //Listed in the book as onLoadEngine which was from GLES1 instead of GLES2
	//more info at http://www.andengine.org/forums/gles2/book-needs-update-learning-android-game-programming-t6189.html -- note that this article is 3 years old at this point
	public EngineOptions onCreateEngineOptions() {
       this.mCamera = new Camera(0,0,CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),this.mCamera);
	
	}

	
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback arg0)
			throws Exception {
		this.mTexture = new Texture(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mSplashTextureRegion = TextureRegionFactory.createFromSource(this.mSplashTextureRegion, this, "gfx/Splashscreen.png",0,0);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPopulateScene(Scene arg0, OnPopulateSceneCallback arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
