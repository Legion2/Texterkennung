package texterkennung;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import advanced.ABufferedImage;
import advanced.AColor;
import debug.IDebugger;

public abstract class Erkennung implements IDebugger {

	protected ArrayList<AColor> farbListe;
	
	protected final ABufferedImage originalBild;
	protected ABufferedImage testBild;
	
	public Erkennung(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font)
	{
		this.originalBild = new ABufferedImage(bufferedImage);
		this.originalBild.setImage(bufferedImage);
		this.farbListe = farbListe;
		Zeichen.setup(font);
	}

	public abstract void run(int par1, int par2);

	public abstract void gui(Graphics g, int w);
	
	public ABufferedImage getTestBild() {
		return testBild;
	}
	
	public ABufferedImage getOriginalBild() {
		return originalBild;
	}
}
