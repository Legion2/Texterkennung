package texterkennung.operator;

import GUI.GUI;
import texterkennung.data.DataList;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;

public class Operator_Bildteilen implements Operator<DataList<Data_ID>>
{
	private final Data_ID data_ID_input;
	private final Data_ID data_ID_output1;
	private final Data_ID data_ID_output2;
	
	public Operator_Bildteilen(Data_ID data_ID)
	{
		this.data_ID_input = data_ID;
		this.data_ID_output1 = new Data_Image(this.data_ID_input.getXlenght() / 2, this.data_ID_input.getYlenght(), "linke Seite", true);
		this.data_ID_output2 = new Data_Image(this.data_ID_input.getXlenght() - this.data_ID_output1.getXlenght(), this.data_ID_input.getYlenght(), "rechte Seite", true);
	}
	
	@Override
	public String getName()
	{
		return "Operator-Bildteilen";
	}

	@Override
	public DataList<Data_ID> get()
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
		
		DataList<Data_ID> dataList = new DataList<Data_ID>("", false);
		dataList.add(this.data_ID_output1);
		dataList.add(this.data_ID_output2);
		return dataList;
	}
}
