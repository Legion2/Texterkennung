package Main;

import java.util.ArrayList;

import GUI.IGUI;
import debug.Debugger;
import texterkennung.Erkennung;

public class Programm
{
	public int parameter1;
	public int parameter2;
	public int parameter3;
	public int parameter4;
	public Erkennung erkennung;//wird gelöscht
	
	public Programm()
	{
		ArrayList<IGUI> IGUIList = new ArrayList<IGUI>();
		IGUIList.add(new ProgrammOutput());
		IGUIList.add(new Debugger());
		//TODO Gui ist zu implementieren
		//GUI gui = new GUI(IGUIList);
	}
}
