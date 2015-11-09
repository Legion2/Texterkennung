package texterkennung.operator;

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
		this.data_NPOS_output = new Data_NPOS(data_ID);
	}

	@Override
	public String getName()
	{
		return "Konvertieren";
	}

	@Override
	public void run()
	{
		//TODO parallelisieren??? möglich ist es
		System.out.println(data_ID_input.getMaxid());
		for (int i = 0; i <= data_ID_input.getMaxid(); i++)
		{
			System.out.println(i);
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
		}
	}

	@Override
	public Data getData()
	{
		return this.data_NPOS_output;
	}
}
