package texterkennung;

import debug.Debugger;
import jogl.OpenGLHandler;
import texterkennung.data.DataList;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_NPOS;
import texterkennung.operator.Operator;
import texterkennung.operator.OperatorGPU_Farbzuordnung;
import texterkennung.operator.OperatorGPU_IDtoNPOS;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_IDtoNPOS;
import texterkennung.operator.Operator_Raster;
import texterkennung.operator.Operator_Verbindungen;
import texterkennung.operator.Operator_Zeichenerkennung;
import texterkennung.operator.Operator_Zeichengenerieren;
import texterkennung.operator.Operator_Zeichenzuordnung;

public class Erkennung_Text extends Erkennung
{
	public Erkennung_Text(Data_Image data_Image, OpenGLHandler openGLHandler, String parameter) {
		super(data_Image, openGLHandler, parameter);
		this.setName("Texterkennung");
	}

	@Override
	public void run()
	{
		super.run();
		
		if (!this.isrunning()) return;
		
		//Markiert die Pixel, die die richtige Farbe haben.
		Operator OF;
		if (this.gpu) OF = new OperatorGPU_Farbzuordnung(this.originalBild, this.farbListe, this.schwellwert, this.openGLHandler.getGL4());
		else OF = new Operator_Farbzuordnung(this.originalBild, this.farbListe, this.schwellwert);
		if (!this.isrunning()) return;
		OF.run();
		DataList dataList = (DataList) OF.getData();
		Data_ID markiertePixel = (Data_ID) dataList.get(0);
		Data_F data_F = (Data_F) dataList.get(1);
		Debugger.info(this, "Farbzuordnung fertig");
		
		//Unterteilt das Bild in Sektoren, in dene jeweils ein Zeichen ist
		Operator_Raster OR = new Operator_Raster(markiertePixel);
		if (!this.isrunning()) return;
		OR.run();
		Data_NPOS sektorenRaster = (Data_NPOS) OR.getData();
		Debugger.info(this, "Raster fertig");
		
		Operator_Verbindungen OV = new Operator_Verbindungen(markiertePixel);
		OV.run();
		Data_ID verbundenePixel = (Data_ID) OV.getData();
		
		/*//Konvertiert die markiertenZeichen Daten in das NPOS format
		Operator OI;
		if (this.gpu) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.openGLHandler.getGL4());
		else OI = new Operator_IDtoNPOS(markierteZeichen);
		if (!this.isrunning()) return;
		OI.run();
		Data_NPOS data_NPOS = (Data_NPOS) OI.getData();
		Debugger.info(this, "Data konvertieren fertig");*/
		
		//Markiert die Pixel, die zu einem Zeichen gehˆren.
		Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(data_F, sektorenRaster, this.schwarzweiﬂ);
		if (!this.isrunning()) return;
		OZ.run();
		DataList dataList2 = (DataList) OZ.getData();
		Data_ID markierteZeichen = (Data_ID) dataList2.get(0);
		DataList zeichenListe = (DataList) dataList2.get(1);
		Debugger.info(this, "Zeichenzuordung fertig");
		
		//Generiert den standart Zeichensatz um diese mit den im Bild vorkommenden zu vergleichen
		Operator_Zeichengenerieren OZG = new Operator_Zeichengenerieren(standartZeichen, this.font, this.schwarzweiﬂ);
		if (!this.isrunning()) return;
		OZG.run();
		DataList generierteZeichenliste = (DataList) OZG.getData();
		Debugger.info(this, "Zeichengenerieren fertig");
		
		//Erkennt die Zeichen
		Operator_Zeichenerkennung OZE = new Operator_Zeichenerkennung(generierteZeichenliste, zeichenListe);
		if (!this.isrunning()) return;
		OZE.run();
		OZE.getData();
		Debugger.info(this, "FERTIG!!!");
	}
	
	/**
	 * 
	 * @return Schwarzweiﬂ GPU Schriftart Schwellwert Farben
	 */
	public static String getConfigPreset()
	{
		return "true;true;Arial;200;0;100";
	}
}