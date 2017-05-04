package texterkennung.operator;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import GUI.GUI;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;

public class Operator_Raster implements Operator<Data_NPOS>
{
	private final Data_ID data_ID_input;
	private final Data_NPOS data_NPOS_output;
	private final int vergleichsID;
	private final Data_F data_F_input;

	public Operator_Raster(Data_ID data_ID, Data_F data_F)
	{
		this.data_ID_input = data_ID;
		this.data_F_input = data_F;
		this.data_NPOS_output = new Data_NPOS(data_ID, "Data-Raster", true);
		this.vergleichsID = this.data_ID_input.getDefault();
	}

	@Override
	public String getName()
	{
		return "Operator_Raster";
	}

	@Override
	public Data_NPOS get()
	{
		int x, y;

		//Waagerecht
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			x = 0;
			while (x < this.data_ID_input.getXlenght() && this.data_F_input.getFloat(x, y) > 0.5f)
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
		
		ArrayList<CompletableFuture<Void>> list = new ArrayList<CompletableFuture<Void>>();

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
				list.add(this.zeileBerechnen(ystart, yend));
			}
		}
		if (!list.isEmpty())
		{
			list.parallelStream().forEach(CompletableFuture::join);
		}

		GUI.MainGUI.setTab(this.data_NPOS_output);
		
		return this.data_NPOS_output;
	}
	
	private CompletableFuture<Void> zeileBerechnen(int ystart, int yend)
	{
		return CompletableFuture.runAsync(() -> {
			int xstart = 0;
			int xmax = this.data_NPOS_output.getXlenght();
			int j;

			boolean box = false;

			for (int x = 1; x < xmax; x++)
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
				for (int k = xstart; k < xmax; k++)
				{
					this.data_NPOS_output.setNPOS(k, j, xmax - 1, yend - 1);
				}
			}

			this.data_NPOS_output.setNPOS(xmax - 1, yend - 1, xstart, ystart);
		});
	}
}
