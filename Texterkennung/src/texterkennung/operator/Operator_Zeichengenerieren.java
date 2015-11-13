package texterkennung.operator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import GUI.GuiElements;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_Zeichen;

public class Operator_Zeichengenerieren extends Operator
{
	private final DataList zeichen;
	private final String zeichenString;
	private final Font font;
	private final int hoehe = 200, breite = 100; 
	
	public Operator_Zeichengenerieren(String string, Font font)
	{
		this.zeichen = new DataList("Generierte Zeichen");
		this.zeichenString = string;
		this.font = font;
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichengenerieren";
	}

	@Override
	public void run()
	{
		for (int i = 0; i < this.zeichenString.length(); i++)
		{
			char c = this.zeichenString.charAt(i);
			
			//Zeichen zeichnen
			BufferedImage zeichenBild = new BufferedImage(this.breite, this.hoehe, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = zeichenBild.createGraphics();
			g.setFont(this.font.deriveFont(this.breite + 0.0f));
			g.setColor(new Color(0, 0, 0));
			g.setBackground(new Color(255, 255, 255));
			g.clearRect(0, 0, breite, hoehe);
			g.drawString(String.valueOf(c), 1, this.hoehe * 2 / 3);
			
			Data_ID data = new Data_Image(zeichenBild, "generiertes Zeichen: " + c, false);
			
			int xmin = this.breite - 1, xmax = 0, ymin = this.hoehe - 1, ymax = 0;
			
			for (int y = 0; y < this.hoehe; y++)
			{
				for (int x = 0; x < this.breite; x++)
				{
					if (data.getInt(x, y) != new Color(255,255,255).getRGB())
					{
						xmin = xmin > x ? x : xmin;
						xmax = xmax < x ? x : xmax;
						ymin = ymin > y ? y : ymin;
						ymax = ymax < y ? y : ymax;
					}
				}
			}
			
			this.zeichen.add(new Data_Zeichen(c, xmin, xmax, ymin, ymax, data, "Zeichen: " + c));
		}
		
		GuiElements.MainGUI.setTab(this.zeichen);
	}

	@Override
	public Data getData()
	{
		return this.zeichen;
	}

}
