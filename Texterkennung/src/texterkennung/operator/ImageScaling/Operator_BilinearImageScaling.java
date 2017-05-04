
package texterkennung.operator.ImageScaling;

import GUI.GUI;
import debug.Debugger;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;

/**
 * @author Fabio Schmidberger
 *
 */
public class Operator_BilinearImageScaling implements Operator_ImageScaling
{
	private Data_ID originalImage;
	private float scaleFaktor;
	private Data_Image scaledImage;

	/**
	 * @param originalImage Image that should be scaled
     * @param scaleFaktor
     * @
	 */
	public Operator_BilinearImageScaling(Data_ID originalImage, float scaleFaktor)
	{
		this.originalImage = originalImage;
        this.scaleFaktor = scaleFaktor;
        this.scaledImage = new Data_Image((int) (originalImage.getXlenght() * scaleFaktor), (int) (originalImage.getYlenght() * scaleFaktor), "scaledImage", true);
	}

	@Override
	public Data_Image get()
	{
		Debugger.info(this, "Skalierung: " + this.scaleFaktor);

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
		
		GUI.MainGUI.setTab(this.scaledImage);
		return this.scaledImage;
	}

	@Override
	public String getName()
	{
		return "BilinearImageScaling";
	}
}
