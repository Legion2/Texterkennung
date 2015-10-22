package texterkennung;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ABufferedImage extends BufferedImage
{
	private int[][] summeRot;
	private int[][] summeGruen;
	private int[][] summeBlau;
	
	private ABufferedImage differenzBild;
	
	public ABufferedImage(int width, int height, int imageType)
	{
		super(width, height, imageType);
		
		this.summeRot = new int[this.getWidth()] [this.getHeight()];
		if (imageType != BufferedImage.TYPE_BYTE_GRAY)
		{
			this.summeGruen = new int[this.getWidth()] [this.getHeight()];
			this.summeBlau = new int[this.getWidth()] [this.getHeight()];
		}
	}
	
	public ABufferedImage(BufferedImage bufferedImage)
	{
		super(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
		
		this.summeRot = new int[this.getWidth()] [this.getHeight()];
		if (bufferedImage.getType() != BufferedImage.TYPE_BYTE_GRAY)
		{
			this.summeGruen = new int[this.getWidth()] [this.getHeight()];
			this.summeBlau = new int[this.getWidth()] [this.getHeight()];
		}
	}
	
	public void setImage(BufferedImage bufferedImage)
	{
		this.setData(bufferedImage.getData());
		this.prepare();
	}
	
	public void prepare()
	{
		summe();
		differenz();
	}
	
	private void summe()
	{
		int x = 0, y = 0;
		int rot = 0, gruen = 0, blau = 0;
		for (x = 0; x < this.getWidth(); x++)
		{
			rot += this.getRot(x, y);
			this.summeRot[x][y] = rot;
			
			if (this.getType() != BufferedImage.TYPE_BYTE_GRAY)
			{
				gruen += this.getGruen(x, y);
				blau += this.getBlau(x, y);
				
				this.summeGruen[x][y] = gruen;
				this.summeBlau[x][y] = blau;
			}
		}
		
		for (y = 1; y < this.getHeight(); y++)
		{
			rot = 0;
			gruen = 0;
			blau = 0;
			
			for (x = 0; x < this.getWidth(); x++)
			{
				rot += this.getRot(x, y);
				
				this.summeRot[x][y] = this.summeRot[x][y-1] + rot;
				if (this.getType() != BufferedImage.TYPE_BYTE_GRAY)
				{
					gruen += this.getGruen(x, y);
					blau += this.getBlau(x, y);
					
					this.summeGruen[x][y] = this.summeGruen[x][y-1] + gruen;
					this.summeBlau[x][y] = this.summeBlau[x][y-1] + blau;
				}
			}
		}
	}

	private void differenz()
	{
		this.differenzBild = new ABufferedImage(this.getWidth(), this.getHeight(), this.getType());
		
		for (int x = 0; x < this.getWidth(); x++)
		{
			for (int y = 0; y < this.getHeight(); y++)
			{
				int rgb = 0;
				int rot = this.getRot(x, y);
				float d = getDurchschnitt(x, y, 1, this.summeRot);//Z
				int diff_rot = (int) Math.abs(d - rot);
				
				if (this.getType() != BufferedImage.TYPE_BYTE_GRAY)
				{
					int diff_gruen = (int) Math.abs(getDurchschnitt(x, y, 1, this.summeGruen) - this.getGruen(x, y));
					int diff_blau = (int) Math.abs(getDurchschnitt(x, y, 1, this.summeBlau) - this.getBlau(x, y));
					rgb = new Color(diff_rot,diff_gruen,diff_blau).getRGB();
				}
				else
				{
					rgb = new Color(diff_rot,diff_rot,diff_rot).getRGB();
				}
				
				this.differenzBild.setRGB(x, y, rgb);
			}
		}
	}
	
	private float getDurchschnitt(int x, int y, int r, int[][] summe)
	{
		return getDurchschnitt(x-r, y-r, x+r, y+r, summe);
	}
	
	private float getDurchschnitt(int xmin, int ymin, int xmax, int ymax, int[][] summe)
	{
		xmin = (xmin < 0) ? 0 : xmin;
		ymin = (ymin < 0) ? 0 : ymin;
		xmax = (xmax >= this.getWidth()) ? this.getWidth()-1 : xmax;
		ymax = (ymax >= this.getHeight()) ? this.getHeight()-1 : ymax;
		
		int anzahl = (xmax - xmin + 1) * (ymax - ymin + 1);
		
		int s1 = getSumme(xmax, ymax, summe);
		int s2 = getSumme(xmin-1, ymin-1, summe);
		int s3 = getSumme(xmin-1, ymax, summe);
		int s4 = getSumme(xmax, ymin-1, summe);
		
		int sum = s1 + s2 - s3 - s4;
		
		return sum / anzahl;
	}
	
	public double getDurchschnitt(int x, int y, int r)
	{
		return Math.sqrt(Math.pow(getDurchschnitt(x-r, y-r, x+r, y+r, this.summeRot), 2) + Math.pow(getDurchschnitt(x-r, y-r, x+r, y+r, this.summeGruen), 2) + Math.pow(getDurchschnitt(x-r, y-r, x+r, y+r, this.summeBlau), 2));
	}

	public ABufferedImage getDifferenzBild() {
		return differenzBild;
	}
	
	public int getSumme(int x, int y)
	{
		return (x < 0 || y < 0) ? 0 : this.summeRot[x][y];
	}
	
	private int getSumme(int x, int y, int[][] summe)
	{
		return (x < 0 || y < 0) ? 0 : summe[x][y];
	}
	
	public int getRot(int x, int y)
	{
		int argb  = this.getRGB(x, y);
		return (argb >> 16) & 0xff;
	}
	
	public int getGruen(int x, int y)
	{
		int argb  = this.getRGB(x, y);
		return(argb >> 8)  & 0xff;
	}
	
	public int getBlau(int x, int y)
	{
		int argb  = this.getRGB(x, y);
		return (argb) & 0xff;
	}
	
	public int getAlpha(int x, int y)
	{
		int argb  = this.getRGB(x, y);
		return (argb >> 24) & 0xff;
	}
}
