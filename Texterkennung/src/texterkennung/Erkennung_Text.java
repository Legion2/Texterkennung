package texterkennung;

import java.awt.Font;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import advanced.AColor;
import debug.Debugger;
import texterkennung.data.DataList;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_NPOS;
import texterkennung.operator.Operator;
import texterkennung.operator.OperatorGPU_Farbzuordnung;
import texterkennung.operator.OperatorGPU_IDtoNPOS;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_IDtoNPOS;
import texterkennung.operator.Operator_Raster;
import texterkennung.operator.Operator_Zeichenerkennung;
import texterkennung.operator.Operator_Zeichengenerieren;
import texterkennung.operator.Operator_Zeichenzuordnung;

public class Erkennung_Text extends Erkennung
{
	public Erkennung_Text(Data_Image data_Image, ArrayList<AColor> farbListe, Font font, boolean schwarzweiﬂ, GL4 gl4)
	{
		super(data_Image, farbListe, font, schwarzweiﬂ, gl4);
		this.setName("Texterkennung");
	}

	@Override
	public void run()
	{
		super.run();
		
		if (!this.isrunning()) return;
		int schwellwert = 200;
		
		//Markiert die Pixel, die die richtige Farbe haben.
		Operator OF;
		if (this.gpu()) OF = new OperatorGPU_Farbzuordnung(originalBild, farbListe, schwellwert, this.gl4);
		else OF = new Operator_Farbzuordnung(originalBild, farbListe, schwellwert);
		if (!this.isrunning()) return;
		OF.run();
		Data_ID markiertePixel = (Data_ID) OF.getData();
		Debugger.info(this, "Farbzuordnung fertig");
		
		//Unterteilt das Bild in Sektoren, in dene jeweils ein Zeichen ist
		Operator_Raster OR = new Operator_Raster(markiertePixel);
		if (!this.isrunning()) return;
		OR.run();
		Data_NPOS sektorenRaster = (Data_NPOS) OR.getData();
		Debugger.info(this, "Raster fertig");
		
		//Markiert die Pixel, die zu einem Zeichen gehˆren.
		Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(markiertePixel, sektorenRaster, schwarzweiﬂ);
		if (!this.isrunning()) return;
		OZ.run();
		DataList dataList = (DataList) OZ.getData();
		Data_ID markierteZeichen = (Data_ID) dataList.get(0);
		DataList zeichenListe = (DataList) dataList.get(1);
		Debugger.info(this, "Zeichenzuordung fertig");
		
		//Konvertiert die markiertenZeichen Daten in das NPOS format
		Operator OI;
		if (this.gpu()) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.gl4);
		else OI = new Operator_IDtoNPOS(markierteZeichen);
		if (!this.isrunning()) return;
		OI.run();
		Data_NPOS data_NPOS = (Data_NPOS) OI.getData();
		Debugger.info(this, "Data konvertieren fertig");
		
		//Generiert den standart Zeichensatz um diese mit den im Bild vorkommenden zu vergleichen
		Operator_Zeichengenerieren OZG = new Operator_Zeichengenerieren(standartZeichen, this.font);
		if (!this.isrunning()) return;
		OZG.run();
		DataList generierteZeichenliste = (DataList) OZG.getData();
		Debugger.info(this, "Zeichengenerieren fertig");
		
		//Erkennt die Zeichen
		Operator_Zeichenerkennung OZE = new Operator_Zeichenerkennung(generierteZeichenliste, zeichenListe, data_NPOS);
		if (!this.isrunning()) return;
		OZE.run();
		OZE.getData();
		Debugger.info(this, "FERTIG!!!");
	}
}
