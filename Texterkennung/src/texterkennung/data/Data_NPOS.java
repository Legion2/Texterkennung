package texterkennung.data;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Data_NPOS extends Data2D
{
	private int[][] xPOS;
	private int[][] yPOS;

	public Data_NPOS(Data2D data2d)
	{
		super(data2d);
	}

	protected void init()
	{
		this.xPOS = new int[this.xlenght][this.ylenght];
		this.yPOS = new int[this.xlenght][this.ylenght];
	}
	
	public void setData(Data_ID data_ID)
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
	public String getName()
	{
		return "Next-Position-Data";
	}
	
	public void setNPOS(int x, int y, int xset, int yset)
	{
		this.xPOS[x][y] = xset;
		this.yPOS[x][y] = yset;
	}
	
	public int[] getNPOS(int x, int y)
	{
		int[] array = {this.xPOS[x][y], this.yPOS[x][y]};
		return array;
	}

	@Override
	public void gui(Pane pane)
	{
		ImageView iv = new ImageView();
		pane.getChildren().add(iv);
	}
}
