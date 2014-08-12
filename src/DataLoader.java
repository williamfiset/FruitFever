
/*
 * @author William Fiset
 *
 * This class allow you to:
 *   - Download images from the web and save them somewhere on your computer 
 *   - Load images from your computer to memory
 */

import acm.graphics.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

// import java.awt.*;
// import java.io.File;
// import java.lang.reflect.Method;
// import java.awt.image.DataBufferInt;
// import sun.awt.image.ToolkitImage;

public class DataLoader {

	private static boolean usingImageIO = true;
	private static DataLoader obj = null;


	/** This method will load an image given a path and a url to the image.
	  * 
	  * First it will try loading the image with imageIO (fastest)
	  * and if that also fails it loads the images from the GitHub server
	  * which is surprisingly fast also.
	  *
	  * @return - returns a BufferedImage of the path/Url specified
	  *
	  **/

    public static BufferedImage loadImage( String imgPath, String urlLink ){
        
		/** Try loading image with imageIO **/
	    if (usingImageIO) {

			// Make sure you instantiate a dataloader object to get the class
	    	if (obj == null) 
	    		obj = new DataLoader();

	        try {

	        	// Gets absolute path
				BufferedImage buffImage = null;
				buffImage = ImageIO.read(obj.getClass().getResource(imgPath));
				
				if (buffImage != null)
					return buffImage;
				
	        } catch (Exception e) {
	        	System.out.printf("Failed to Load imageIO: %s", imgPath);
	        	usingImageIO = false;
	        }
	    }

		/** If loading image from the computer fails load it using the web! **/
	    try {

			BufferedImage buffImage =null;

			// Reads image from the link provided
	        URL url = new URL(urlLink);
	        buffImage = ImageIO.read(url);
	

	        if (buffImage != null)
	       		return buffImage;	
	
	        else {

	        	// Prints which URL caused null
				String [] urlParts = urlLink.split("/");
				System.out.printf("\nFailed To Load URL:  %s ", urlParts[urlParts.length - 1]);
	        }

	    // Image is too large or no Internet connection
	    } catch(Exception e) {

	    	// Tell user to connect to the Internet?

        	// Prints which URL caused error
			String [] urlParts = urlLink.split("/");
			System.out.printf("\nFailed To Load URL:  %s ", urlParts[urlParts.length - 1]);

	    }


	    /** If return null executes it means that the image could not be loaded 
	     * using imageIO or using the Internet to download did not work **/
		return null;

    }

	/** 
	  * Try loading the image with imageIO 
	  *
	  * @return - returns a BufferedImage of the path/Url specified
	  *
	  **/

    public static BufferedImage loadImage( String imgPath ){
        
		/** Try loading image with imageIO **/
	    if (usingImageIO) {

			// Make sure you instantiate a dataloader object to get the class
	    	if (obj == null) 
	    		obj = new DataLoader();

	        try {

	        	// Gets absolute path
				BufferedImage buffImage = null;
				buffImage = ImageIO.read(obj.getClass().getResource(imgPath));
				
				if (buffImage != null)
					return buffImage;
				
	        } catch (IOException e) {
	        	System.out.printf("Failed to Load imageIO: %s", imgPath);
	        	usingImageIO = false;
	        }
	    }

	    /** If return null executes it means that the image could not be loaded using a toolKitImage,
	     * using imageIO nor using the Internet **/
		return null;

    }




	/** Saves an image from the web to the current working directory *
	  * NOTE: this only seems to works with images without alpha channels **/
	public static void downloadImageWithName(String urlName, String imageName){
		
		try {

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
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	/* Saves an image from the web to a location specified on the computer
	 * NOTE: this only seems to works with images without alpha channels */
	public void downloadImageWithPath(String urlName, String pathWithImageName){
		
		try {

			// Get url object and open connection
			URL url = new URL(urlName);
			URLConnection urlConnection = url.openConnection();
			
			// Places the image at a specified path
			FileOutputStream destination = new FileOutputStream( pathWithImageName );
			
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
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** This method will return a GImage (this we we can altogether avoid having)
	  * physical images in our project.
	  * 
	  * NOTE: Currently only works only with small images **/
	
	public static GImage getImageWithUrl(String urlName){
			
		BufferedImage image =null;
	    try{
	
	        URL url = new URL(urlName);
	        // read the url
	       image = ImageIO.read(url);
	
	    }catch(Exception e){
	    	System.out.println("Could not turn URL into image");
	        e.printStackTrace();
	    }
	    
	    // Try returning a GImage if applicable
	    if (image != null)
	    	return new GImage(image);
	    return null;
	}

}













