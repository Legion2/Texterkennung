package texterkennung.operator;

import GUI.GUI;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;
import texterkennung.data.Data_Zeichen;

public class Operator_Zeichenzuordnung implements Operator<DataList<Data>>
{
	private Data_F data_F_input;
	private Data_NPOS data_NPOS_input;
	private DataList<DataList<DataList<Data_Zeichen>>> dataList_output;
	private Data_ID data_ID_output;
	
	private final boolean schwarzweiﬂ;
	
	public Operator_Zeichenzuordnung(Data_F data_F, Data_NPOS data_NPOS, boolean schwarzweiﬂ)
	{
		this.data_F_input = data_F;
		this.data_NPOS_input = data_NPOS;
		this.dataList_output = new DataList<DataList<DataList<Data_Zeichen>>>("Zeichen Liste", true);
		this.data_ID_output = new Data_ID(data_F, "Data-Zeichen", true);
		this.schwarzweiﬂ = schwarzweiﬂ;
	}

	@Override
	public String getName()
	{
		return "Operator_Zeichenzuordnung";
	}

	@Override
	public DataList<Data> get()
	{
		int x = 0, y = 0, ID = 0, zeilennummer = 0;

		for (y = 0; y < this.data_NPOS_input.getYlenght(); y++)
		{
			if (this.data_NPOS_input.getNPOS(0, y)[0] != this.data_NPOS_input.getXlenght() - 1) {
				// neue Zeile
				DataList<DataList<Data_Zeichen>> zeile = new DataList<DataList<Data_Zeichen>>("Zeile " + zeilennummer, false);
				zeilennummer++;
				this.dataList_output.add(zeile);
				DataList<Data_Zeichen> wort = new DataList<Data_Zeichen>("Wort", false);

				int ystart = y;
				int xstart = 0;
				int xend = this.data_NPOS_input.getNPOS(0, y)[0];
				int yend = this.data_NPOS_input.getNPOS(0, y)[1];
				int ymin = yend;
				int ymax = ystart;
				boolean z = false;// Ob ein Zeichen im Letzten sekor gefunden
									// wurde

				for (x = 0; x < this.data_NPOS_input.getXlenght(); x++) {
					if (x > xend)// neuer Sektor
					{
						if (z) {
							wort.add(new Data_Zeichen('\u0000', xstart, xend, ymin, ymax, this.data_F_input,
									this.schwarzweiﬂ, "Zeichen Daten: " + ID));
							ID++;
							ymin = yend;
							ymax = ystart;

							z = false;
							int nextx = this.data_NPOS_input.getNPOS(x, ystart)[0];
							if (nextx - x > (yend - ystart) / 3)// leerzeichen
							{
								zeile.add(wort);
								wort = new DataList<Data_Zeichen>("Wort", false);
							}

							x = nextx;
							xstart = x + 1;
							if (xstart < this.data_NPOS_input.getXlenght()) {
								xend = this.data_NPOS_input.getNPOS(xstart, ystart)[0];
							}

							continue;
						} else {
							xstart = x;
							xend = this.data_NPOS_input.getNPOS(x, ystart)[0];
						}
					}

					for (y = ystart; y <= yend; y++) {
						if (this.data_F_input.getFloat(x, y) < 0.5f) {
							z = true;

							ymin = ymin > y ? y : ymin;
							ymax = ymax < y ? y : ymax;

							this.data_ID_output.setInt(x, y, ID);// TODO braucht
																	// man das?
						}
					}
				}

				y = yend;
			}
		}

		this.data_ID_output.setMaxid(ID);
		GUI.MainGUI.setTab(this.dataList_output);
		GUI.MainGUI.setTab(this.data_ID_output);

		DataList<Data> list = new DataList<Data>("return list", false);
		list.add(this.data_ID_output);
		list.add(this.dataList_output);
		return list;
	}
}
