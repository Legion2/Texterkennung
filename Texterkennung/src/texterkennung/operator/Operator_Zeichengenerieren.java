package texterkennung.operator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import GUI.GUI;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_F;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_Zeichen;

/**
 * Generiert eine Liste Data_Zeichen mit allen Zeichen, die im String ¸bergeben wurden.
 * @author Leon
 *
 */
public class Operator_Zeichengenerieren extends Operator
{
	private final DataList zeichen;
	private final String zeichenString;
	private final Font font;
	private final boolean schwarzweiﬂ;
	private final int hoehe = 200, breite = 100; 
	
	/**
	 * 
	 * @param string String mit allen Zeichen die generiert werden
	 * @param font Schriftart der generierten Zeichen
	 * @param schwarzweiﬂ AA
	 */
	public Operator_Zeichengenerieren(String string, Font font, boolean schwarzweiﬂ)
	{
		this.zeichen = new DataList("Generierte Zeichen", true);
		this.zeichenString = string;
		this.font = font;
		this.schwarzweiﬂ = schwarzweiﬂ;
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichengenerieren";
	}

	@Override
	public void run()
	{
		BufferedImage zeichenBild = new BufferedImage(this.breite, this.hoehe, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = zeichenBild.createGraphics();
		if (!this.schwarzweiﬂ) g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(this.font.deriveFont(this.breite + 0.0f));
		g.setColor(new Color(0, 0, 0));
		g.setBackground(new Color(255, 255, 255));
		
		for (int i = 0; i < this.zeichenString.length(); i++)
		{
			char c = this.zeichenString.charAt(i);
			
			//Zeichen zeichnen
			g.clearRect(0, 0, breite, hoehe);
			g.drawString(String.valueOf(c), 1, this.hoehe * 2 / 3);
			
			Data_F data_F = new Data_F(zeichenBild, "generiertes Zeichen: " + c, false);
			
			int xmin = this.breite - 1, xmax = 0, ymin = this.hoehe - 1, ymax = 0;
			
			for (int y = 0; y < this.hoehe; y++)
			{
				for (int x = 0; x < this.breite; x++)
				{
					if (data_F.getFloat(x, y) != 1.0f)
					{
						xmin = xmin > x ? x : xmin;
						xmax = xmax < x ? x : xmax;
						ymin = ymin > y ? y : ymin;
						ymax = ymax < y ? y : ymax;
					}
				}
			}
			
			this.zeichen.add(new Data_Zeichen(c, xmin, xmax, ymin, ymax, data_F, this.schwarzweiﬂ, "generiertes Zeichen: " + c));
		}
		
		GUI.MainGUI.setTab(this.zeichen);
	}

	@Override
	public Data getData()
	{
		return this.zeichen;
	}
}
