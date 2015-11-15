package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import advanced.AColor;
import debug.Debugger;
import javafx.scene.layout.Pane;

public class Erkennung_Vertretungsplan extends Erkennung
{
	public Erkennung_Vertretungsplan(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font, GL4 gl4)
	{
		super(bufferedImage, farbListe, font, gl4);
	}

	@Override
	public void run()
	{
		Debugger.info(this, "run");
	}

	@Override
	public void gui(Pane pane)
	{
		//TODO
	}
}
