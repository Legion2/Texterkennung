package texterkennung.operator.ImageScaling;

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
    Data_ID originalImage;
    Data_ID scaledImage;

    float scaleFaktor;

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

    public int [] getRGB (int colourNum)
    {
    	int [] rgb = new int [3];

    	rgb[0] = (colourNum >> 16) & 0xff;			//red
		rgb[1] = (colourNum >> 8)  & 0xff;			//green
		rgb[2] = (colourNum) & 0xff;				//blue
    	return rgb;
    }

    /**
     * converts rgb array to int
     * @param rgb array with [0]=red; [1]=green; [2]=blue
     * @return returns int value
     */
    public int RGBtoInt (int [] rgb) {
    	int rgbValue=rgb[0];
    	rgbValue = (rgbValue << 8) + rgb[1];
    	rgbValue = (rgbValue << 8) + rgb[2];

    	return rgbValue;
    }


    @Override
    /**
     * @return Name of the Class
     */
    public String getName()
    {
        return "Operator_ImageScaling";
    }

    @Override
    /**
     * @return returns the scaled Image
     */
    public Data getData()
    {
        return this.scaledImage;
    }


}
