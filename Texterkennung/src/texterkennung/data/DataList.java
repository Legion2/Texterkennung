package texterkennung.data;

import java.util.ArrayList;

import GUI.IGUI;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DataList extends Data
{
	private ArrayList<Data> data;
	
	public DataList(String name)
	{
		super(name, true);
		this.data = new ArrayList<Data>();
	}
	
	public DataList(String name, boolean b)
	{
		super(name, b);
		this.data = new ArrayList<Data>();
	}

	public void add(Data data)
	{
		this.data.add(data);
	}
	
	public Data get(int index)
	{
		return this.data.get(index);
	}
	
	public ArrayList<Data> get()
	{
		return this.data;
	}
	
	public int size()
	{
		return this.data.size();
	}

	@Override
	public void gui(BorderPane pane)
	{
		TabPane tabPane = new TabPane();
		for (IGUI iGUI : this.data)
		{
			Tab tab = new Tab();
			tab.setText(iGUI.getName());
			
			BorderPane pane2 = new BorderPane();
			
			iGUI.gui(pane2);
			
			tab.setContent(pane2);
			tabPane.getTabs().add(tab);
		}
		
		pane.setCenter(tabPane);
	}

}
