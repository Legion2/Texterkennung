package GUI;

import java.io.IOException;

import debug.Debugger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainGUI extends Application
{
	private static Debugger debugger;
	private ProgrammOutput programmOutput;
	
	private Stage WindowLayout;
	private BorderPane TexterkennungPanels;

	@Override
	public void init()
	{
		// Init all classes for the program and add them to GUIList
		// TODO 
		
		
		debugger = new Debugger();
		this.programmOutput = new ProgrammOutput();
	}

	@Override
	public void start(Stage WindowLayout) {
		this.WindowLayout = WindowLayout;
		this.WindowLayout.setTitle("Informatik Projekt: Texterkennung");

		initRootLayout();

		showPersonOverview();
		
		//------------------------------------------------------
		/**
		 * only for testing
		 * TODO remove this later
		 */
		this.programmOutput.Knopf_gedrueckt_Bildladen();
		this.programmOutput.Knopf_gedrueckt_Texterkennen();
		//-------------------------------------------------
	}

	/**
	 * Initializes the WindowLayout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainGUI.class.getResource("views/WindowLayout.fxml"));
			TexterkennungPanels = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(TexterkennungPanels);
			WindowLayout.setScene(scene);
			WindowLayout.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showPersonOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainGUI.class.getResource("views/TexterkennungPanels.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			TexterkennungPanels.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return WindowLayout;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Debugger getDebugger()
	{
		return debugger;
	}
}
