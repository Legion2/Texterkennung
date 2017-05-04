package texterkennung.operator.ImageScaling;

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
public interface Operator_ImageScaling extends Operator<Data_Image>
{
    /**
     * returns to a given int the rgb values in array
     * @param colourNum int value
     * @return [0]=red; [1]=green; [2]=blue
     */
    public default int [] getRGBA (int colourNum)
    {
    	int [] rgba = new int [4];

    	//int to rgb with error correction -> no values over 255 allowed
    	rgba[0] = (colourNum >> 16) & 0xff;		//red
		rgba[1] = (colourNum >> 8) & 0xff;		//green
		rgba[2] = (colourNum) & 0xff;			//blue
		rgba[3] = (colourNum >> 24) & 0xff;		//alpha
    	return rgba;
    }

    /**
     * converts rgba array to int
     * @param rgba array with [0]=red; [1]=green; [2]=blue [3]=alpha
     * @return returns int value with rgba information
     */
    public default int RGBAtoInt (int [] rgba)
    {
    	int rgbaValue=rgba[2];
    	rgbaValue += (rgba[1] << 8);
    	rgbaValue += (rgba[0] << 16);
    	rgbaValue += (rgba[3] << 24);

    	return rgbaValue;
    }
}