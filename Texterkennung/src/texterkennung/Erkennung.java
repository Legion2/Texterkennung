package texterkennung;

import java.awt.Font;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import advanced.AColor;
import debug.Debugger;
import debug.IInfo;
import texterkennung.data.Data_Image;

public abstract class Erkennung extends Thread implements IInfo
{
	public static final String standartZeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(),.;:!?";
	
	protected ArrayList<AColor> farbListe;
	protected final Data_Image originalBild;
	protected final Font font;

	protected GL4 gl4;
	
	private boolean run = false;
	
	public Erkennung(Data_Image data_Image, ArrayList<AColor> farbListe, Font font, GL4 gl4)
	{
		this.originalBild = data_Image;
		this.farbListe = farbListe;
		this.font = font;
		this.gl4 = gl4;
		Debugger.info(this, "GPU: " + this.gl4);
	}
	
	@Override
	public void run()
	{
		super.run();
		this.run = true;
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
