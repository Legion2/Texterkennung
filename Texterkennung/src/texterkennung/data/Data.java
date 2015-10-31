package texterkennung.data;

import GUI.MainGUI;
import debug.IDebugger;

public abstract class Data implements IDebugger
{
	public Data()
	{
		MainGUI.getDebugger().add(this);
	}

}
