package texterkennung.data;

import advanced.ABufferedImage;
import advanced.AColor;
import debug.Debugger;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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
	private final Data_F data_F;
	private float[] snow_Boden;
	private float[] snow_Wand;
	private char c;

	public Data_Zeichen(int iD, int xstart, int xend, int ystart, int yend, Data_ID data_ID_input, Data_F data_F_input, boolean schwarzweiﬂ, String name)
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
		this.data_F = data_F_input;
		
		this.snow(schwarzweiﬂ);
	}
	
	public Data_Zeichen(char c, int xstart, int xend, int ystart, int yend, Data_ID data_ID_input, String name)
	{
		super(name, false);
		this.ID = -1;
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
		this.hoehe = yend - ystart + 1;
		this.breite = xend - xstart + 1;
		this.data_ID = data_ID_input;
		this.data_F = null;
		this.c = c;
		
		this.snow(true);
	}

	/**
	 * Schneefall ausrechnen
	 * 
	 */
	private void snow(boolean schwarzweiﬂ)
	{
		this.snow_Boden = new float[this.breite];
		this.snow_Wand = new float[this.hoehe];
		
		for (int x = 0; x < this.breite; x++)
		{
			float summe = 0;
			for (int y = 0; y < this.hoehe; y++)
			{
				if (schwarzweiﬂ)
				{
					summe += this.data_ID.getInt(xstart + x, ystart + y) != AColor.weiﬂ ? 1 : 0;
				}
				else
				{
					summe += this.data_ID.getInt(xstart + x, ystart + y) != AColor.weiﬂ ? (1 - this.data_F.getFloat(xstart + x, ystart + y)) : 0;
				}
			}
			
			this.snow_Boden[x] = summe / this.hoehe;
		}
		for (int y = 0; y < this.hoehe; y++)
		{
			float summe = 0;
			for (int x = 0; x < this.breite; x++)
			{
				if (schwarzweiﬂ)
				{
					summe += this.data_ID.getInt(xstart + x, ystart + y) != AColor.weiﬂ ? 1 : 0;
				}
				else
				{
					summe += this.data_ID.getInt(xstart + x, ystart + y) != AColor.weiﬂ ? (1 - this.data_F.getFloat(xstart + x, ystart + y)) : 0;
				}
			}
			
			this.snow_Wand[y] = summe / this.breite;
		}
	}
	
	/**
	 * Vergleicht zwei Zeichen und gibt die ‰hnlichkeit als wert zur¸ck.
	 * 
	 * @param zeichen Das Zeichen mit dem an diese Zeichen vergleicht
	 * @return Wert der die den Grad der ¸bereinstimmung an gibt, je kleiner dieser ist, desto ‰hnlicher sind sich die zeichen
	 */
	public double vergleichenmit(Data_Zeichen zeichen)
	{
		float wert;
		float vwert;
		
		//
		float summe = 0;
		for (int x = 0; x < zeichen.breite; x++)
		{
			wert = zeichen.getSnow_Boden(x);
			vwert = this.getIntegralnormal(this.snow_Boden, x * 1.0f / zeichen.breite, (x + 1.0f) / zeichen.breite);
			
			summe += Math.abs(vwert - wert);
		}
		summe /= zeichen.breite;
		
		//
		float summe2 = 0;
		for (int y = 0; y < zeichen.hoehe; y++)
		{
			wert = zeichen.getSnow_Wand(y);
			vwert = this.getIntegralnormal(this.snow_Wand, y * 1.0f / zeichen.hoehe, (y + 1.0f) / zeichen.hoehe);
			
			summe2 += Math.abs(vwert - wert);
		}
		summe2 /= zeichen.hoehe;
		
		//Hˆhen-Breiten Verh‰ltnis
		float ver = (zeichen.breite * 1.0f / zeichen.hoehe) / (this.breite * 1.0f / this.hoehe);
		
		ver = ver < 1 ? (1 / ver) - 1 : ver - 1;
		
		if (ver < 0.1f) Debugger.info(this, "Ver: " + ver);
		
		return summe + summe2 + ver;
	}
	
	/**
	 * 
	 * @param snow_array
	 * @param xmin range 0.0F - 1.0F
	 * @param xmax range 0.0F - 1.0F
	 * @return range 0.0F - 1.0F
	 */
	private float getIntegralnormal(float[] snow_array, float xmin, float xmax)
	{
		float min = xmin * snow_array.length;
		float max = xmax * snow_array.length;
		
		return this.getIntegral(snow_array, min, max) / (max - min);
	}

	/**
	 * 
	 * @param snow_array
	 * @param xmin range 0.0F - snow_array.length
	 * @param xmax range 0.0F - snow_array.length
	 * @return integral from xmin to xmax over snow_array
	 */
	private float getIntegral(float[] snow_array, float xmin, float xmax)
	{
		float summe = 0.0f;
		
		int min = (int) xmin;
		int max = (int) xmax;
		for (int i = min + 1; i < max; i++)
		{
			summe += snow_array[i];
		}
		
		summe += snow_array[min] - this.lineareInterpolation(snow_array, xmin, this.getoffset(snow_array, min));
		
		
		if (max != xmax)
		{
			summe += this.lineareInterpolation(snow_array, xmax, this.getoffset(snow_array, max));
		}
		
		
		
		return summe;
	}
	
	private float getoffset(float[] snow_array, int mitte)
	{
		if (mitte == 0 || mitte == (snow_array.length - 1)) return 0;
		
		return (snow_array[mitte - 1] + snow_array[mitte + 1] - 2 * snow_array[mitte]) / 6;
	}

	private float lineareInterpolation(float[] snow_array, float f, float offset)
	{
		int min = (int) f;
		float koma = f - min;
		if (offset == 0)
		{
			if (min == 0)
			{
				return (snow_array[min + 1] + (snow_array[min] - snow_array[min + 1]) * (koma + 1) / 2) * koma;
			}
			else
			{
				return (snow_array[min - 1] + (snow_array[min] - snow_array[min - 1]) * (koma + 1) / 2) * koma;
			}
		}
		else
		{
			
			if (koma < 0.5)
			{
				return (snow_array[min - 1] + (snow_array[min] + offset - snow_array[min - 1]) * (koma + 1) / 2) * koma;
			}
			else
			{
				return snow_array[min] - (snow_array[min + 1] + (snow_array[min] + offset - snow_array[min + 1]) * (2 - koma) / 2) * (1 - koma);
			}
		}
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
	public void gui(BorderPane pane)
	{
		ABufferedImage image = new ABufferedImage(this.breite, this.hoehe);
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				int wert = this.data_ID.getInt(xstart + x, ystart + y);
				if (wert == this.data_ID.getDefault())
				{
					image.setRGB(x, y, AColor.weiﬂ);
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
		pane.setCenter(borderPane);//TODO 
	}
}
