package texterkennung.data;

import java.awt.image.BufferedImage;

public class Data_NPOS extends Data
{
	private int[][] xPOS;
	private int[][] yPOS;
	
	public Data_NPOS(Data_ID data_ID)
	{
		this.xPOS = new int[data_ID.getXlenght()][data_ID.getYlenght()];
		this.yPOS = new int[data_ID.getXlenght()][data_ID.getYlenght()];
		
		
		setData(data_ID);
	}
	
	private void setData(Data_ID data_ID)
	{
		//TODO parallelisieren??? möglich ist es
		
		for (int i = 0; i <= data_ID.getMaxid(); i++)
		{
			for (int y = 0; y < data_ID.getYlenght(); y++)
			{
				for (int x = 0; x < data_ID.getXlenght(); x++)
				{
					
				}
			}
		}
		
		
		
	}

	@Override
	public BufferedImage visualisieren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		return "Next-Position-Data";
	}

}
