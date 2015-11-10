package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import advanced.ABufferedImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Data_ID extends Data2D
{
	private int[][] data;
	private int maxid = 0;
	
	public Data_ID(int x, int y, String name)
	{
		super(x, y, name);
	}
	
	//not used
	public Data_ID(int x, int y, int[][] data, String name)
	{
		super(x, y, name);
		this.data = data;
	}
	
	public Data_ID(Data2D data2d, String name)
	{
		super(data2d, name);
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
	public void gui(Pane pane)
	{
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				bi.setRGB(x, y, new Color((this.data[x][y]*17)%255, 255 - (this.data[x][y]*47)%255, (this.data[x][y]*23)%255).getRGB());
			}
		}
		
		
		ImageView image = bi.getImageView();
		pane.getChildren().add(image);
	}
}
