package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import advanced.AColor;
import debug.Debugger;
import texterkennung.data.DataList;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;
import texterkennung.operator.Operator;
import texterkennung.operator.OperatorGPU_Farbzuordnung;
import texterkennung.operator.OperatorGPU_IDtoNPOS;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_IDtoNPOS;
import texterkennung.operator.Operator_Raster;
import texterkennung.operator.Operator_Verbindungen;
import texterkennung.operator.Operator_Zeichenzuordnung;

public class Erkennung_Text extends Erkennung
{

	public Erkennung_Text(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font, GL4 gl4)
	{
		super(bufferedImage, farbListe, font, gl4);
		setName("Text");
	}

	@Override
	public void run()
	{
		super.run();
		if (!this.isrunning()) return;
		Debugger.info(this, "run");
		int schwellwert = 200;
		int vergleichsID = 0;
		Debugger.info(this, "Start");
		
		//Markiert die Pixel, die die richtige Farbe haben.
		Operator OF;
		if (this.gpu()) OF = new OperatorGPU_Farbzuordnung(originalBild, farbListe, schwellwert, this.gl4);
		else OF = new Operator_Farbzuordnung(originalBild, farbListe, schwellwert);
		if (!this.isrunning()) return;
		OF.run();
		Data_ID markiertePixel = (Data_ID) OF.getData();
		Debugger.info(this, "Farbzuordnung fertig");
		
		
		/*
		Operator_Verbindungen OV = new Operator_Verbindungen((Data_ID) OF.getData());
		if (!this.isrunning()) return;
		OV.run();
		Data_ID data = (Data_ID) OV.getData();
		Debugger.info(this, "Verbindungen fertig");
		*/
		
		
		
		//Unterteilt das Bild in Sektoren, in dene jeweils ein Zeichen ist
		Operator_Raster OR = new Operator_Raster(markiertePixel, vergleichsID);
		if (!this.isrunning()) return;
		OR.run();
		Data_NPOS sektorenRaster = (Data_NPOS) OR.getData();
		Debugger.info(this, "Raster fertig");
		
		//Markiert die Pixel, die zu einem Zeichen gehören.
		Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(markiertePixel, sektorenRaster);
		if (!this.isrunning()) return;
		OZ.run();
		DataList dataList = (DataList) OZ.getData();
		Data_ID markierteZeichen = (Data_ID) dataList.get(0);
		DataList zeichenListe = (DataList) dataList.get(1);
		Debugger.info(this, "fertig");
		
		
		Operator OI;
		if (this.gpu()) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.gl4);
		else OI = new Operator_IDtoNPOS(markierteZeichen);
		if (!this.isrunning()) return;
		OI.run();
		Debugger.info(this, "fertig Data konvertieren");
		
		
		// TODO nichtfertig
	}
}
