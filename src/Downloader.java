
/*
 * @author William Fiset
 * This class allow you to download images from the web and save them somewhere on your computer 
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;

public abstract class Downloader {

	/* Saves an image from the web to the current working directory */
	public static void downloadImageWithName(String urlName, String imageName){
		
		try{

			// Get url object and open connection
			URL url = new URL(urlName);
			URLConnection urlConnection = url.openConnection();
			
			// Gets the current Working directory
			FileOutputStream destination = new FileOutputStream( System.getProperty("user.dir") +"/" + imageName );
			
			BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(destination);

			// until the end of data, keep saving into file.
			int i;
			while ((i = in.read()) != -1) {
			    out.write(i);
			}
			out.flush();

			out.close();
			in.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/* Saves an image from the web to a location specified on the computer */
	public void downloadImageWithPath(String urlName, String pathWithImageName){
		
		try{

			// Get url object and open connection
			URL url = new URL(urlName);
			URLConnection urlConnection = url.openConnection();
			
			// Places the image at a specified path
			FileOutputStream destination = new FileOutputStream( pathWithImageName );
			
			BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(destination);

			// Until the end of data, keep saving into file.
			int i;
			while ((i = in.read()) != -1) {
			    out.write(i);
			}
			out.flush();

			out.close();
			in.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/** This method will return a GImage (this we we can altogether avoid having)
	  * physical images in our project.
	  * 
	  * NOTE: Currently only works only with small image sizes, for the final release
	  * we can make sure to break up our images into smaller ones and this would be no
	  * problem or to make the file sizes smaller. **/
	
	public static GImage getImage(String urlName){
			
		BufferedImage image = null;
	    try {
	
			URL url = new URL(urlName);
			image = ImageIO.read(url);
	
	    } catch(IOException e) {
	    	System.out.println("Could not turn URL into image");
	        e.printStackTrace();
	    }
	    
	    // Try returning a GImage if applicable
	    if (image != null)
	    	return new GImage(image);
	    return null;
	}
	
	public static GImage getBufferedImage(String urlName){
			
		BufferedImage image = null;
		
	    try {
			URL url = new URL(urlName);
			image = ImageIO.read(url);
	
	    } catch(IOException e) {
	    	System.out.println("Could not turn URL into an image.");
	        e.printStackTrace();
	    }
	    
	    return image
	}
}
