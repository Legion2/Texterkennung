package GUI;

import debug.IInfo;
import javafx.scene.layout.Pane;

public interface IGUI extends IInfo
{
	public void gui();
	
	public Pane tabpane = new Pane ();
}
