package texterkennung.operator;

import GUI.GuiElements;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;

public class Operator_Raster extends Operator
{
	private Data_ID data_ID_input;
	private Data_NPOS data_NPOS_output;
	private final int vergleichsID;

	public Operator_Raster(Data_ID data_ID)
	{
		this.data_ID_input = data_ID;
		this.data_NPOS_output = new Data_NPOS(data_ID, "Data-Raster");
		this.vergleichsID = this.data_ID_input.getDefault();
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
			while (x < this.data_ID_input.getXlenght() && this.data_ID_input.getInt(x, y) == this.vergleichsID)
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
			if (ystart < yend)
			{
				int xstart = 0;
				int j;

				boolean box = false;

				for (x = 1; x < this.data_NPOS_output.getXlenght(); x++)
				{
					j = ystart;
					while (j < yend && this.data_ID_input.getInt(x, j) == this.vergleichsID)
					{
						j++;
					}

					if (j == yend)
					{
						if (box)
						{
							for (j = ystart; j < yend; j++)
							{
								for (int k = xstart; k < x; k++)
								{
									this.data_NPOS_output.setNPOS(k, j, x - 1, yend - 1);
								}
							}

							this.data_NPOS_output.setNPOS(x - 1, yend - 1, xstart, ystart);

							box = false;
							xstart = x;
						}
					}
					else
					{
						if (!box)
						{
							for (j = ystart; j < yend; j++)
							{
								for (int k = xstart; k < x; k++)
								{
									this.data_NPOS_output.setNPOS(k, j, x - 1, yend - 1);
								}
							}

							this.data_NPOS_output.setNPOS(x - 1, yend - 1, xstart, ystart);

							box = true;
							xstart = x;
						}
					}
				}

				for (j = ystart; j < yend; j++)
				{
					for (int k = xstart; k < x; k++)
					{
						this.data_NPOS_output.setNPOS(k, j, x - 1, yend - 1);
					}
				}

				this.data_NPOS_output.setNPOS(x - 1, yend - 1, xstart, ystart);
			}
		}

		GuiElements.MainGUI.setTab(this.data_NPOS_output);
	}

	@Override
	public Data getData()
	{
		return this.data_NPOS_output;
	}

}
