package texterkennung;

import java.awt.Font;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import advanced.AColor;
import debug.Debugger;
import texterkennung.data.Data_Image;

public class Erkennung_Vertretungsplan extends Erkennung
{
	public Erkennung_Vertretungsplan(Data_Image data_Image, ArrayList<AColor> farbListe, Font font, GL4 gl4)
	{
		super(data_Image, farbListe, font, gl4);
	}

	@Override
	public void run()
	{
		Debugger.info(this, "run");
	}
}
