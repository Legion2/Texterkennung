package advanced;

import java.awt.Color;

@SuppressWarnings("serial")
public class AColor extends Color
{
	public static final int weiß = new Color(255, 255, 255).getRGB();
	public static final int schwarz = new Color(0, 0, 0).getRGB();
	

	public AColor(int r, int g, int b)
	{
		super(r, g, b);
	}
	
	public AColor(int argb)
	{
		super(argb);
	}
	
	public boolean isColor(int argb2, int schwellwert)
	{
		int argb = this.getRGB();
		int rot = (argb >> 16) & 0xff;
		int gruen = (argb >> 8)  & 0xff;
		int blau = (argb) & 0xff;
		
		int rot2 = (argb2 >> 16) & 0xff;
		int gruen2 = (argb2 >> 8)  & 0xff;
		int blau2 = (argb2) & 0xff;
		
		double l = Math.pow(rot - rot2, 2) + Math.pow(gruen - gruen2, 2) + Math.pow(blau - blau2, 2);
		
		return l < Math.pow(schwellwert, 2);
	}
	
	public float fColor(int argb2, int schwellwert)
	{
		int argb = this.getRGB();
		int rot = (argb >> 16) & 0xff;
		int gruen = (argb >> 8)  & 0xff;
		int blau = (argb) & 0xff;
		
		int rot2 = (argb2 >> 16) & 0xff;
		int gruen2 = (argb2 >> 8)  & 0xff;
		int blau2 = (argb2) & 0xff;
		
		double l = Math.pow(rot - rot2, 2) + Math.pow(gruen - gruen2, 2) + Math.pow(blau - blau2, 2);
		
		return (float) (l / Math.pow(schwellwert, 2));
	}
}
