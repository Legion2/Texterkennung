package texterkennung;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import advanced.ABufferedImage;

public class Zeichen 
{
	//Parameter
	private final static String standartZeichen = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private final static int v = 40, h = 20; 
	
	//Liste mit Zeichen
	public static HashMap<String, Zeichen> zeichenListe;
	
	//Variablen
	private final char zeichen;
	private ABufferedImage zeichenBild;
	private int[] snow_v;
	private int[] snow_h;
	
	public Zeichen(char c, Font f)
	{
		this.zeichen = c;
		//Zeichen zeichnen
		zeichenBild = new ABufferedImage(h, v, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g = zeichenBild.createGraphics();
		//g.setFont(g.getFont().deriveFont(30f));
		g.setFont(f);
		g.drawString(String.valueOf(c), 0, 20);
		
		//Summe erstellen
		zeichenBild.prepare();
		
		//Schneefall ausrechnen
		snow_v = new int[v];
		snow_h = new int[h];
		for (int i = 0; i < v; i++)
		{
			snow_v[i] = zeichenBild.getSumme(h-1, i) - zeichenBild.getSumme(h-1, i-1);
		}
		for (int i = 0; i < h; i++)
		{
			snow_h[i] = zeichenBild.getSumme(i, h) - zeichenBild.getSumme(i-1, h-1);
		}
	}
	
	public static void setup(Font f)
	{
		zeichenListe = new HashMap<String, Zeichen>();
		for (int i = 0; i < standartZeichen.length(); i++)
		{
			add(standartZeichen.charAt(i), f);
		}
	}
	
	public static void add(char c, Font f)
	{
		zeichenListe.put(String.valueOf(c), new Zeichen(c, f));
	}
	
	public BufferedImage getBufferedImage() {
		return this.zeichenBild;
	}
	
	public char getZeichen() {
		return zeichen;
	}
}
