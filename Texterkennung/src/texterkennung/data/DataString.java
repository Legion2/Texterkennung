package texterkennung.data;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class DataString extends Data
{
	private final String data;
	
	public DataString(String data, String name, boolean tab)
	{
		super(name, tab);
		this.data = data;
	}
	
	public String get()
	{
		return this.data;
	}

	@Override
	public void gui(Pane pane)
	{
		Label l = new Label(this.data);
		//Font f = new Font("Arial", Font.BOLD, 100);
		//l.setFont(f);
		pane.getChildren().add(l);
	}
}
