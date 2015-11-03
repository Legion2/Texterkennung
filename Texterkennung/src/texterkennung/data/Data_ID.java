package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import GUI.basicCalculation;

public class Data_ID extends Data2D
{
	private int[][] data;
	private int maxid = 0;
	
	public Data_ID(int x, int y)
	{
		super(x, y);
	}
	
	//not used
	public Data_ID(int x, int y, int[][] data)
	{
		super(x, y);
		this.data = data;
	}
	
	public Data_ID(Data2D data2d)
	{
		super(data2d);
	}

	public int getInt(int x, int y)
	{
		return (x < 0 || y < 0 || x >= this.xlenght || y >= this.ylenght) ? 0 : this.data[x][y];
	}
	
	public void setInt(int x, int y, int wert)
	{
		this.data[x][y] = wert;
	}
	
	@Override
	public void gui()
	{
		BufferedImage bi = new BufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				// TODO testen!!!
				bi.setRGB(x, y, new Color((this.data[x][y]*17)%255, 255 - (this.data[x][y]*47)%255, (this.data[x][y]*23)%255).getRGB());
			}
		}
		
		
		ImageView image = GUI.basicCalculation.BufferdToImage (bi);
		
		BorderPane borderpane =new BorderPane (image);
		
		
	}

	@Override
	public String getName()
	{
		return "ID-Array-Data";
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
}
