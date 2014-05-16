package com.tesc.sos2014.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class ParsePngFile {
	//CHANGE TO XML FILE EXTENSION WHEN READY TO USE!

/*	// Parser Option 1: Read png and write entities to an XML file (current one we're using)
	public static void parsePNG(String pathname) throws IOException {

		// Initialize writer and create buffer
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		// Header
		//BE SURE TO CHANGE THE PNG DIMENSION OF THE LEVEL HERE!
		bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!--This is parsed from \"" + pathname + "\"-->\n\n<level width=\"1000\" height=\"780\">\n");
		String pngPathName = "";

		Bitmap image = BitmapFactory.decodeFile(pathname);
		int width = image.getWidth();
		int height = image.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel = image.getPixel(x, y);
				int r1 = Color.red(pixel);
				int g1 = Color.green(pixel);
				int b1 = Color.blue(pixel);
				
				Log.v("Red" , "Color red " + r1);
				
				
				

				if (!file.exists()) {
					file.createNewFile();
				}
				
				//CHANGE THESE FILE NAME ENTRIES TO MATCH OUR PNG FILES
				
				if (r1 == 255 && g1 == 255 && b1 == 0) {
					pngPathName = "yellow.png";

				} else if (r1 == 255 && g1 == 255 && b1 == 255) {
					pngPathName = "white.png";

				} else if (r1 == 0 && g1 == 255 && b1 == 0) {
					pngPathName = "green.png";

				} else if (r1 == 255 && g1 == 0 && b1 == 0) {
					pngPathName = "red.png";

				} else if (r1 == 255 && g1 == 0 && b1 == 255) {
					pngPathName = "magenta.png";

				} else {
					pngPathName = "black.png";
				}

				// Print the entities that aren't black
				if (r1 == 0 && g1 == 0 && b1 == 0) {
					// Do nothing
				} else {
					bw.write("<entity x=\"" + x + "\" y=\"" + y + "\" type=\""
							+ pngPathName + "\"/>\n");
					// Test to console

					System.out.print("\n<entity x=\"" + x + "\" y=\"" + y
							+ "\" type=\"" + pngPathName + "\"/>   RGB: " + r1
							+ " - " + g1 + " - " + b1);

				}
			}

		}

		// Footer
		bw.write("</level>");
		bw.close();
	}

*/	
	
	
	
	//return map object
	
	
	//Array<GameData>
	// Parser Option 2: Stores XML entries in a vector (not currently used nor needed to produce XML file)
	public static LinkedList<Entity> readImageFile(String pathname)
			throws IOException {

		Bitmap image = BitmapFactory.decodeFile(pathname);
	
		Log.v("image decoded", "image ia null = " + (image == null));
		
		if(image == null) {
			Log.v("imagenull", "image null");
			return null;
		}
		else {
			
		
		int width = image.getWidth();
		int height = image.getHeight();
		String typeID = null;
		
				
		LinkedList<Entity> entities = new LinkedList<Entity>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
				Coordinate coordinate = new Coordinate(x, y, typeID);
				int pixel = image.getPixel(x, y);
				Entity entity = new Entity(coordinate, pixel, typeID);
				int r1 = Color.red(pixel);
				int g1 = Color.green(pixel);
				int b1 = Color.blue(pixel);
				
				Log.v("Red" , "Color red " + r1);
				Log.v("Green" , "Color red " + g1);
				Log.v("Blue" , "Color blue " + b1);
				
				if (r1 == 255 && g1 == 255 && b1 == 0) {
					entity.setTypeID("yellow");
					coordinate.setTypeID("yellow");

				} else if (r1 == 255 && g1 == 255 && b1 == 255) {
					entity.setTypeID("white");
					coordinate.setTypeID("white");

				} else if (r1 == 0 && g1 == 255 && b1 == 0) {
					entity.setTypeID("green");
					coordinate.setTypeID("green");

				} else if (r1 == 255 && g1 == 0 && b1 == 0) {
					entity.setTypeID("red");
					coordinate.setTypeID("red");

				} else if (r1 == 255 && g1 == 0 && b1 == 255) {
					entity.setTypeID("magenta");
					coordinate.setTypeID("magenta");

				} else {
					entity.setTypeID("black");
					coordinate.setTypeID("black");
				}
								
				
				if (r1 == 0 && g1 == 0 && b1 == 0) {

				} else {
					//Coordinate gameData = new Coordinate(x, y, typeID);
					//Coordinate coordinate = new Coordinate(x, y, type);
					//
					//mapObject mapObject = new mapObject(floorPlan, wallPlan, entityPlan);
					
					entities.add(entity);
					//coordinate.toString();
					//Log.v("Red" , "Color red " + r1);
					//System.out.println("Coordinate x = " + coordinate.getX() + " - " + "Coordinate y = " + coordinate.getY() + " Coordinate TypeID = " + coordinate.getTypeID());
					//Log.v("Coordinate ", "x" + coordinate.getX() + " - " + "Coordinate y = " + coordinate.getY() + " Coordinate TypeID = " + coordinate.getTypeID());
					
				}
			}
		}
		
		return entities;
		} //end if else
	}//method return if called
/*
	// Stores color entries in a vector (not currently used nor needed to produce XML file)
	public static List<Color> readPNGReturnColor(String pathname)
			throws IOException {
		BufferedImage image = ImageIO.read(new File(pathname));
		int width = image.getWidth();
		int height = image.getHeight();
		List<Color> colors = new ArrayList<Color>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color imageColor = new Color(image.getRGB(x, y));
				int r1 = imageColor.getRed();
				int g1 = imageColor.getGreen();
				int b1 = imageColor.getBlue();

				if (r1 == 0 && g1 == 0 && b1 == 0) {

				} else {
					colors.add(imageColor);
				}
			}
		}
		return colors;
	}
*/
}
