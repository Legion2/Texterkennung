package texterkennung;

import java.awt.Font;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import GUI.IConfigurable;
import advanced.AColor;
import debug.Debugger;
import debug.IInfo;
import jogl.OpenGLHandler;
import texterkennung.data.DataList;
import texterkennung.data.Data_Image;
import texterkennung.data.Data_Zeichen;
import texterkennung.operator.Operator_Zeichengenerieren;

public abstract class Erkennung implements IInfo, IConfigurable, Runnable
{
	protected static final String standartZeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(),.;:!?‰ˆ¸ƒ÷‹ﬂ-";

	private String name;
	
	//Config
	private final String config;
	protected boolean schwarzweiﬂ;
	protected boolean gpu;
	protected Font font;
	protected int schwellwert;
	protected ArrayList<AColor> farbListe;
	protected float scale;
	
	protected final Data_Image originalBild;
	protected final OpenGLHandler openGLHandler;
	
	/**
	 * Liste mit den generierten vergleichs Zeichen
	 */
	protected CompletableFuture<DataList<Data_Zeichen>> generierteZeichenliste;
	
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
		this.run = true;
		Debugger.info(this, "run");
		
		//Generiert den standart Zeichensatz um diese mit den im Bild vorkommenden zu vergleichen
		Operator_Zeichengenerieren OZG = new Operator_Zeichengenerieren(standartZeichen, this.font, this.schwarzweiﬂ);
		if (!this.isrunning()) return;
		this.generierteZeichenliste = CompletableFuture.supplyAsync(OZG);
		
		Debugger.info(this, "Zeichengenerieren fertig");
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
				this.schwarzweiﬂ = par[i].equals("true");
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
			case 5:
				this.scale = Integer.valueOf(par[i]) / 100.0f;
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

	@Override
	public String getName()
	{
		return this.name;
	}
	
	protected void setName(String string)
	{
		this.name = string;
	}
}
