package texterkennung.operator;

import debug.IInfo;
import texterkennung.data.Data;

public abstract class Operator implements IInfo
{
	public abstract void run();
	
	public abstract Data getData();
}
