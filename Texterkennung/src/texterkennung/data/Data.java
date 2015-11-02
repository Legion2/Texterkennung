package texterkennung.data;

import GUI.GuiElements;
import GUI.IGUI;

public abstract class Data implements IGUI
{
	public Data()
	{
		GuiElements.MainGUI.addTab(this);
	}
}
