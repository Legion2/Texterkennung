package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import advanced.AColor;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_Raster;
import texterkennung.operator.Operator_Verbindungen;
import texterkennung.operator.Operator_Zeichenzuordnung;

public class Erkennung_Text extends Erkennung
{

	public Erkennung_Text(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font)
	{
		super(bufferedImage, farbListe, font);
		setName("Originalbild");
	}

	/*@Override
	public String getName()
	{
		return "Erkennung_Text";
	}*/

	@Override
	public void run()
	{
		super.run();
		if (!this.isrunning()) return;
		System.out.println("run");
		int schwellwert = 200;
		int vergleichsID = 0;
		System.out.println("Start");
		Operator_Farbzuordnung OF = new Operator_Farbzuordnung(originalBild, farbListe, schwellwert);
		
		if (!this.isrunning()) return;
		OF.run();
		System.out.println("Farbzuordnung fertig");
		Operator_Verbindungen OV = new Operator_Verbindungen((Data_ID) OF.getData());
		
		if (!this.isrunning()) return;
		OV.run();
		System.out.println("Verbindungen fertig");
		
		if (!this.isrunning()) return;
		Data_ID data_ID = (Data_ID) OV.getData();
		System.out.println("start Data konvertieren");
		
		if (!this.isrunning()) return;
		Data_NPOS data_NPOS = new Data_NPOS(data_ID);
		data_NPOS.setData(data_ID);//Daten umwandeln
		System.out.println("fertig data konvertieren");
		Operator_Raster OR = new Operator_Raster(data_ID, vergleichsID);
		
		if (!this.isrunning()) return;
		OR.run();
		System.out.println("Raster fertig");
		
		Operator_Zeichenzuordnung OZ = new Operator_Zeichenzuordnung(data_NPOS, (Data_NPOS) OR.getData());
		
		if (!this.isrunning()) return;
		OZ.run();
		System.out.println("fertig");
		
		
		/*testBild = new ABufferedImage(this.originalBild.getWidth(), this.originalBild.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < this.originalBild.getWidth(); x++)
		{
			for (int y = 0; y < this.originalBild.getHeight(); y++)
			{
				testBild.setRGB(x, y, new Color((this.originalBild.getDifferenzBild().getRot(x, y) < par1) ? 0 : this.originalBild.getDifferenzBild().getRot(x, y), (this.originalBild.getDifferenzBild().getGruen(x, y) < par1) ? 0 : this.originalBild.getDifferenzBild().getGruen(x, y), (this.originalBild.getDifferenzBild().getBlau(x, y) < par1) ? 0 : this.originalBild.getDifferenzBild().getBlau(x, y)).getRGB());
			}
		}
		
		this.testBild.prepare();
		
		
		
		for (int y = 0; y < originalBild.getHeight(); y++)
		{
			int x = 0;			
			for (x = 0; x < originalBild.getWidth(); x++)
			{
				if (this.testBild.getDurchschnitt(x, y, par2) < 1)
				{
					this.originalBild.setRGB(x, y, new Color(this.originalBild.getRot(x, y), this.originalBild.getGruen(x, y), this.originalBild.getBlau(x, y), 0).getRGB());
				}
			}
		}*/
		
		// TODO nichtfertig
	}
}
