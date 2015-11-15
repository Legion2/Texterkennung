package texterkennung.operator;

import GUI.GuiElements;
import debug.Debugger;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_NPOS;
import texterkennung.data.Data_Zeichen;

public class Operator_Zeichenerkennung extends Operator
{
	private final DataList vzeichenListe;
	private final DataList zeichenListe;
	private final Data_NPOS data_NPOS;
	private String erkanntertext;
	
	public Operator_Zeichenerkennung(DataList vzeichenListe, DataList zeichenListe, Data_NPOS data_NPOS)
	{
		this.vzeichenListe = vzeichenListe;
		this.zeichenListe = zeichenListe;
		this.data_NPOS = data_NPOS;
		this.erkanntertext = "";
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichenerkennung";
	}

	@Override
	public void run()
	{
		for (int i = 0; i < this.zeichenListe.size(); i++)
		{
			Data_Zeichen zeichen = (Data_Zeichen) this.zeichenListe.get(i);
			
			float[] array = new float[this.vzeichenListe.size()];
			
			int best = 0;
			
			for (int j = 0; j < array.length; j++)
			{
				Data_Zeichen data_Zeichen = ((Data_Zeichen) this.vzeichenListe.get(j));
				
				array[j] = (float) data_Zeichen.vergleichenmit(zeichen);
				//Debugger.info(this, "Zeichen: " + data_Zeichen.getchar() + " Übereinstimmung: " + array[j]);
				best = array[best] > array[j] ? j : best;
			}
			Data_Zeichen data_Zeichen = (Data_Zeichen) this.vzeichenListe.get(best);
			
			Debugger.info(this, "Beste Übereinstimmung: " + array[best] + " " + data_Zeichen.getchar());
			
			
			this.erkanntertext += data_Zeichen.getchar();
			
			zeichen.setchar(data_Zeichen.getchar());
		}
		Debugger.info(this, "Erkanntertext: " + this.erkanntertext);
		GuiElements.MainGUI.setTab(this.zeichenListe);
	}

	@Override
	public Data getData()
	{
		return this.zeichenListe;
	}
}
