package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import GUI.GuiElements;
import GUI.IGUI;
import advanced.ABufferedImage;
import advanced.AColor;
import javafx.scene.layout.Pane;

public abstract class Erkennung extends Thread implements IGUI
{
	protected ArrayList<AColor> farbListe;
	protected final ABufferedImage originalBild;
	
	private boolean run = false;
	
	public Erkennung(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font)
	{
		this.originalBild = new ABufferedImage(bufferedImage);
		this.originalBild.setImage(bufferedImage);
		this.farbListe = farbListe;
		Zeichen.setup(font);
		GuiElements.MainGUI.addTab(this);
		GuiElements.MainGUI.setTab(this);
	}
	
	@Override
	public void run()
	{
		super.run();
		this.run = true;
	}
	
	@Override
	public void gui(Pane pane)
	{
		pane.getChildren().add(this.originalBild.getImageView());
	}
	
	/**
	 * to stop the Thread when the window is closed and the application exit
	 */
	public void close()
	{
		this.run = false;
	}
	
	protected boolean isrunning()
	{
		return this.run;
	}
}
