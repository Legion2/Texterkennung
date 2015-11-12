package texterkennung.data;

import java.awt.image.BufferedImage;

import GUI.GuiElements;
import advanced.ABufferedImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Data_Image extends Data_ID
{
	public Data_Image(BufferedImage image, String string)
	{
		super(image.getWidth(), image.getHeight(), string);
		setBufferedImage(image);
	}

	private void setBufferedImage(BufferedImage image)
	{
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.setInt(x, y, image.getRGB(x, y));
			}
		}
		
		GuiElements.MainGUI.setTab(this);
	}
	
	@Override
	public void gui(Pane pane)
	{
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				bi.setRGB(x, y, this.getInt(x, y));
			}
		}
		
		ImageView image = bi.getImageView();
		pane.getChildren().add(image);
	}
}
