package texterkennung.data;

import GUI.GUI;
import GUI.IGUI;

public abstract class Data implements IGUI
{
	private final String name;
	
	/**
	 * Data Objetc für verschiedene Daten
	 * @param name Anzeige Name in der GUI
	 * @param tab Gibt an ob die Daten als Tab in der Gui angezeigt werden
	 */
	public Data(String name, boolean tab)
	{
		this.name = name;
		if (tab) GUI.MainGUI.addTab(this);
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
}
