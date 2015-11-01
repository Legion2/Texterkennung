package texterkennung.operator;

import texterkennung.data.Data;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;

public class Operator_Raster extends Operator
{
	private Data_ID data_ID_input;
	private Data_NPOS data_NPOS_output;
	private final int vergleichsID;
	
	public Operator_Raster(Data_ID data_ID, int vergleichsID)
	{
		this.data_ID_input = data_ID;
		this.data_NPOS_output = new Data_NPOS(data_ID);
		this.vergleichsID = vergleichsID;
	}

	@Override
	public String getName()
	{
		return "Operator_Raster";
	}

	@Override
	public void run()
	{
		int x,y;
		
		//Waagerecht
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			x=0;
			while (x < this.data_ID_input.getXlenght() && this.data_ID_input.getInt(x, y) == vergleichsID)
			{
				x++;
			}
			
			if (x == this.data_ID_input.getXlenght())
			{
				for (int i = 0; i < this.data_NPOS_output.getXlenght() - 1; i++)
				{
					this.data_NPOS_output.setNPOS(i, y, this.data_NPOS_output.getXlenght() - 1, y);
				}
				this.data_NPOS_output.setNPOS(this.data_NPOS_output.getXlenght() - 1, y, 0, y);
			}
		}
		
		//Senkrecht
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			int ystart = y;
			while (y < this.data_ID_input.getYlenght() && this.data_NPOS_output.getNPOS(0, y)[0] != this.data_NPOS_output.getXlenght() - 1)
			{
				y++;
			}
			int yend = y;
			
			int xstart = -1, xend = -1;
			
			for (x = 0; x < this.data_NPOS_output.getXlenght(); x++)
			{
				int j = ystart;
				while (j < yend && this.data_ID_input.getInt(x, j) == vergleichsID)
				{
					j++;
				}
				
				if (j == yend)
				{
					if (xstart == -1)
					{
						xstart = x;
					}
					
					xend = x;
				}
				else
				{
					if (xstart != -1)
					{
						for (j = ystart; j < yend; j++)
						{
							for (int k = xstart ; k <= xend; k++)
							{
								this.data_NPOS_output.setNPOS(k, j, xend, yend - 1);
							}
						}
						
						this.data_NPOS_output.setNPOS(xend, yend - 1, xstart, ystart);
						
						xstart = xend = -1;
					}
				}
			}
		}
		
	}

	@Override
	public Data getData()
	{
		return this.data_NPOS_output;
	}

}
