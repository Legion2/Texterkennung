package texterkennung.data;

import GUI.GuiElements;
import GUI.IGUI;

public abstract class Data implements IGUI
{
	private final String name;
	
	public Data()
	{
		GuiElements.MainGUI.addTab(this);
		this.name = "Data";
	}
	
	public Data(String name)
	{
		GuiElements.MainGUI.addTab(this);
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
}
