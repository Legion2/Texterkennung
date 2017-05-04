package texterkennung.operator;

import GUI.GUI;
import debug.Debugger;
import texterkennung.data.DataList;
import texterkennung.data.DataString;
import texterkennung.data.Data_Zeichen;

public class Operator_Zeichenerkennung implements Operator<DataString>
{
	private final DataList<Data_Zeichen> vzeichenListe;
	private final DataList<DataList<DataList<Data_Zeichen>>> zeichenListe;
	
	private String erkanntertext;
	
	public Operator_Zeichenerkennung(DataList<Data_Zeichen> vzeichenListe, DataList<DataList<DataList<Data_Zeichen>>> zeichenListe)
	{
		this.vzeichenListe = vzeichenListe;
		this.zeichenListe = zeichenListe;
		this.erkanntertext = "";
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichenerkennung";
	}

	@Override
	public DataString get()
	{
		// VList (Zeilen)
		for (int i = 0; i < this.zeichenListe.size(); i++) {
			DataList<DataList<Data_Zeichen>> woerter = this.zeichenListe.get(i);
			// HList (Wörter)
			for (int k = 0; k < woerter.size(); k++) {
				DataList<Data_Zeichen> zeichenListe = woerter.get(k);
				// Wort (Buchstaben)
				for (int l = 0; l < zeichenListe.size(); l++) {
					Data_Zeichen zeichen = (Data_Zeichen) zeichenListe.get(l);

					if (zeichen.getchar() != '\u0000')
						continue;// Wenn Zeichen schon gesetzt

					float[] array = new float[this.vzeichenListe.size()];

					int best = 0;
					Data_Zeichen data_Zeichen;

					for (int j = 0; j < array.length; j++) {
						data_Zeichen = ((Data_Zeichen) this.vzeichenListe.get(j));

						array[j] = (float) data_Zeichen.vergleichenmit(zeichen);
						// Debugger.info(this, "Zeichen: " +
						// data_Zeichen.getchar() + " Übereinstimmung: " +
						// array[j]);
						best = array[best] > array[j] ? j : best;
					}
					data_Zeichen = (Data_Zeichen) this.vzeichenListe.get(best);

					char c = data_Zeichen.getchar();

					Debugger.info(this, "Beste Übereinstimmung: " + array[best] + " " + c);
					this.erkanntertext += c;
					zeichen.setchar(data_Zeichen);
				}
				this.erkanntertext += ' ';
			}
			this.erkanntertext += '\n';
		}

		DataString dataString = new DataString(this.erkanntertext, "erkannter Text", true);
		GUI.MainGUI.setTab(dataString);
		Debugger.info(this, "Erkanntertext: " + this.erkanntertext);
		GUI.MainGUI.setTab(this.zeichenListe);
		
		return dataString;
	}
}
