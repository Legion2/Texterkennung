package texterkennung;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import debug.IDebugger;

public abstract class Erkennung implements IDebugger {

	protected int parameter1;
	
	protected final ABufferedImage originalBild;
	protected ABufferedImage testBild;
	
	public Erkennung(BufferedImage bufferedImage)
	{
		this.originalBild = new ABufferedImage(bufferedImage);
		this.originalBild.setImage(bufferedImage);
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
