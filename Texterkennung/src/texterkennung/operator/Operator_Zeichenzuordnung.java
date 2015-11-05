package texterkennung.operator;

import GUI.GuiElements;
import texterkennung.data.Data;
import texterkennung.data.Data_NPOS;

public class Operator_Zeichenzuordnung extends Operator
{
	private Data_NPOS data_NPOS_input;
	private Data_NPOS data_NPOS_output;
	
	public Operator_Zeichenzuordnung(Data_NPOS data_NPOS, Data_NPOS data_NPOS2)
	{
		this.data_NPOS_input = data_NPOS;
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichenzuordnung";
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//GuiElements.MainGUI.setTab(this.data_NPOS_output);
	}

	@Override
	public Data getData()
	{
		return this.data_NPOS_output;
	}

}
