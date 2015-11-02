package texterkennung.data;

import GUI.GuiElements;
import debug.IDebugger;

public abstract class Data implements IDebugger
{
	public Data()
	{
		GuiElements.getDebugger().add(this);
	}

}
