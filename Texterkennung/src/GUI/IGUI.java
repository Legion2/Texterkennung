package GUI;

import debug.IInfo;
import javafx.scene.layout.Pane;

public interface IGUI extends IInfo
{
	/**
	 * Diese Methode wird vom javafx Thread aufgerufen und soll den Tabinhalt aktuallisieren.
	 * 
	 * @param pane Pane auf dem der Tabinhalt gezeichnet wird
	 */
	public void gui(Pane pane);
}
