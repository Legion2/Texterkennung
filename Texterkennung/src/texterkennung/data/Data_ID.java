package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import advanced.ABufferedImage;
import advanced.AColor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Data_ID extends Data2D
{
	private int[][] data;
	private int defaultwert = -1;
	private int maxid = 0;
	
	public Data_ID(int x, int y, String name)
	{
		super(x, y, name, true);
	}
	
	public Data_ID(Data2D data2d, String name)
	{
		super(data2d, name);
	}

	public Data_ID(int x, int y, String name, boolean b)
	{
		super(x, y, name, b);
	}

	public int getInt(int x, int y)
	{
		return (x < 0 || y < 0 || x >= this.xlenght || y >= this.ylenght) ? 0 : this.data[x][y];
	}
	
	public void setInt(int x, int y, int wert)
	{
		this.data[x][y] = wert;
	}

	public int getMaxid()
	{
		return maxid;
	}

	public void setMaxid(int maxid)
	{
		this.maxid = maxid;
	}

	@Override
	protected void init()
	{
		this.data = new int[this.xlenght][this.ylenght];
	}

	@Override
	public void gui(BorderPane pane)
	{
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				int wert = this.data[x][y];
				if (wert == this.defaultwert)
				{
					bi.setRGB(x, y, AColor.weiß);
				}
				else
				{
					wert = Math.abs(wert);
					bi.setRGB(x, y, new Color((wert*17)%255, 255 - (wert*47)%255, (wert*23)%255).getRGB());
				}
			}
		}
		
		ImageView image = bi.getImageView();
		pane.setCenter(image);
	}

	public void setDefault(int d)
	{
		this.defaultwert = d;
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.data[x][y] = this.defaultwert;
			}
		}
	}
	
	public int getDefault()
	{
		return this.defaultwert;
	}
}
