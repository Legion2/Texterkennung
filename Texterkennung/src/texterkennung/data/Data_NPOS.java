package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import GUI.GuiElements;
import advanced.ABufferedImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Data_NPOS extends Data2D
{
	private int[][] xPOS;
	private int[][] yPOS;

	public Data_NPOS(Data2D data2d, String name)
	{
		super(data2d, name);
	}

	protected void init()
	{
		this.xPOS = new int[this.xlenght][this.ylenght];
		this.yPOS = new int[this.xlenght][this.ylenght];
	}
	
	public void setData(Data_ID data_ID)
	{
		//TODO parallelisieren??? möglich ist es
		System.out.println(data_ID.getMaxid());
		int p = 0;
		for (int i = 0; i <= data_ID.getMaxid(); i++)
		{
			System.out.println(i);
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
			
			
			
			int j = (i * 20) / data_ID.getMaxid();
			if (j > p)
			{
				p = j;
				System.out.println("Konvertierung bei " + (j * 5) + "%");
			}
		}
		GuiElements.MainGUI.setTab(this);
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
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				// TODO testen!!!
				bi.setRGB(x, y, new Color(((this.xPOS[x][y]*this.ylenght+this.yPOS[x][y]) * 17)%255, 255 - ((this.xPOS[x][y]*this.ylenght+this.yPOS[x][y]) * 47)%255, ((this.xPOS[x][y]*this.ylenght+this.yPOS[x][y]) *23 )%255).getRGB());
			}
		}
		
		
		ImageView image = bi.getImageView();
		pane.getChildren().add(image);
	}
}
