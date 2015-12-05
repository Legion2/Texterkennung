package texterkennung.data;

import java.awt.image.BufferedImage;

import GUI.GuiElements;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Data_Image extends Data_ID
{
	public Data_Image(BufferedImage image, String string, boolean b)
	{
		super(image.getWidth(), image.getHeight(), string, b);
		this.setBufferedImage(image, b);
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
	
	public ImageView getImageView()
	{
		WritableImage wr = new WritableImage(this.xlenght, this.ylenght);
        PixelWriter pw = wr.getPixelWriter();
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				 pw.setArgb(x, y, this.getInt(x, y));
			}
		}
		
		ImageView imView = new ImageView(wr);
	    return imView;
	}
	
	@Override
	public void gui(BorderPane pane)
	{
		ImageView image = this.getImageView();
		pane.setCenter(image);
	}
}
