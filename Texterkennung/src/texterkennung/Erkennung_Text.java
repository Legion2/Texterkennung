package texterkennung;

import java.util.concurrent.CompletableFuture;

import debug.Debugger;
import jogl.OpenGLHandler;
import texterkennung.data.Data;
import texterkennung.data.Data2D;
import texterkennung.data.DataList;
import texterkennung.data.DataString;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_NPOS;
import texterkennung.data.Data_Zeichen;
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
		
		//Skalieren
		Operator_ImageScaling OI = new Operator_BilinearImageScaling(this.originalBild, this.scale);
		CompletableFuture<Data_Image> completableFuture = CompletableFuture.supplyAsync(OI);
		
		//Data_ID scaledImage = (Data_ID) OI.get();
		//Debugger.info(this, "Skalierung fertig");
		
		if (!this.isrunning()) return;
		//Markiert die Pixel, die die richtige Farbe haben.
		
		CompletableFuture<DataList<Data2D>> completableFuture2 = completableFuture.thenApply((Data_Image scaledImage) -> {
			Operator<DataList<Data2D>> OF;
			if (this.gpu) OF = new OperatorGPU_Farbzuordnung(scaledImage, this.farbListe, this.schwellwert, this.openGLHandler.getGL4());
			else OF = new Operator_Farbzuordnung(scaledImage, this.farbListe, this.schwellwert);
			
			return (DataList<Data2D>) OF.get();
		});
		
		Data_ID markiertePixel = (Data_ID) completableFuture2.join().get(0);
		Data_F data_F = (Data_F) completableFuture2.join().get(1);
		Debugger.info(this, "Farbzuordnung fertig");
		
		//Unterteilt das Bild in Sektoren, in dene jeweils ein Zeichen ist
		
		Operator_Raster OR = new Operator_Raster(markiertePixel, data_F);
		if (!this.isrunning()) return;
		Data_NPOS sektorenRaster = OR.get();
		Debugger.info(this, "Raster fertig");
		
		/*//
		Operator_Verbindungen OV = new Operator_Verbindungen(markiertePixel);
		if (!this.isrunning()) return;
		Data_ID verbundenePixel = (Data_ID) OV.getData();*/
		
		/*//Konvertiert die markiertenZeichen Daten in das NPOS format
		Operator OI;
		if (this.gpu) OI = new OperatorGPU_IDtoNPOS(markierteZeichen, this.openGLHandler.getGL4());
		else OI = new Operator_IDtoNPOS(markierteZeichen);
		if (!this.isrunning()) return;
		Data_NPOS data_NPOS = (Data_NPOS) OI.getData();
		Debugger.info(this, "Data konvertieren fertig");*/
		
		//Markiert die Pixel, die zu einem Zeichen gehören.
		Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(data_F, sektorenRaster, this.schwarzweiß);
		if (!this.isrunning()) return;
		DataList<Data> dataList2 = OZ.get();
		Data_ID markierteZeichen = (Data_ID) dataList2.get(0);
		DataList<DataList<DataList<Data_Zeichen>>> zeichenListe = (DataList<DataList<DataList<Data_Zeichen>>>) dataList2.get(1);
		Debugger.info(this, "Zeichenzuordung fertig");
		
		//Erkennt die Zeichen
		Operator_Zeichenerkennung OZE = new Operator_Zeichenerkennung((DataList<Data_Zeichen>) this.generierteZeichenliste.join(), zeichenListe);
		if (!this.isrunning()) return;
		DataString dataString = (DataString) OZE.get();
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