package texterkennung.operator;

import GUI.GUI;
import debug.Debugger;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;

public class Operator_IDtoNPOS extends Operator
{
	private Data_ID data_ID_input;
	private Data_NPOS data_NPOS_output;
	
	public Operator_IDtoNPOS(Data_ID data_ID)
	{
		this.data_ID_input = data_ID;
		this.data_NPOS_output = new Data_NPOS(data_ID, "Data-NPOS", true);
	}

	@Override
	public String getName()
	{
		return "Konvertieren";
	}

	@Override
	public void run()
	{
		Debugger.info(this, "" + this.data_ID_input.getMaxid());
		int p = 0;
		for (int i = 0; i <= data_ID_input.getMaxid(); i++)
		{
			int lastindex_x = -1, lastindex_y = -1;
			int firstindex_x = -1, firstindex_y = -1;
			
			for (int y = 0; y < data_ID_input.getYlenght(); y++)
			{
				for (int x = 0; x < data_ID_input.getXlenght(); x++)
				{
					if (data_ID_input.getInt(x, y) == i)
					{
						if (lastindex_x == -1)
						{
							firstindex_x = lastindex_x = x;
							firstindex_y = lastindex_y = y;
						}
						else
						{
							this.data_NPOS_output.setNPOS(lastindex_x, lastindex_y, x, y);
							lastindex_x = x;
							lastindex_y = y;
						}
					}
				}
			}
			
			if (lastindex_x != -1)
			{
				this.data_NPOS_output.setNPOS(lastindex_x, lastindex_y, firstindex_x, firstindex_y);
			}
			
			int j = (i * 20) / this.data_ID_input.getMaxid();
			if (j > p)
			{
				p = j;
				Debugger.info(this, "Konvertierung bei " + (j * 5) + "%");
			}
		}
		GUI.MainGUI.setTab(this.data_NPOS_output);
	}

	@Override
	public Data getData()
	{
		return this.data_NPOS_output;
	}
}
