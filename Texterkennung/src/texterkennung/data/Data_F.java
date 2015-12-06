package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import advanced.ABufferedImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Data_F extends Data2D
{
	private float[][] data;
	private float defaultwert = 1.0f;
	
	public Data_F(Data2D data, String name)
	{
		super(data, name);
	}
	
	public float getFloat(int x, int y)
	{
		return (x < 0 || y < 0 || x >= this.xlenght || y >= this.ylenght) ? this.defaultwert : this.data[x][y];
	}
	
	public void setFloat(int x, int y, float wert)
	{
		this.data[x][y] = wert;
	}
	
	public void setDefault(float d)
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
	
	public float getDefault()
	{
		return this.defaultwert;
	}

	@Override
	public void gui(BorderPane pane)
	{
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				float wert = this.data[x][y];
				
				bi.setRGB(x, y, new Color(wert, wert, wert).getRGB());
			}
		}
		
		ImageView image = bi.getImageView();
		pane.setCenter(image);
	}

	@Override
	protected void init()
	{
		this.data = new float[this.xlenght][this.ylenght];
	}
}
