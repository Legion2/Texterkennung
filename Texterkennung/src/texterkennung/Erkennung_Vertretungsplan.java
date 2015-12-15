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
import texterkennung.operator.Operator_Bildteilen;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_Raster;
import texterkennung.operator.Operator_Zeichenerkennung;
import texterkennung.operator.Operator_Zeichenzuordnung;
import texterkennung.operator.ImageScaling.Operator_BilinearImageScaling;
import texterkennung.operator.ImageScaling.Operator_ImageScaling;

public class Erkennung_Vertretungsplan extends Erkennung
{
	public Erkennung_Vertretungsplan(Data_Image data_Image, OpenGLHandler openGLHandler, String parameter)
	{
		super(data_Image, openGLHandler, parameter);
		this.setName("Vertretungsplanerkennung");
	}

	@Override
	public void run()
	{
		super.run();
		if (!this.isrunning()) return;
		
		//
		Operator_ImageScaling OI = new Operator_BilinearImageScaling(this.originalBild, this.scale);
		if (!this.isrunning()) return;
		OI.run();
		Data_ID scaledImage = (Data_ID) OI.getData();
		Debugger.info(this, "Skalierung fertig");
		
		//Teilt den Vertretungsplan in seine zwei hälften
		Operator_Bildteilen OB = new Operator_Bildteilen(scaledImage);
		if (!this.isrunning()) return;
		OB.run();
		DataList dataList0 = (DataList) OB.getData();
		
		for (int i = 0; i < dataList0.size(); i++)
		{
			Data_ID teilBild = (Data_ID) dataList0.get(i);
			
			//Markiert die Pixel, die die richtige Farbe haben.
			Operator OF;
			if (this.gpu) OF = new OperatorGPU_Farbzuordnung(teilBild, farbListe, schwellwert, this.openGLHandler.getGL4());
			else OF = new Operator_Farbzuordnung(teilBild, farbListe, schwellwert);
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
			
			//Markiert die Pixel, die zu einem Zeichen gehören.
			Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(data_F, sektorenRaster, this.schwarzweiß);
			if (!this.isrunning()) return;
			OZ.run();
			DataList dataList2 = (DataList) OZ.getData();
			Data_ID markierteZeichen = (Data_ID) dataList2.get(0);
			DataList zeichenListe = (DataList) dataList2.get(1);
			Debugger.info(this, "Zeichenzuordung fertig");
			
			/*//Konvertiert die markiertenZeichen Daten in das NPOS format
			Operator OI;
			if (this.gpu()) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.gl4);
			else OI = new Operator_IDtoNPOS(markierteZeichen);
			if (!this.isrunning()) return;
			OI.run();
			Data_NPOS data_NPOS = (Data_NPOS) OI.getData();
			Debugger.info(this, "Data konvertieren fertig");*/
			
			//Erkennt die Zeichen
			Operator_Zeichenerkennung OZE = new Operator_Zeichenerkennung(this.generierteZeichenliste, zeichenListe);
			if (!this.isrunning()) return;
			OZE.run();
			OZE.getData();
		}
		Debugger.info(this, "FERTIG!!!");
	}

	/**
	 *
	 * @return Schwarzweiß GPU Schriftart Schwellwert Farben
	 */
	public static String getConfigPreset()
	{
		return "false;true;Arial;200;0:16711680;200";
	}
}
