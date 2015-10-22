package texterkennung;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Erkennung_Text extends Erkennung
{

	public Erkennung_Text(BufferedImage bufferedImage, ArrayList<Color> farbListe)
	{
		super(bufferedImage, farbListe);
	}

	@Override
	public String getName()
	{
		return "Erkennung_Text";
	}

	@Override
	public BufferedImage visualisieren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(int par1, int par2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gui(Graphics g, int w) {
		// TODO Auto-generated method stub
		
	}
}
