package texterkennung;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import advanced.AColor;
import texterkennung.data.Data_int;
import texterkennung.operator.Operator_Farbzuordnung;
import texterkennung.operator.Operator_Verbindungen;

public class Erkennung_Text extends Erkennung
{

	public Erkennung_Text(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font)
	{
		super(bufferedImage, farbListe, font);
	}

	@Override
	public String getName()
	{
		return "Erkennung_Text";
	}

	@Override
	public BufferedImage visualisieren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(int par1, int par2)
	{
		System.out.println("run");
		
		Operator_Farbzuordnung OF = new Operator_Farbzuordnung(originalBild, farbListe, 5);
		OF.run();
		
		Operator_Verbindungen OV = new Operator_Verbindungen((Data_int) OF.getData());
		
		
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

	@Override
	public void gui(Graphics g, int w) {
		// TODO Auto-generated method stub
		// TODO nichtfertig
	}
}
