package texterkennung.operator;

import texterkennung.data.Data;
import texterkennung.data.Data_ID;

public class Operator_Verbindungen extends Operator
{
	private Data_ID data_int_input;
	private Data_ID data_int_output;
	
	
	public Operator_Verbindungen(Data_ID data_int)
	{
		this.data_int_input = data_int;
	}
	
	@Override
	public String getName()
	{
		return "Verbindungen";
	}

	@Override
	public void run()
	{
		int x = 0, y = 0, id = 0;
		this.data_int_output = new Data_ID(data_int_input);
		
		for (y = 0; y < this.data_int_input.getYlenght(); y++)
		{
			for (x = 0; x < this.data_int_input.getXlenght(); x++)
			{
				if (this.data_int_input.getInt(x - 1, y) == this.data_int_input.getInt(x, y))
				{
					this.data_int_output.setInt(x, y, this.data_int_output.getInt(x - 1, y));
				}
				else if (this.data_int_input.getInt(x, y - 1) == this.data_int_input.getInt(x, y))
				{
					this.data_int_output.setInt(x, y, this.data_int_output.getInt(x, y - 1));
				}
				else
				{
					id++;
					this.data_int_output.setInt(x, y, id);
				}
			}
		}
		
		this.data_int_output.setMaxid(id);
		// TODO testen!!!
		// TODO noch nichtfertig
	}


	@Override
	public Data getData()
	{
		return this.data_int_output;
	}
}
