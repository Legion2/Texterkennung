package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import advanced.ABufferedImage;
import advanced.AColor;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

/**
 * 2D int Array Data
 * @author Leon
 *
 */
public class Data_ID extends Data2D
{
	private int[][] data;
	private final int defaultwert = -1;
	private int maxid = 0;
	
	/**
	 * 
	 * @param data2d anderes Data2D object, dessen größe für das neue Object übernommen wird
	 * @param name Anzeigename in der Gui
	 * @param b
	 */
	public Data_ID(Data2D data2d, String name, boolean b)
	{
		super(data2d, name, b);
	}

	/**
	 * 
	 * @param x Größe in x Richtung
	 * @param y Größe in y Richtung
	 * @param name Anzeigename in der Gui
	 * @param b
	 */
	public Data_ID(int x, int y, String name, boolean b)
	{
		super(x, y, name, b);
	}

	/**
	 * Gibt den gespeicherten int wert zurück
	 * @param x
	 * @param y
	 * @return gespeicherter int wert
	 */
	public int getInt(int x, int y)
	{
		return (x < 0 || y < 0 || x >= this.xlenght || y >= this.ylenght) ? this.defaultwert : this.data[x][y];
	}
	
	public void setInt(int x, int y, int wert)
	{
		this.data[x][y] = wert;
	}

	public int getMaxid()
	{
		return maxid;
	}

	/**
	 * Setzt den größten int wert der gespeichert wurde.
	 * @param maxid
	 */
	public void setMaxid(int maxid)
	{
		this.maxid = maxid;
	}
	public int getDefault()
	{
		return this.defaultwert;
	}
	
	/**
	 * Setzt alle Werte auf den defaultwert Wert.
	 */
	@Override
	public void setDefault()
	{
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.data[x][y] = this.defaultwert;
			}
		}
	}

	@Override
	protected void init()
	{
		this.data = new int[this.xlenght][this.ylenght];
	}

	@Override
	public void gui(BorderPane pane)
	{
		WritableImage wr = new WritableImage(this.xlenght, this.ylenght);
        PixelWriter pw = wr.getPixelWriter();
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				int wert = this.data[x][y];
				if (wert == this.defaultwert)
				{
					pw.setArgb(x, y, AColor.weiß);
				}
				else
				{
					wert = Math.abs(wert);
					pw.setArgb(x, y, new Color((wert*17)%255, 255 - (wert*47)%255, (wert*23)%255).getRGB());
				}
			}
		}
		
		ImageView image = new ImageView(wr);
		pane.setCenter(image);
	}
}