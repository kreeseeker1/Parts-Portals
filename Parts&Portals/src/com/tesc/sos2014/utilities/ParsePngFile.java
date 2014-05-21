package com.tesc.sos2014.utilities;


import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;



public class ParsePngFile  {
	
	private Context ct;// = new Context();
	private String fName;
	
	ParsePngFile(Context context, String fName)
	{
		this.ct = context;
		this.fName = fName;
	}
	
	
		
	

	public void ParseFile()
	{
		Resources resources = ct.getResources();
		final int resourceId = resources.getIdentifier(fName, "drawable",  ct.getPackageName());
		
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		Bitmap b = BitmapFactory.decodeResource(ct.getResources(), resourceId, options);
		Log.v("Bitmap", "drawable is null = " + (b == null));
		for (int x = 0; x < b.getWidth(); x++)		
	    {
			for (int y = 0; y < b.getHeight(); y++)
	        {
				StringBuilder sb1 = new StringBuilder();
				sb1.append("x = " + x + ", y = " + y);
				//Log.v("COORDINATE", sb1.toString());
	            int c = b.getPixel(x, y);
	            Log.v("C" , "c = " + c);
	            
	            int  redColors = Color.red(c);
	            int greenColors = Color.green(c);
	            int blueColors = Color.blue(c);
	            StringBuilder sb = new StringBuilder();
	            sb.append("red = " + redColors + ", blue = " + blueColors + ", green = " + greenColors);
	            Log.v("RGB" , sb.toString());
	        }
	    }
	}
		/*try {
			InputStream ims = getAssets().open("level-01.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
			Log.v("DRAWABLE", "drawable is null = " + (d == null));		
			Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ALPHA_8);
			Log.v("BITMAP", "bitmap is null = " + (bitmap == null));
			for (int x = 0; x < bitmap.getWidth(); x++)		
		    {
				for (int y = 0; y < bitmap.getHeight(); y++)
		        {
					StringBuilder sb1 = new StringBuilder();
					sb1.append("x = " + x + ", y = " + y);
					//Log.v("COORDINATE", sb1.toString());
		            int c = bitmap.getPixel(x, y);
		            Log.v("C" , "c = " + c);
		            
		           int  redColors = Color.red(c);
		            int greenColors = Color.green(c);
		            int blueColors = Color.blue(c);
		            StringBuilder sb = new StringBuilder();
		            sb.append("red = " + redColors + ", blue = " + blueColors + ", green = " + greenColors);
		            Log.v("RGB" , sb.toString());
		        }
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		
	}



