package texterkennung.data;

import java.awt.Color;

import advanced.ABufferedImage;
import debug.Debugger;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Data_Zeichen extends Data
{
	private final int ID;
	private final int xstart;
	private final int ystart;
	private final int xend;
	private final int yend;
	private final Data_ID data_ID;

	public Data_Zeichen(int iD, int xstart, int xend, int ystart, int yend, Data_ID data_ID_input, String name)
	{
		super(name, false);
		Debugger.info(this, name);
		this.ID = iD;
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
		this.data_ID = data_ID_input;
	}

	@Override
	public void gui(Pane pane)
	{
		ABufferedImage image = new ABufferedImage(xend - xstart + 1, yend - ystart + 1);
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				int wert = Math.abs(this.data_ID.getInt(xstart + x, ystart + y));
				image.setRGB(x, y, new Color((wert*17)%255, 255 - (wert*47)%255, (wert*23)%255).getRGB());
			}
		}
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(new Label("xstart: " + this.xstart + " xend: " + this.xend + " ystart: " + this.ystart + " yend: " + this.yend));
		borderPane.setCenter(image.getImageView());
		pane.getChildren().add(borderPane);
	}
}
