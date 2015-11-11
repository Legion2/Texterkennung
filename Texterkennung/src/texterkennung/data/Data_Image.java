package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import advanced.ABufferedImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Data_Image extends Data_ID
{
	public Data_Image(int x, int y, String name)
	{
		super(x, y, name);
	}
	
	public void setBufferedImage(BufferedImage image)
	{
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.setInt(x, y, image.getRGB(x, y));
			}
		}
	}
	
	@Override
	public void gui(Pane pane)
	{
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				bi.setRGB(x, y, new Color(this.getInt(x, y), this.getInt(x, y), this.getInt(x, y)).getRGB());
			}
		}
		
		
		ImageView image = bi.getImageView();
		pane.getChildren().add(image);
	}
}
