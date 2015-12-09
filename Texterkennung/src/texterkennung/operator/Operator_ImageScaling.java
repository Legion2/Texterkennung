package texterkennung.operator;

import texterkennung.data.Data;
import texterkennung.data.Data_ID;

/**
 *
 * @author Fabio Schmidberger
 *
 *	Scales a given Data_ID Object
 *	The scaled Data_ID Object is saved as scaledImage
 *	This Image can be accessed through the method getData()
 *
 */
public class Operator_ImageScaling extends Operator
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


	@Override
	/**
	 * @return Name of the Class
	 */
	public String getName()
	{
		return "Operator_ImageScaling";
	}

	
	/**
	 * 
	 * 
	 *  	  A    --\ 1 2
	 *		C P B  --/ 3 4
  	 *		  D 
 	 *	1=P; 2=P; 3=P; 4=P;
 	 * 	
 	 *	IF A==C => 1=A
 	 *	IF A==B => 2=B
 	 *	IF B==D => 4=D
 	 * 	IF D==C => 3=C
 	 *	IF of A, B, C, D, three or more are identical: 1=2=3=4=P
	 * 
	 * http://tech-algorithm.com/articles/bilinear-image-scaling/
	 * 
	 * 
	 */
	
	
	@Override
	public void run()
	{

		int originalX=originalImage.getXlenght();
		int originalY=originalImage.getYlenght();

		int scaledX= (int) (originalX*scaleFaktor);
		int scaledY= (int) (originalY*scaleFaktor);

		//generate new Data_ID Object, with scaled Data Array
		Data_ID scaledImage = new Data_ID(scaledX, scaledY, getName());


		//Initialize the Data Array of scaled Image

		//iterate over the x-Direction
		for (int currentXpos=0; currentXpos < originalX; currentXpos++)
		{

			//iterate over y-Direction
			for (int currentYpos = 0; currentYpos < originalY; currentYpos++)
			{

				//Compare Java-Doc entry for names
				int pixelP=originalImage.getInt(currentXpos, currentYpos);
				
				int pixelA=originalImage.getInt(currentXpos, currentYpos-1);
				int pixelB=originalImage.getInt(currentXpos+1, currentYpos);
				int pixelC=originalImage.getInt(currentXpos-1, currentYpos);
				int pixelD=originalImage.getInt(currentXpos, currentYpos+1);
				
				if (pixelA==pixelC) 
				{
					
				}
				
				if (pixelA==pixelB) 
				{
					
				}
				
				if (pixelA==pixelC) 
				{
					
				}
				
				if (pixelB==pixelD) 
				{
					
				}
				
				if (pixelD==pixelC) 
				{
					
				}
					
					
				
				
			}


		};



	}


	@Override
	/**
	 * @return returns the scaled Image
	 */
	public Data getData()
	{

		return scaledImage;
	}

}
