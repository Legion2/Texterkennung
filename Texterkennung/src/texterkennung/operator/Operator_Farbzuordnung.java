package texterkennung.operator;

import java.util.ArrayList;

import GUI.GuiElements;
import advanced.AColor;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;

/**
 * Sortiert Hintergrundfarben aus
 * 
 * @author Leon
 *
 */
public class Operator_Farbzuordnung extends Operator
{
	private Data_ID data_ID;
	
	private final Data_Image data_Image;
	private final ArrayList<AColor> farbListe;
	private final int schwellwert;
	
	public Operator_Farbzuordnung(Data_Image data_Image, ArrayList<AColor> farbListe, int schwellwert)
	{
		this.data_Image = data_Image;
		this.farbListe = farbListe;
		this.schwellwert = schwellwert;
		this.data_ID = new Data_ID(this.data_Image.getXlenght(), this.data_Image.getYlenght(), "Data-Farbzuordnung");
	}
	
	public Operator_Farbzuordnung(Data_Image data_Image, ArrayList<AColor> farbListe)
	{
		this.data_Image = data_Image;
		this.farbListe = farbListe;
		this.schwellwert = -1;
		this.data_ID = new Data_ID(this.data_Image.getXlenght(), this.data_Image.getYlenght(), "Data-Farbzuordnung");
	}
	
	@Override
	public String getName()
	{
		return "Operator_Farbzuordnung";
	}

	@Override
	public void run()
	{
		for (int y = 0; y < this.data_Image.getYlenght(); y++)
		{
			for (int x = 0; x < this.data_Image.getXlenght(); x++)
			{
				int i = 0;
				while (i < farbListe.size() && !farbListe.get(i).isColor(this.data_Image.getInt(x, y), schwellwert))
				{
					i++;
				}
				if (i != farbListe.size())
				{
					this.data_ID.setInt(x, y, i);
				}
				else
				{
					this.data_ID.setInt(x, y, this.data_ID.getDefault());
				}
			}
		}
		
		GuiElements.MainGUI.setTab(this.data_ID);
	}
	
	

	@Override
	public Data getData()
	{
		return this.data_ID;
	}
}
