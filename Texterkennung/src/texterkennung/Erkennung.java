package texterkennung;

import java.awt.Font;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import GUI.IConfigurable;
import advanced.AColor;
import debug.Debugger;
import debug.IInfo;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jogl.OpenGLHandler;
import texterkennung.data.Data_Image;

public abstract class Erkennung extends Thread implements IInfo, IConfigurable
{
	protected static final String standartZeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(),.;:!?";
	
	//Config
	private final String config;
	protected boolean schwarzweiß;
	protected boolean gpu;
	protected Font font;
	protected int schwellwert;
	protected ArrayList<AColor> farbListe;
	
	
	protected final Data_Image originalBild;
	protected final OpenGLHandler openGLHandler;
	
	/**
	 * indicates whether the Thread is running
	 */
	private boolean run = false;
	
	public Erkennung(Data_Image data_Image, OpenGLHandler openGLHandler, String parameter)
	{
		this.originalBild = data_Image;
		this.openGLHandler = openGLHandler;
		this.config = parameter;
		this.setConfig(parameter);
	}
	
	@Override
	public void run()
	{
		super.run();
		this.run = true;
		Debugger.info(this, "run");
		if (this.gpu) this.openGLHandler.getGL4();
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
			switch (i)
			{
			case 0:
				this.schwarzweiß = par[i].equals("true");
				break;
			case 1:
				this.gpu = par[i].equals("true");
				break;
			case 2:
				this.font = new Font(par[i], Font.PLAIN, 30);
				break;
			case 3:
				this.schwellwert = Integer.valueOf(par[i]);
				break;
			case 4:
				String[] s = par[i].split(":");
				this.farbListe = new ArrayList<AColor>();
				for (int j = 0; j < s.length; j++)
				{
					this.farbListe.add(new AColor(Integer.valueOf(s[j])));
				}
				break;
			default:
				Debugger.error(this, "Config out of range. i = " + i);
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
