
package texterkennung.operator.ImageScaling;

import GUI.GuiElements;
import texterkennung.data.Data_ID;

/**
 * @author Fabio Schmidberger
 *
 */
public class Operator_BilinearImageScaling extends Operator_ImageScaling {

	/**
	 * @param originalImage
	 * @param scaleFaktor
	 */
	public Operator_BilinearImageScaling(Data_ID originalImage, float scaleFaktor) {
		super(originalImage, scaleFaktor);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see texterkennung.operator.ImageScaling.IData2dScalar#scale()
	 */

	@Override
	public Data_ID scale() {

		int newXLength= (int) (originalImage.getXlenght()*scaleFaktor);
		int newYLength= (int) (originalImage.getYlenght()*scaleFaktor);

		Data_ID newImage = new Data_ID(newXLength, newYLength, getName(), true);


		/*
		 * Bilinear Interpolation
		 *
		 * search for the unknown value f(x,y)
		 *
		 * the values at f(x1, y1), f(x1, y2), f(x2, y1), f(x2, y2) are known
		 *
		 *
		 * x1<x<x2
		 * y1<y<y2
		 *
		 * https://en.wikipedia.org/wiki/Bilinear_interpolation
		 */


		//iterate over all xPos
		for (int xPos=0; xPos<newXLength; xPos++) {

			//iterate over yPos
			for (int yPos=0; yPos<newYLength; yPos++) {

				//current x-Pos relative to the originalImage
				float x = xPos/scaleFaktor;

				//closest X-Pos of pixel in Original Image (left)
				float x1 = (int) (x);

				//closest X-Pos of pixel in Original Image (right)
				float x2 = (int) (x+1);


				//current y-Pos relative to the originalImage
				float y = yPos/scaleFaktor;

				//closest y-Pos of pixel in Original Image (top)
				float y2 = (int) (y);

				//closest y-Pos of pixel in Original Image (botton)
				float y1 = (int) (y+1);


				//known Points
				float Q11=originalImage.getInt( (int) x1	, (int) y1);
				float Q12=originalImage.getInt( (int) x1	, (int) y2);
				float Q21=originalImage.getInt( (int) x2	, (int) y1);
				float Q22=originalImage.getInt( (int) x2	, (int) y2);


				//linear Interpolation in x-direction
				float R1 = ((x2 - x)/(x2 - x1))*Q11 + ((x - x1)/(x2 - x1))*Q21;
				float R2 = ((x2 - x)/(x2 - x1))*Q12 + ((x - x1)/(x2 - x1))*Q22;

				float P = ((y2 - y)/(y2 - y1))*R1 + ((y - y1)/(y2 - y1))*R2;

				newImage.setInt(xPos, yPos, (int)(P));

			}
		}

		return newImage;

	}



	/* (non-Javadoc)
	 * @see texterkennung.operator.Operator#run()
	 */
	@Override
	public void run()
	{
		scaledImage = scale();

		GuiElements.MainGUI.setTab(this.scaledImage);
	}
}
