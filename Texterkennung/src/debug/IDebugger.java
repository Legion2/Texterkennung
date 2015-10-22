package debug;

import java.awt.image.BufferedImage;

public interface IDebugger 
{
	String getName();
	
	BufferedImage visualisieren();
}
