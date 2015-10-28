package texterkennung.operator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import advanced.ABufferedImage;
import advanced.AColor;
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
	
	
	@Override
	public String getName()
	{
		return "Verbindungen";
	}

	@Override
	public BufferedImage visualisieren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run()
	{
		
		
		
		this.data_int_output = new Data_int(data_int_input);
		
		this.gruppen[0][0] = 0;
		
		for (int x = 1; x < this.originalBild.getWidth(); x++)
		{
			
		}
		
		for (int y = 1; y < this.originalBild.getHeight(); y++)
		{
			for (int x = 1; x < this.originalBild.getWidth(); x++)
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
