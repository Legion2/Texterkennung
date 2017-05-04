package texterkennung;

import debug.Debugger;
import jogl.OpenGLHandler;
import texterkennung.data.Data;
import texterkennung.data.Data2D;
import texterkennung.data.DataList;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_NPOS;
import texterkennung.data.Data_Zeichen;
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
		Data_ID scaledImage = (Data_ID) OI.get();
		Debugger.info(this, "Skalierung fertig");
		
		//Teilt den Vertretungsplan in seine zwei hälften
		Operator_Bildteilen OB = new Operator_Bildteilen(scaledImage);
		if (!this.isrunning()) return;
		DataList<Data_ID> dataList0 = OB.get();
		
		for (int i = 0; i < dataList0.size(); i++)
		{
			Data_ID teilBild = (Data_ID) dataList0.get(i);
			
			//Markiert die Pixel, die die richtige Farbe haben.
			Operator<DataList<Data2D>> OF;
			if (this.gpu) OF = new OperatorGPU_Farbzuordnung(teilBild, farbListe, schwellwert, this.openGLHandler.getGL4());
			else OF = new Operator_Farbzuordnung(teilBild, farbListe, schwellwert);
			if (!this.isrunning()) return;
			DataList<Data2D> dataList = OF.get();
			Data_ID markiertePixel = (Data_ID) dataList.get(0);
			Data_F data_F = (Data_F) dataList.get(1);
			Debugger.info(this, "Farbzuordnung fertig");
			
			//Unterteilt das Bild in Sektoren, in dene jeweils ein Zeichen ist
			Operator_Raster OR = new Operator_Raster(markiertePixel, data_F);
			if (!this.isrunning()) return;
			Data_NPOS sektorenRaster = (Data_NPOS) OR.get();
			Debugger.info(this, "Raster fertig");
			
			//Markiert die Pixel, die zu einem Zeichen gehören.
			Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(data_F, sektorenRaster, this.schwarzweiß);
			if (!this.isrunning()) return;
			DataList<Data> dataList2 = OZ.get();
			//Data_ID markierteZeichen = (Data_ID) dataList2.get(0);
			DataList<DataList<DataList<Data_Zeichen>>> zeichenListe = (DataList<DataList<DataList<Data_Zeichen>>>) dataList2.get(1);
			Debugger.info(this, "Zeichenzuordung fertig");
			
			/*//Konvertiert die markiertenZeichen Daten in das NPOS format
			Operator OI;
			if (this.gpu()) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.gl4);
			else OI = new Operator_IDtoNPOS(markierteZeichen);
			if (!this.isrunning()) return;
			Data_NPOS data_NPOS = (Data_NPOS) OI.getData();
			Debugger.info(this, "Data konvertieren fertig");*/
			
			//Erkennt die Zeichen
			Operator_Zeichenerkennung OZE = new Operator_Zeichenerkennung((DataList<Data_Zeichen>) this.generierteZeichenliste.join(), zeichenListe);
			if (!this.isrunning()) return;
			OZE.get();
		}
		Debugger.info(this, "FERTIG!!!");
	}

	/**
	 *
	 * @return Schwarzweiß GPU Schriftart Schwellwert Farben
	 */
	public static String getConfigPreset()
	{
		return "false;true;Arial;200;0:16711680;100";
	}
}
