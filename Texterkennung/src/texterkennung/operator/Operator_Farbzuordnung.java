package texterkennung.operator;

import java.util.ArrayList;

import advanced.ABufferedImage;
import advanced.AColor;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;

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
	
	@SuppressWarnings("null")
	public Operator_Farbzuordnung(ABufferedImage originalBild, ArrayList<AColor> farbListe)
	{
		this.originalBild = originalBild;
		this.farbListe = farbListe;
		this.schwellwert = (Integer) null;
	}
	
	@Override
	public String getName()
	{
		return "Operator_Farbzuordnung";
	}

	@Override
	public void run()
	{
		//TODO parallelisieren??? m�glich ist es
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
					gruppen[x][y] = i+1;
				}
				else
				{
					gruppen[x][y] = 0;
				}
			}
		}
	}
	
	@Override
	public Data getData()
	{
		return new Data_ID(this.originalBild.getWidth(), this.originalBild.getHeight(), this.gruppen);
	}
}
