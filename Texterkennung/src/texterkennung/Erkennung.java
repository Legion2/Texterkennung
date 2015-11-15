package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import GUI.GuiElements;
import GUI.IGUI;
import advanced.ABufferedImage;
import advanced.AColor;
import javafx.scene.layout.Pane;

public abstract class Erkennung extends Thread implements IGUI
{
	public static final String standartZeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(),.;:!?";
	
	protected ArrayList<AColor> farbListe;
	protected final ABufferedImage originalBild;
	protected final Font font;

	protected GL4 gl4;
	
	private boolean run = false;
	
	public Erkennung(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font, GL4 gl4)
	{
		this.originalBild = new ABufferedImage(bufferedImage);
		this.originalBild.setImage(bufferedImage);
		this.farbListe = farbListe;
		this.font = font;
		this.gl4 = gl4;
		GuiElements.MainGUI.addTab(this);
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
	
	protected boolean gpu()
	{
		return (this.gl4 != null);
	}
	
	/**
	 * to stop the Thread when the window is closed and the application exit
	 */
	public void close()
	{
		this.run = false;
	}
	
	public boolean isrunning()
	{
		return this.run;
	}
}
