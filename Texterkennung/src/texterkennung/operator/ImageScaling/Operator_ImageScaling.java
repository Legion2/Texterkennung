package texterkennung.operator.ImageScaling;

import texterkennung.data.Data;
import texterkennung.data.Data_ID;
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
		this.originalImage=originalImage;
		this.scaleFaktor=scaleFaktor;
	}
	
	abstract public Data_ID scale ();

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
