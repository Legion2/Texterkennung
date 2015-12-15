package texterkennung.operator;

import java.util.ArrayList;

import GUI.GUI;
import advanced.AColor;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;

/**
 * Sortiert Hintergrundfarben aus
 * 
 * @author Leon
 *
 */
public class Operator_Farbzuordnung extends Operator
{
	private final Data_ID data_ID;
	private final Data_F data_F;
	
	private final Data_ID data_Image;
	private final ArrayList<AColor> farbListe;
	private final int schwellwert;
	
	public Operator_Farbzuordnung(Data_ID data_Image, ArrayList<AColor> farbListe, int schwellwert)
	{
		this.data_Image = data_Image;
		this.farbListe = farbListe;
		this.schwellwert = schwellwert;
		this.data_ID = new Data_ID(data_Image, "Data-Farbzuordnung", true);
		this.data_F = new Data_F(data_Image, "Data-Farbübereinstimmung", true);
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
				float f = 1.0f;
				
				for (int i = 0; i < farbListe.size(); i++)
				{
					float f0 = farbListe.get(i).fColor(this.data_Image.getInt(x, y), this.schwellwert);
					if (f0 < f)
					{
						f = f0;
						this.data_ID.setInt(x, y, i);
					}
				}
				
				this.data_F.setFloat(x, y, f);
			}
		}
		
		GUI.MainGUI.setTab(this.data_ID);
		GUI.MainGUI.setTab(this.data_F);
	}
	
	

	@Override
	public Data getData()
	{
		DataList list = new DataList("return list", false);
		list.add(this.data_ID);
		list.add(this.data_F);
		return list;
	}
}
