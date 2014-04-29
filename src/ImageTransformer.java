/**
 *
 * @author William Fiset and http://javaingrab.blogspot.ca/2013/06/reflect-or-flip-image.html
 * 
 * 
 **/

import acm.graphics.GImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


abstract class ImageTransformer {


	/** Flips image Horizontally **/
	public static GImage horizontalFlip(GImage img){
     	
		BufferedImage src = toBufferedImage(img.getImage());

    	AffineTransform affineTransform = AffineTransform.getScaleInstance(-1.0, 1.0);  //scaling
    	affineTransform.translate(-src.getWidth(),0);  //translating
    	AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, null);  //transforming
     
     	return new GImage(affineTransformOp.filter(src, null));  //filtering

    }

    /** Flips image vertically (doesn't always work) **/
    
	// public static GImage verticalFlip(GImage img){
     	
	// 	BufferedImage src = toBufferedImage(img.getImage());

	//  	AffineTransform affineTransform = AffineTransform.getScaleInstance(1.0, -1.0);  //scaling
	//  	affineTransform.translate(-src.getWidth(),0);  //translating
	//  	AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, null);  //transforming
	     
	//  	return new GImage(affineTransformOp.filter(src, null));  //filtering

	//  }

    // ** Transforms an Image to BufferedImage **/
    private static BufferedImage toBufferedImage(Image img){

        if (img instanceof BufferedImage)
            return (BufferedImage) img;
        
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();

        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

	
}








