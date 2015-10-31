package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Data_NPOS extends Data2D
{
	private int[][] xPOS;
	private int[][] yPOS;
	
	public Data_NPOS(Data_ID data_ID)
	{
		super(data_ID.xlenght, data_ID.ylenght);
		this.xPOS = new int[data_ID.xlenght][data_ID.ylenght];
		this.yPOS = new int[data_ID.xlenght][data_ID.ylenght];
		
		setData(data_ID);
	}
	
	public Data_NPOS(Data_NPOS data_NPOS)
	{
		super(data_NPOS.xlenght, data_NPOS.ylenght);
		
		this.xPOS = new int[data_NPOS.xlenght][data_NPOS.ylenght];
		this.yPOS = new int[data_NPOS.xlenght][data_NPOS.ylenght];
	}
	
	private void setData(Data_ID data_ID)
	{
		//TODO parallelisieren??? möglich ist es
		
		for (int i = 0; i <= data_ID.getMaxid(); i++)
		{
			int lastindex_x = -1, lastindex_y = -1;
			int firstindex_x = -1, firstindex_y = -1;
			
			for (int y = 0; y < data_ID.getYlenght(); y++)
			{
				for (int x = 0; x < data_ID.getXlenght(); x++)
				{
					if (data_ID.getInt(x, y) == i)
					{
						if (lastindex_x == -1)
						{
							firstindex_x = lastindex_x = x;
							firstindex_y = lastindex_y = y;
						}
						else
						{
							this.xPOS[lastindex_x][lastindex_y] = x;
							this.yPOS[lastindex_x][lastindex_y] = y;
							lastindex_x = x;
							lastindex_y = y;
						}
					}
				}
			}
			
			if (lastindex_x != -1)
			{
				this.xPOS[lastindex_x][lastindex_y] = firstindex_x;
				this.yPOS[lastindex_x][lastindex_y] = firstindex_y;
			}
		}
	}

	@Override
	public BufferedImage visualisieren()
	{
		BufferedImage bi = new BufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		return bi;
	}

	@Override
	public String getName()
	{
		return "Next-Position-Data";
	}
}
