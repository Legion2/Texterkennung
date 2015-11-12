package texterkennung.data;

import GUI.GuiElements;
import GUI.IGUI;

public abstract class Data implements IGUI
{
	private final String name;
	
	
	public Data(String name, boolean tab)
	{
		this.name = name;
		if (tab) GuiElements.MainGUI.addTab(this);
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
}
