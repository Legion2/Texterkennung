package texterkennung;

import debug.Debugger;
import jogl.OpenGLHandler;
import texterkennung.data.DataList;
import texterkennung.data.DataString;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_NPOS;
import texterkennung.operator.Operator;
import texterkennung.operator.OperatorGPU_Farbzuordnung;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_Raster;
import texterkennung.operator.Operator_Zeichenerkennung;
import texterkennung.operator.Operator_Zeichenzuordnung;
import texterkennung.operator.ImageScaling.Operator_BilinearImageScaling;
import texterkennung.operator.ImageScaling.Operator_ImageScaling;

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
		
		//
		Operator_ImageScaling OI = new Operator_BilinearImageScaling(this.originalBild, this.scale);
		if (!this.isrunning()) return;
		OI.run();
		Data_ID scaledImage = (Data_ID) OI.getData();
		Debugger.info(this, "Skalierung fertig");
		
		//Markiert die Pixel, die die richtige Farbe haben.
		Operator OF;
		if (this.gpu) OF = new OperatorGPU_Farbzuordnung(scaledImage, this.farbListe, this.schwellwert, this.openGLHandler.getGL4());
		else OF = new Operator_Farbzuordnung(scaledImage, this.farbListe, this.schwellwert);
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
		
		/*//
		Operator_Verbindungen OV = new Operator_Verbindungen(markiertePixel);
		if (!this.isrunning()) return;
		OV.run();
		Data_ID verbundenePixel = (Data_ID) OV.getData();*/
		
		/*//Konvertiert die markiertenZeichen Daten in das NPOS format
		Operator OI;
		if (this.gpu) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.openGLHandler.getGL4());
		else OI = new Operator_IDtoNPOS(markierteZeichen);
		if (!this.isrunning()) return;
		OI.run();
		Data_NPOS data_NPOS = (Data_NPOS) OI.getData();
		Debugger.info(this, "Data konvertieren fertig");*/
		
		//Markiert die Pixel, die zu einem Zeichen gehören.
		Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(data_F, sektorenRaster, this.schwarzweiß);
		if (!this.isrunning()) return;
		OZ.run();
		DataList dataList2 = (DataList) OZ.getData();
		Data_ID markierteZeichen = (Data_ID) dataList2.get(0);
		DataList zeichenListe = (DataList) dataList2.get(1);
		Debugger.info(this, "Zeichenzuordung fertig");
		
		//Erkennt die Zeichen
		Operator_Zeichenerkennung OZE = new Operator_Zeichenerkennung(this.generierteZeichenliste, zeichenListe);
		if (!this.isrunning()) return;
		OZE.run();
		DataString dataString = (DataString) OZE.getData();
		dataString.setFont(this.font);
		Debugger.info(this, "FERTIG!!!");
	}
	
	/**
	 * 
	 * @return Schwarzweiß GPU Schriftart Schwellwert Farben
	 */
	public static String getConfigPreset()
	{
		return "false;true;Arial;400;0;100";
	}
}