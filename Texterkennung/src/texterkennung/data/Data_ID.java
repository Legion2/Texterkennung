package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Data_ID extends Data
{
	private final int xlenght;
	private final int ylenght;
	private int[][] data;
	private int maxid = 0;
	
	public Data_ID(int x, int y)
	{
		this.xlenght = x;
		this.ylenght = y;
		this.data = new int[x][y];
	}
	
	public Data_ID(int x, int y, int[][] data)
	{
		this.xlenght = x;
		this.ylenght = y;
		this.data = data;
	}

	public Data_ID(Data_ID data_ID_input)
	{
		this.xlenght = data_ID_input.xlenght;
		this.ylenght = data_ID_input.ylenght;
		this.data = new int[this.xlenght][this.ylenght];
	}
	
	public int getInt(int x, int y)
	{
		return (x < 0 || y < 0) ? 0 : this.data[x][y];
	}
	
	public void setInt(int x, int y, int wert)
	{
		this.data[x][y] = wert;
	}
	
	public int getXlenght()
	{
		return xlenght;
	}
	
	public int getYlenght()
	{
		return ylenght;
	}
	
	@Override
	public BufferedImage visualisieren()
	{
		BufferedImage bi = new BufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				// TODO testen!!!
				bi.setRGB(x, y, new Color((this.data[x][y]*5)%255, 255 - (this.data[x][y]*7)%255, (this.data[x][y]*11)%255).getRGB());
			}
		}
		
		return bi;
	}

	@Override
	public String getName()
	{
		return "Int-Array-Data";
	}

	public int getMaxid()
	{
		return maxid;
	}

	public void setMaxid(int maxid)
	{
		this.maxid = maxid;
	}
}
