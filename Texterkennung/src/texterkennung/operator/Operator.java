package texterkennung.operator;

import debug.IDebugger;
import texterkennung.data.Data;

public abstract class Operator implements IDebugger
{
	public abstract void run();
	
	public abstract Data getData();
}
