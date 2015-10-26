package texterkennung.operator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import advanced.ABufferedImage;
import advanced.AColor;

public class Operator_Verbindungen extends Operator
{
	private int[][] gruppen;
	
	
	
	public Operator_Verbindungen(ABufferedImage originalBild, ArrayList<AColor> farbListe)
	{
		//this.originalBild = originalBild;
		//this.farbListe = farbListe;
	}
	
	
	@Override
	public String getName()
	{
		return "Verbindungen";
	}

	@Override
	public BufferedImage visualisieren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run()
	{
		/*this.gruppen = new int[this.originalBild.getWidth()][this.originalBild.getHeight()];
		this.gruppen[0][0] = 0;
		
		for (int x = 1; x < this.originalBild.getWidth(); x++)
		{
			
		}
		
		for (int y = 1; y < this.originalBild.getHeight(); y++)
		{
			for (int x = 1; x < this.originalBild.getWidth(); x++)
			{
				
			}
		}*/
	}
}
