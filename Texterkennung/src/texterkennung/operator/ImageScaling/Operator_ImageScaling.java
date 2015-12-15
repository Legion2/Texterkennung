package texterkennung.operator.ImageScaling;

import java.awt.Color;

import debug.Debugger;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.operator.Operator;

/**
 *
 * @author Fabio Schmidberger
 *
 *	Scales a given Data_ID Object
 *	The scaled Data_ID Object is saved as scaledImage
 *	This Image can be accessed through the method getData()
 *
 */
abstract public class Operator_ImageScaling extends Operator
{
    protected final Data_ID originalImage;
    protected final Data_ID scaledImage;

    protected final float scaleFaktor;

    /**
     *
     * @param originalImage Image that should be scaled
     * @param scaleFaktor
     */
    public Operator_ImageScaling(Data_ID originalImage, float scaleFaktor)
    {
        this.originalImage = originalImage;
        this.scaleFaktor = scaleFaktor;
        this.scaledImage = new Data_Image((int) (originalImage.getXlenght() * scaleFaktor), (int) (originalImage.getYlenght() * scaleFaktor), "scaledImage", true);
    }

    abstract public void scale();


    /**
     * returns to a given int the rgb values in array
     * @param colourNum int value
     * @return [0]=red; [1]=green; [2]=blue
     */

    public int [] getRGBA (int colourNum)
    {
    	int [] rgba = new int [4];

    	//Debugger.error(scaledImage, ""+colourNum);

    	Color c = new Color(colourNum, true);

    	rgba[0] = c.getRed();
    	rgba[1] = c.getGreen();
    	rgba[2] = c.getBlue();
    	rgba[3] = c.getAlpha();
    	
    	//Debugger.info(this, "red: " + rgba[0] + " green: " + rgba[1] + " blue: " +rgba[2] + " alpha: " + rgba[3] );

    	return rgba;

    	/*

    	//int to rgb with error correction -> no values over 255 allowed
    	rgba[0] = (((colourNum >> 16) & 0xff) > 255) ? 255 : ((colourNum >> 16) & 0xff) ;		//red
		rgba[1] = (((colourNum >> 8) & 0xff) > 255) ? 255 : ((colourNum >> 8) & 0xff);			//green
		rgba[2] = (((colourNum) & 0xff) > 255) ? 255 : ((colourNum) & 0xff);						//blue
		rgba[3] = (((colourNum >> 24) & 0xff) > 255) ? 255 : ((colourNum >> 24) & 0xff);			//alpha
    	return rgba;

    	*/
    }

    /**
     * converts rgba array to int
     * @param rgba array with [0]=red; [1]=green; [2]=blue [3]=alpha
     * @return returns int value with rgba information
     */
    public int RGBAtoInt (int [] rgba) {

    	Color c = new Color (rgba[0], rgba[1], rgba[2], rgba[3]);

    	return c.getRGB();

    	/*
    	int rgbaValue=rgba[0];
    	rgbaValue = (rgbaValue << 8) + rgba[1];
    	rgbaValue = (rgbaValue << 16) + rgba[2];
    	rgbaValue = (rgbaValue << 24) + rgba[3];

    	return rgbaValue;
    	*/
    }

    /**
     * @return Name of the Class
     */
    @Override
    public String getName()
    {
        return "Operator_ImageScaling";
    }

    /**
     * @return returns the scaled Image
     */
    @Override
    public Data getData()
    {
        return this.scaledImage;
    }
}
