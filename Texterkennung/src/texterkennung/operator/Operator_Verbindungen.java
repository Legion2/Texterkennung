package texterkennung.operator;

import texterkennung.data.Data;
import texterkennung.data.Data_int;

public class Operator_Verbindungen extends Operator
{
	private Data_int data_int_input;
	private Data_int data_int_output;
	
	
	public Operator_Verbindungen(Data_int data_int)
	{
		this.data_int_input = data_int;
	}
	
	
	
	public String getName()
	{
		return "Verbindungen";
	}

	@Override
	public void run()
	{
		
		this.data_int_output = new Data_int(data_int_input);
		
		this.data_int_output.setInt(0, 0, 0);
		
		for (int x = 1; x < this.data_int_input.getXlenght(); x++)
		{
			
		}
		
		for (int y = 1; y < this.data_int_input.getYlenght(); y++)
		{
			for (int x = 1; x < this.data_int_input.getXlenght(); x++)
			{
				
			}
		}
		
		// TODO nichtfertig
	}


	@Override
	public Data getData()
	{
		return this.data_int_output;
	}
}
