package texterkennung.data;

import java.awt.image.BufferedImage;

public class Data_int extends Data
{
	private final int xlenght;
	private final int ylenght;
	private int[][] data;
	
	public Data_int(int x, int y)
	{
		this.xlenght = x;
		this.ylenght = y;
		this.data = new int[x][y];
	}
	
	public Data_int(int x, int y, int[][] data)
	{
		this.xlenght = x;
		this.ylenght = y;
		this.data = data;
	}

	public Data_int(Data_int data_int_input)
	{
		this.xlenght = data_int_input.xlenght;
		this.ylenght = data_int_input.ylenght;
		this.data = new int[this.xlenght][this.ylenght];
	}
	
	public int getInt(int x, int y)
	{
		return this.data[x][y];
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
				// TODO
			}
		}
		
		return bi;
	}

	@Override
	public String getName()
	{
		return "Int-Array-Data";
	}
}
