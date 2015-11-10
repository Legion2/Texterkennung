package texterkennung.operator;

import java.util.ArrayList;

import GUI.GuiElements;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;

public class Operator_Verbindungen extends Operator
{
	private Data_ID data_ID_input;
	private Data_ID data_ID_output;
	
	public Operator_Verbindungen(Data_ID data_int)
	{
		this.data_ID_input = data_int;
		this.data_ID_output = new Data_ID(data_ID_input, "Data-Verbindungen");
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
		ArrayList<Integer> masterIDList = new ArrayList<Integer>();
		masterIDList.add(id);
		//HashMap<Integer, ArrayList<Integer>> zuordung = new HashMap<Integer, ArrayList<Integer>>();
		
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			for (x = 0; x < this.data_ID_input.getXlenght(); x++)
			{
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
						
						
						
						/* HAT FEHLER
						if (zuordung.containsKey(key))
						{
							if (!zuordung.get(key).contains(value))
							{
								ArrayList<Integer> list = zuordung.get(key);
								list.add(value);
								if (zuordung.containsKey(value))
								{
									for (int connected_value : zuordung.get(value))
									{
										list.add(connected_value);
									}
									zuordung.remove(value);
								}
							}
						}
						else
						{
							ArrayList<Integer> list = new ArrayList<Integer>();
							list.add(value);
							if (zuordung.containsKey(value))
							{
								for (int connected_value : zuordung.get(value))
								{
									list.add(connected_value);
								}
								zuordung.remove(value);
							}
							zuordung.put(key, list);
						}
						*/
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
				this.data_ID_output.setInt(x, y, masterIDList.get(this.data_ID_output.getInt(x, y)));
			}
		}
		
		
		
		//Invert HashMap
		/*
		int[] master_ID = new int[id + 1];
		
		for (Integer key : zuordung.keySet())
		{
			for (int value : zuordung.get(key))
			{
				master_ID[value] = key;
			}
			master_ID[key] = key;
		}
		*/
		// Master ID setzen
		/*
		for (y = 0; y < this.data_ID_input.getYlenght(); y++)
		{
			for (x = 0; x < this.data_ID_input.getXlenght(); x++)
			{
				this.data_ID_output.setInt(x, y, master_ID[this.data_ID_output.getInt(x, y)]);
			}
		}
		*/
		
		/* Nicht das gewünschte resultat
		for (y = this.data_ID_input.getYlenght()-1; y >= 0; y--)//Invertiert
		{
			for (x = this.data_ID_input.getXlenght()-1; x >= 0; x--)
			{
				if (this.data_ID_input.getInt(x + 1, y) == this.data_ID_input.getInt(x, y))
				{
					this.data_ID_output.setInt(x, y, this.data_ID_output.getInt(x + 1, y));
				}
				else if (this.data_ID_input.getInt(x, y + 1) == this.data_ID_input.getInt(x, y))
				{
					this.data_ID_output.setInt(x, y, this.data_ID_output.getInt(x, y + 1));
				}
			}
		}*/
		
		
		
		this.data_ID_output.setMaxid(id);
		// TODO kann man das besser machen?
		GuiElements.MainGUI.setTab(this.data_ID_output);
	}


	@Override
	public Data getData()
	{
		return this.data_ID_output;
	}
}
