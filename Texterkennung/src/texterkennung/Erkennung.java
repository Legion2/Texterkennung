package texterkennung;

import java.awt.Font;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import GUI.IConfigurable;
import advanced.AColor;
import debug.Debugger;
import debug.IInfo;
import jogl.OpenGLHandler;
import texterkennung.data.Data_Image;

public abstract class Erkennung extends Thread implements IInfo, IConfigurable
{
	public static final String standartZeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(),.;:!?";
	
	private final String config;
	
	protected ArrayList<AColor> farbListe;
	protected final Data_Image originalBild;
	protected Font font;
	protected boolean schwarzweiﬂ;

	protected final OpenGLHandler openGLHandler;
	protected GL4 gl4;
	
	private boolean run = false;
	
	
	
	public Erkennung(Data_Image data_Image, OpenGLHandler openGLHandler, String parameter)
	{
		this.originalBild = data_Image;
		this.openGLHandler = openGLHandler;
		this.config = parameter;
		this.setConfig(parameter);
		Debugger.info(this, "GPU: " + this.gl4);
	}
	
	@Override
	public void run()
	{
		super.run();
		this.run = true;
		Debugger.info(this, "run");
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
	
	@Override
	public void setConfig(String parameter)
	{
		String[] par = parameter.split(";");
		for (int i = 0; i < par.length; i++)
		{
			String s = par[i];
			switch (i)
			{
			case 0:
				this.schwarzweiﬂ = s == "true";
				break;

			default:
				break;
			}
			
		}
	}
	
	@Override
	public String getConfig()
	{
		return this.config;
	}
}
