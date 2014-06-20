package com.tesc.sos2014.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.tesc.sos2014.managers.ResourcesManager;

public class ParsePngFile {

	private Context context;
	private static List<Entity> entities = new ArrayList<Entity>();
	public static Bitmap b = null;
	
	
	public ParsePngFile(Context context) {
		this.context = context;
	}

	public static void parsePNGFile() {
		BitmapFactory.Options options = new BitmapFactory.Options();
	   b = BitmapFactory.decodeResource(
				ResourcesManager.getInstance().activity.getResources(),
				com.tesc.sos2014.R.drawable.level_01, options);
			
		for (int x = 0; x < b.getWidth(); x++) {
			for (int y = 0; y < b.getHeight(); y++) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append("x = " + x + ", y = " + y);
				int c = b.getPixel(x, y);
				int redColor = Color.red(c);
				int greenColor = Color.green(c);
				int blueColor = Color.blue(c);
				StringBuilder sb = new StringBuilder();
				
				MyColor color = new MyColor(redColor, greenColor, blueColor);
				Coordinate coordinate = new Coordinate(x, y);
				Entity entity = new Entity(coordinate, color);
				entities.add(entity);

				//Log.v("RGB", sb.toString());
				
				/*Log.v("ENTITY", "x =  " + 
								entities.get(0).getCoordinate().getX()
								+ "y = " + entities.get(0).getCoordinate().getY()
								+ "type = " + entities.get(0).getCoordinate().getTypeID());*/
			}
		}
		/*Log.v("SIZE", "entities size: " + getEntities().size());
		Log.v("ENTITY1", "x =  " + 
				entities.get(0).getCoordinate().getX()
				+ "y = " + entities.get(0).getCoordinate().getY()
				+ "type = " + entities.get(0).getCoordinate().getTypeID());
		
		Log.v("ENTITY2", "x =  " + 
				entities.get(1).getCoordinate().getX()
				+ "y = " + entities.get(1).getCoordinate().getY()
				+ "type = " + entities.get(1).getCoordinate().getTypeID());
		
		Log.v("ENTITY3", "x =  " + 
				entities.get(2).getCoordinate().getX()
				+ "y = " + entities.get(2).getCoordinate().getY()
				+ "type = " + entities.get(2).getCoordinate().getTypeID());*/
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public static List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

}
