package texterkennung.operator;

import java.util.ArrayList;

import GUI.GUI;
import texterkennung.data.Data_ID;

public class Operator_Verbindungen implements Operator<Data_ID>
{
	private final Data_ID data_ID_input;
	private final Data_ID data_ID_output;
	
	public Operator_Verbindungen(Data_ID data_int)
	{
		this.data_ID_input = data_int;
		this.data_ID_output = new Data_ID(data_ID_input, "Data-Verbindungen", true);
	}
	
	@Override
	public String getName()
	{
		return "Verbindungen";
	}

	@Override
	public Data_ID get()
	{
		int x = 0, y = 0, id = -1;
		ArrayList<Integer> masterIDList = new ArrayList<Integer>();
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			for (x = 0; x < this.data_ID_input.getXlenght(); x++)
			{
				if (this.data_ID_input.getInt(x, y) == this.data_ID_input.getDefault()) continue;//Wenn das pixel ein Hintergrundpixel ist nicht beachten und mit dem nächsten weiter machen
				boolean both = false;
				if (this.data_ID_input.getInt(x - 1, y) == this.data_ID_input.getInt(x, y))
				{
					this.data_ID_output.setInt(x, y, this.data_ID_output.getInt(x - 1, y));
					both = true;
				}
				if (this.data_ID_input.getInt(x, y - 1) == this.data_ID_input.getInt(x, y))
				{
					if (both && (this.data_ID_output.getInt(x, y - 1) != this.data_ID_output.getInt(x - 1, y)))
					{
						int key = this.data_ID_output.getInt(x, y - 1);
						int value = this.data_ID_output.getInt(x, y - 1);
						
						if (this.data_ID_output.getInt(x - 1, y) < key)
						{
							key = this.data_ID_output.getInt(x - 1, y);
						}
						else
						{
							value = this.data_ID_output.getInt(x - 1, y);
						}
						
						int verweis = masterIDList.get(value);
						
						if (verweis == value)
						{
							masterIDList.set(value, key);
						}
						else if (verweis != key)
						{//verweisketten kombinieren
							ArrayList<Integer> verweise1 = new ArrayList<Integer>();
							ArrayList<Integer> verweise2 = new ArrayList<Integer>();
							ArrayList<Integer> verweise3 = new ArrayList<Integer>();
							
							int nextkey = key;
							verweise1.add(nextkey);
							while (nextkey != masterIDList.get(nextkey))
							{
								nextkey = masterIDList.get(nextkey);
								verweise1.add(nextkey);
							}
							
							nextkey = value;
							verweise2.add(nextkey);
							while (nextkey != masterIDList.get(nextkey))
							{
								nextkey = masterIDList.get(nextkey);
								verweise2.add(nextkey);
							}
							
							int i1 = 0, i2 = 0;
							
							while (i1 < verweise1.size() && i2 < verweise2.size())
							{
								if (verweise1.get(i1) < verweise2.get(i2))
								{
									verweise3.add(verweise2.get(i2));
									i2++;
								}
								else if (verweise1.get(i1) > verweise2.get(i2))
								{
									verweise3.add(verweise1.get(i1));
									i1++;
								}
								else
								{
									break;
								}
							}
							
							if (i1 < verweise1.size())
							{
								verweise3.add(verweise1.get(i1));
							}
							else
							{
								verweise3.add(verweise2.get(i2));
							}
							
							int nextindex = verweise3.get(0);
							for (int i = 1; i < verweise3.size(); i++)
							{
								masterIDList.set(nextindex, verweise3.get(i));
								nextindex = verweise3.get(i);
							}
						}
					}
					else
					{
						this.data_ID_output.setInt(x, y, this.data_ID_output.getInt(x, y - 1));
						both = true;
					}
				}
				
				if (!both)
				{
					id++;
					masterIDList.add(id);
					this.data_ID_output.setInt(x, y, id);
				}
			}
		}
		
		for (int i = 0; i < masterIDList.size(); i++)
		{
			masterIDList.set(i, masterIDList.get(masterIDList.get(i)));
		}
		
		
		// Master ID setzen
		
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			for (x = 0; x < this.data_ID_input.getXlenght(); x++)
			{
				if (this.data_ID_input.getInt(x, y) == this.data_ID_input.getDefault()) continue;//Wenn das pixel ein Hintergrundpixel ist nicht beachten und mit dem nächsten weiter machen
				this.data_ID_output.setInt(x, y, masterIDList.get(this.data_ID_output.getInt(x, y)));
			}
		}
		
		this.data_ID_output.setMaxid(id);
		GUI.MainGUI.setTab(this.data_ID_output);
		
		return this.data_ID_output;
	}
}
