package texterkennung.data;

import java.awt.image.BufferedImage;

import GUI.GuiElements;
import advanced.ABufferedImage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Data_Image extends Data_ID
{
	public Data_Image(BufferedImage image, String string, boolean b)
	{
		super(image.getWidth(), image.getHeight(), string, b);
		setBufferedImage(image, b);
	}

	private void setBufferedImage(BufferedImage image, boolean b)
	{
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.setInt(x, y, image.getRGB(x, y));
			}
		}
		
		if (b) GuiElements.MainGUI.setTab(this);
	}
	
	@Override
	public void gui(Pane pane)
	{
		ABufferedImage bi = new ABufferedImage(this.xlenght, this.ylenght);
		
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
