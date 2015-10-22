package Main;

import texterkennung.Erkennung;
import texterkennung.Zeichen;

public class Programm
{
	public Erkennung erkennung;
	
	public int parameter1;
	public int parameter2;
	public int parameter3;
	public int parameter4;
	
	public Programm()
	{
		Fenster f = new Fenster(this);
        f.setVisible(true);
        Zeichen.setup();
	}
}
