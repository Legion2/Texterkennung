package texterkennung.operator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import advanced.ABufferedImage;
import advanced.AColor;

/**
 * Sortiert Hintergrundfarben aus
 * 
 * Kann Parallelisiert werden
 * 
 * @author Leon
 *
 */
public class Operator_Farbzuordnung extends Operator
{
	private int[][] gruppen;
	
	private final ABufferedImage originalBild;
	private final ArrayList<AColor> farbListe;
	private final int schwellwert;
	
	public Operator_Farbzuordnung(ABufferedImage originalBild, ArrayList<AColor> farbListe, int schwellwert)
	{
		this.originalBild = originalBild;
		this.farbListe = farbListe;
		this.schwellwert = schwellwert;
	}

	@Override
	public BufferedImage visualisieren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		return "Operator_Farbzuordnung";
	}

	@Override
	public void run()
	{
		for (int y = 0; y < this.originalBild.getHeight(); y++)
		{
			for (int x = 0; x < this.originalBild.getWidth(); x++)
			{
				int i = 0;
				while (i < farbListe.size() && !farbListe.get(i).isColor(this.originalBild.getRGB(x, y), schwellwert))
				{
					i++;
				}
			}
		}
	}

}
