
/*
 * @author William Fiset
 * This class allow you to download images from the web and save them somewhere on your computer 
 */

import acm.graphics.*;
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
import java.lang.reflect.Method;
import java.awt.image.DataBufferInt;
// import sun.awt.image.ToolkitImage;

public class DataLoader {

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
	
	    }catch(IOException e){
	    	System.out.println("Could not turn URL into image");
	        e.printStackTrace();
	    }
	    
	    // Try returning a GImage if applicable
	    if (image != null)
	    	return new GImage(image);
	    return null;
	}
	
	/** This is the old image loading method, avoid using if possible **/
	public static BufferedImage getImageWithRelativePath(String relativePath){

		BufferedImage image = null;
		
		try { 
			image = ImageIO.read(new File(relativePath));
		}
		catch (IOException e) {	
			e.printStackTrace(); 
		}
		
		return image;
		
	}
	

	/** Uses the sun package toolKit to laod images **/
    public static BufferedImage getImageFromClassDirectory( String image ){
        
    	URL imagePath = null;

    	try{
			imagePath = new File( image ).toURI().toURL(); 
    	}catch(Exception e){}

        try{

            Image img = Toolkit.getDefaultToolkit().createImage( imagePath );
            Method method = img.getClass().getMethod( "getBufferedImage" );
            BufferedImage bufferedImage = null;
            int counter = 0;

            // Take 30 seconds maximum
            while( bufferedImage == null && counter < 3000 ){

                img.getWidth( null );
                bufferedImage = (BufferedImage) method.invoke( img );
                try{ Thread.sleep( 10 ); }
                catch( InterruptedException e ){ }
                counter ++;

            }
           
            if( bufferedImage != null )
                return bufferedImage;
            
        } catch( Exception e ){ }

        // If Loading with toolkit failed use the slower ImageIO method
        try{
            return ImageIO.read( imagePath );
        } catch( IOException ioe ){
            return null;
        }
    }

    /**
     * This is another bit of a hack and might also change
     */
    public static int[] loadPixelsCrazyFast( BufferedImage img ){
        return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }



	
}













