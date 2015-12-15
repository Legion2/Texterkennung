
package texterkennung.operator.ImageScaling;

import GUI.GuiElements;
import debug.Debugger;
import texterkennung.data.Data_ID;

/**
 * @author Fabio Schmidberger
 *
 */
public class Operator_BilinearImageScaling extends Operator_ImageScaling
{
	/**
	 * @param originalImage
	 * @param scaleFaktor
	 */
	public Operator_BilinearImageScaling(Data_ID originalImage, float scaleFaktor)
	{
		super(originalImage, scaleFaktor);
	}

	@Override
	public void scale()
	{

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
		for (int xPos=0; xPos < this.scaledImage.getXlenght(); xPos++) {

			//iterate over yPos
			for (int yPos=0; yPos<this.scaledImage.getYlenght(); yPos++) {

				//current x-Pos relative to the originalImage
				float x = xPos / this.scaleFaktor;

				//closest X-Pos of pixel in Original Image (left)
				float x1 = (int) (x);

				//closest X-Pos of pixel in Original Image (right)
				float x2 = (int) (x+1);


				//current y-Pos relative to the originalImage
				float y = yPos / this.scaleFaktor;

				//closest y-Pos of pixel in Original Image (top)
				float y2 = (int) (y);

				//closest y-Pos of pixel in Original Image (botton)
				float y1 = (int) (y+1);



				//known Points with rgb values
				int [] Q11 = getRGBA(originalImage.getInt( (int) x1	, (int) y1));
				int [] Q12 = getRGBA(originalImage.getInt( (int) x1	, (int) y2));
				int [] Q21 = getRGBA(originalImage.getInt( (int) x2	, (int) y1));
				int [] Q22 = getRGBA(originalImage.getInt( (int) x2	, (int) y2));


				/**
				 * Colour array with [0] = red; [1] = green; [2] = blue; [3]= alpha
				 */
				int [] P_rgba = new int [4];

				for (int colour = 0; colour < P_rgba.length; colour++)
				{
					//linear Interpolation in x-direction
					float R1 = ((x2 - x)/(x2 - x1))*Q11[colour] + ((x - x1)/(x2 - x1)) * Q21[colour];
					float R2 = ((x2 - x)/(x2 - x1))*Q12[colour] + ((x - x1)/(x2 - x1)) * Q22[colour];

					//linear Interpolation in y-direction
					float P = ((y2 - y)/(y2 - y1)) * R1 + ((y - y1)/(y2 - y1)) * R2;

					P_rgba[colour] = (int) P;
				}
				this.scaledImage.setInt(xPos, yPos, this.RGBAtoInt(P_rgba));
			}
		}
	}

	@Override
	public void run()
	{
		Debugger.info(this, "Skalierung: " + this.scaleFaktor);
		scale();

		GuiElements.MainGUI.setTab(this.scaledImage);
	}
}
