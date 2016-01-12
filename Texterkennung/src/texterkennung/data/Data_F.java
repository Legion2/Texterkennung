package texterkennung.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import advanced.AColor;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

/**
 * 2D float Array Data
 * @author Leon
 *
 */
public class Data_F extends Data2D
{
	private float[][] data;
	private final float defaultwert = 1.0f;
	
	public Data_F(Data2D data, String name, boolean b)
	{
		super(data, name, b);
	}
	
	/**
	 * 
	 * @param data_Image Bild mit weiﬂem Hintergrund und schwarzer Schrift
	 * @param name
	 */
	public Data_F(BufferedImage bufferedImage, String name, boolean b)
	{
		super(bufferedImage.getWidth(), bufferedImage.getHeight(), name, b);
		this.setDatafromImage(bufferedImage, b);
	}
	
	private void setDatafromImage(BufferedImage bufferedImage, boolean b)
	{
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.setFloat(x, y, new AColor(bufferedImage.getRGB(x, y)).getBlue() / 255.0f);
			}
		}
	}
	
	public float getFloat(int x, int y)
	{
		return (x < 0 || y < 0 || x >= this.xlenght || y >= this.ylenght) ? this.defaultwert : this.data[x][y];
	}
	
	public void setFloat(int x, int y, float wert)
	{
		this.data[x][y] = wert;
	}
	
	public void setDefault()
	{
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				this.data[x][y] = this.defaultwert;
			}
		}
	}
	
	public float getDefault()
	{
		return this.defaultwert;
	}

	@Override
	public void gui(BorderPane pane)
	{
		WritableImage wr = new WritableImage(this.xlenght, this.ylenght);
        PixelWriter pw = wr.getPixelWriter();
		
		for (int y = 0; y < this.ylenght; y++)
		{
			for (int x = 0; x < this.xlenght; x++)
			{
				float wert = this.data[x][y];
				
				pw.setArgb(x, y, new Color(wert, wert, wert).getRGB());
			}
		}
		
		ImageView image = new ImageView(wr);
		pane.setCenter(image);
	}

	@Override
	protected void init()
	{
		this.data = new float[this.xlenght][this.ylenght];
	}
}
