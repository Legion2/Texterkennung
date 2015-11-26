package texterkennung.data;

import java.awt.Color;

import advanced.ABufferedImage;
import advanced.AColor;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Data_Zeichen extends Data
{
	private final int ID;
	private final int xstart;
	private final int ystart;
	private final int xend;
	private final int yend;
	private final int hoehe;
	private final int breite;
	private final Data_ID data_ID;
	private float[] snow_Boden;
	private float[] snow_Wand;
	private char c;

	public Data_Zeichen(int iD, int xstart, int xend, int ystart, int yend, Data_ID data_ID_input, String name)
	{
		super(name, false);
		this.ID = iD;
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
		this.hoehe = yend - ystart + 1;
		this.breite = xend - xstart + 1;
		this.data_ID = data_ID_input;
		
		this.snow();
	}
	
	public Data_Zeichen(char c, int xstart, int xend, int ystart, int yend, Data_ID data_ID_input, String name)
	{
		super(name, false);
		this.ID = 1;
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
		this.hoehe = yend - ystart + 1;
		this.breite = xend - xstart + 1;
		this.data_ID = data_ID_input;
		this.c = c;
		
		this.snow();
	}

	/**
	 * Schneefall ausrechnen
	 */
	private void snow()
	{
		this.snow_Boden = new float[this.breite];
		this.snow_Wand = new float[this.hoehe];
		
		for (int x = 0; x < this.breite; x++)
		{
			float summe = 0;
			for (int y = 0; y < this.hoehe; y++)
			{
				summe += this.data_ID.getInt(xstart + x, ystart + y) != AColor.weiß ? 1 : 0;
			}
			
			this.snow_Boden[x] = summe / this.hoehe;
		}
		for (int y = 0; y < this.hoehe; y++)
		{
			float summe = 0;
			for (int x = 0; x < this.breite; x++)
			{
				summe += this.data_ID.getInt(xstart + x, ystart + y) != AColor.weiß ? 1 : 0;
			}
			
			this.snow_Wand[y] = summe / this.breite;
		}
	}
	
	public double vergleichenmit(Data_Zeichen zeichen)
	{
		float summe = 0;
		
		float wert;
		float vwert;
		
		for (int x = 0; x < zeichen.breite; x++)
		{
			wert = zeichen.getSnow_Boden(x);
			float x2 = ((x + 0.5f) / zeichen.breite) * this.breite - 0.5f;
			int x2i = (int) x2;
			
			vwert = this.getSnow_Boden(x2i) + (this.getSnow_Boden(x2i + 1) - this.getSnow_Boden(x2i)) * (x2 - x2i);
			
			summe += Math.abs(vwert - wert);
		}
		
		summe /= zeichen.breite;
		
		float summe2 = 0;
		
		
		for (int y = 0; y < zeichen.hoehe; y++)
		{
			wert = zeichen.getSnow_Wand(y);
			float y2 = ((y + 0.5f) / zeichen.hoehe) * this.hoehe - 0.5f;
			int y2i = (int) y2;
			
			vwert = this.getSnow_Wand(y2i) + (this.getSnow_Wand(y2i + 1) - this.getSnow_Wand(y2i)) * (y2 - y2i);
			
			summe2 += Math.abs(vwert - wert);
		}
		summe2 /= zeichen.hoehe;
		
		return summe + summe2;
	}
	
	private float getSnow_Wand(int index)
	{
		return index < 0 ? this.snow_Wand[0] : (index < this.snow_Wand.length ? this.snow_Wand[index] : this.snow_Wand[this.snow_Wand.length - 1]);
	}
	
	private float getSnow_Boden(int index)
	{
		return index < 0 ? this.snow_Boden[0] : (index < this.snow_Boden.length ? this.snow_Boden[index] : this.snow_Boden[this.snow_Boden.length - 1]);
	}
	
	public void setchar(char c)
	{
		this.c = c;
	}
	
	public char getchar()
	{
		return this.c;
	}

	@Override
	public void gui(Pane pane)
	{
		ABufferedImage image = new ABufferedImage(this.breite, this.hoehe);
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				int wert = this.data_ID.getInt(xstart + x, ystart + y);
				if (wert == this.data_ID.getDefault())
				{
					image.setRGB(x, y, AColor.weiß);
				}
				else
				{
					image.setRGB(x, y, AColor.schwarz);
				}
			}
		}
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(new Label("xstart: " + this.xstart + " xend: " + this.xend + " ystart: " + this.ystart + " yend: " + this.yend));
		borderPane.setCenter(image.getImageView());
		borderPane.setBottom(new Label("Zeichen: " + this.c));
		pane.getChildren().add(borderPane);
	}
}
