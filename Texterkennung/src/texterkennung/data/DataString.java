package texterkennung.data;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class DataString extends Data
{
	private final String data;
	private Font font;
	
	public DataString(String data, String name, boolean tab)
	{
		super(name, tab);
		this.data = data;
	}
	
	public String get()
	{
		return this.data;
	}
	
	public void setFont(java.awt.Font font)
	{
		this.font = new Font(font.getFamily(), font.getSize());
	}
	
	@Override
	public void gui(BorderPane pane)
	{
		Label l = new Label(this.data);
		if (this.font != null) l.setFont(this.font);
		pane.setCenter(l);
	}
}