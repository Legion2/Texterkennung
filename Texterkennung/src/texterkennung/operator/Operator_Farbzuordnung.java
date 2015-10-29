package texterkennung.operator;

import java.util.ArrayList;

import advanced.ABufferedImage;
import advanced.AColor;
import texterkennung.data.Data;
import texterkennung.data.Data_int;

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
				if (i != farbListe.size())
				{
					gruppen[x][y] = i;
				}
			}
		}
	}
	
	@Override
	public Data getData()
	{
		return new Data_int(this.originalBild.getWidth(), this.originalBild.getHeight(), this.gruppen);
	}
}
