package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GUI.GuiElements;
import GUI.IGUI;
import advanced.ABufferedImage;
import advanced.AColor;
import javafx.scene.layout.Pane;

public abstract class Erkennung implements IGUI {

	protected ArrayList<AColor> farbListe;
	
	protected final ABufferedImage originalBild;
	
	public Erkennung(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font)
	{
		this.originalBild = new ABufferedImage(bufferedImage);
		this.originalBild.setImage(bufferedImage);
		this.farbListe = farbListe;
		Zeichen.setup(font);
		GuiElements.MainGUI.addTab(this);
		GuiElements.MainGUI.setTab(this);
	}

	public abstract void run(int par1, int par2);
	
	@Override
	public void gui(Pane pane)
	{
		pane.getChildren().add(this.originalBild.getImageView());
	}
}
