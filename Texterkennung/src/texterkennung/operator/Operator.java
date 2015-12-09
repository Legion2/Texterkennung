package texterkennung.operator;

import debug.IInfo;
import texterkennung.data.Data;

public abstract class Operator implements IInfo
{
	/**
	 * Methode in der die Berechnung gemacht wird
	 */
	public abstract void run();
	
	/**
	 * 
	 * @return Die Daten die berechnet wurden.
	 */
	public abstract Data getData();
}
