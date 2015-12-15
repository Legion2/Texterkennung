package texterkennung.operator;

import GUI.GUI;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_ID;

public class Operator_Bildteilen extends Operator
{
	private final Data_ID data_ID_input;
	private final Data_ID data_ID_output1;
	private final Data_ID data_ID_output2;
	
	public Operator_Bildteilen(Data_ID data_ID)
	{
		this.data_ID_input = data_ID;
		this.data_ID_output1 = new Data_ID(this.data_ID_input.getXlenght() / 2, this.data_ID_input.getYlenght(), "linke Seite", true);
		this.data_ID_output2 = new Data_ID(this.data_ID_input.getXlenght() - this.data_ID_output1.getXlenght(), this.data_ID_input.getYlenght(), "rechte Seite", true);
	}
	
	@Override
	public String getName()
	{
		return "Operator-Bildteilen";
	}

	@Override
	public void run()
	{
		for (int y = 0; y < this.data_ID_output1.getYlenght(); y++)
		{
			for (int x = 0; x < this.data_ID_output1.getXlenght(); x++)
			{
				this.data_ID_output1.setInt(x, y, this.data_ID_input.getInt(x, y));
			}
		}
		
		for (int y = 0; y < this.data_ID_output2.getYlenght(); y++)
		{
			for (int x = 0; x < this.data_ID_output2.getXlenght(); x++)
			{
				this.data_ID_output2.setInt(x, y, this.data_ID_input.getInt(this.data_ID_output1.getXlenght() + x, y));
			}
		}
		GUI.MainGUI.setTab(this.data_ID_output1);
		GUI.MainGUI.setTab(this.data_ID_output2);
	}

	@Override
	public Data getData()
	{
		DataList dataList = new DataList("", false);
		dataList.add(this.data_ID_output1);
		dataList.add(this.data_ID_output2);
		return dataList;
	}

}
