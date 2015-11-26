package texterkennung.operator;

import GUI.GuiElements;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;
import texterkennung.data.Data_Zeichen;

public class Operator_Zeichenzuordnung extends Operator
{
	private Data_ID data_ID_input;
	private Data_NPOS data_NPOS_input;
	private DataList dataList_output;
	private Data_ID data_ID_output;
	
	public Operator_Zeichenzuordnung(Data_ID data_ID, Data_NPOS data_NPOS)
	{
		this.data_ID_input = data_ID;
		this.data_NPOS_input = data_NPOS;
		this.dataList_output = new DataList("Zeichen Liste");
		this.data_ID_output = new Data_ID(data_ID, "Data-Zeichen");
		this.data_ID_output.setDefault(-1);
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichenzuordnung";
	}

	@Override
	public void run()
	{
		int x = 0, y = 0, ID = 0;
		
		for (y = 0; y < this.data_NPOS_input.getYlenght(); y++)
		{
			if (this.data_NPOS_input.getNPOS(0, y)[0] != this.data_NPOS_input.getXlenght() - 1)
			{
				int ystart = y;
				int xstart = 0;
				int xend = this.data_NPOS_input.getNPOS(0, y)[0];
				int yend = this.data_NPOS_input.getNPOS(0, y)[1];
				int ymin = yend;
				int ymax = ystart;
				boolean z = false;//Ob ein Zeichen im Letzten sekor gefunden wurde
				
				for (x = 0; x < this.data_NPOS_input.getXlenght(); x++)
				{
					if (x > xend)//neuer Sektor
					{
						if (z)
						{
							this.dataList_output.add(new Data_Zeichen(ID, xstart, xend, ymin, ymax, this.data_ID_output, "Zeichen Daten: " + ID));
							ID++;
							ymin = yend;
							ymax = ystart;
							
							z = false;
							x = this.data_NPOS_input.getNPOS(x, ystart)[0];
							xstart = x + 1;
							if (xstart < this.data_NPOS_input.getXlenght())
							{
								xend = this.data_NPOS_input.getNPOS(xstart, ystart)[0];
							}
							
							continue;
						}
						else
						{
							xstart = x;
							xend = this.data_NPOS_input.getNPOS(x, ystart)[0];
						}
					}
					
					for (y = ystart; y <= yend; y++)
					{
						if (this.data_ID_input.getInt(x, y) != this.data_ID_input.getDefault())
						{
							z = true;
							
							ymin = ymin > y ? y : ymin;
							ymax = ymax < y ? y : ymax;
							
							this.data_ID_output.setInt(x, y, ID);
						}
					}
				}
				
				y = yend;
			}
		}
		
		this.data_ID_output.setMaxid(ID);
		GuiElements.MainGUI.setTab(this.dataList_output);
		GuiElements.MainGUI.setTab(this.data_ID_output);
	}

	@Override
	public Data getData()
	{
		DataList list = new DataList("return list", false);
		list.add(this.data_ID_output);
		list.add(this.dataList_output);
		return list;
	}
}
