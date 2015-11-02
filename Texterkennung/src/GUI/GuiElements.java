package GUI;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GuiElements extends Application
{
	public static GuiElements MainGUI;

	private ArrayList<IGUI> list;
	private ProgrammOutput programmOutput;
	private TabPane tabPane;

	@Override
	public void init()
	{
		// Init all classes for the program and add them to GUIList
		// TODO 
		MainGUI = this;//TODO kann man das besser lösen
		list = new ArrayList<IGUI>();
		this.programmOutput = new ProgrammOutput();
	}

	@Override
	public void start(final Stage stage) {

		// (1) Komponenten erzeugen

		//Titel Label
		Label label_title = new Label ("Texterkennung");
		label_title.setFont(GUI.LayoutElements.titleFont);
		label_title.setAlignment(Pos.TOP_LEFT);
		label_title.setPadding(new Insets (5));

		//DateiBrowser-Setup
		BorderPane fileUI=browseSetup();



		//Modus Auswahl
		Label label_mode = new Label ("Modus: ");

		ComboBox<String> comboBox_mode = new ComboBox<String>();
		comboBox_mode.getItems().addAll("Texterkennung", "Stundenplan", "Vertretungsplan");
		comboBox_mode.setValue("Texterkennung");

		Button button_startCalc = new Button ("Starte berechnung");


		BorderPane modeSelection = new BorderPane(comboBox_mode);
		modeSelection.setLeft(label_mode);
		modeSelection.setRight(button_startCalc);

		modeSelection.setPadding(new Insets (10));

		BorderPane.setAlignment(label_mode, Pos.TOP_CENTER);
		BorderPane.setAlignment(comboBox_mode, Pos.TOP_CENTER);
		BorderPane.setAlignment(button_startCalc, Pos.TOP_CENTER);


		//Berechnung Button



		//Tab Layout
		this.tabPane = new TabPane();
		Tab tab = new Tab();
		tab.setText("new tab");
		tab.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
		tabPane.getTabs().add(tab);

		// (2) Layout-Klassen erzeugen und Komponenten einsetzen

		VBox UIElements = new VBox (fileUI, modeSelection, tabPane);
		UIElements.setPadding(new Insets(20));
		UIElements.setSpacing(5);

		BorderPane pane = new BorderPane();

		pane.setTop(label_title);
		pane.setCenter(UIElements);



		// (3) Szenen erzeugen und ins Fenster setzen

		Scene scene = new Scene(pane, 800, 500);


		// (4) Fenster konfigurieren und anzeigen


		stage.setTitle("Informatik Projekt: Texterkennung");
		stage.setScene(scene);
		stage.show();


		//------------------------------------------------------
		/**
		 * only for testing
		 * TODO remove this later
		 */
		this.programmOutput.Knopf_gedrueckt_Bildladen();
		this.programmOutput.Knopf_gedrueckt_Texterkennen();
		//-------------------------------------------------


	}

	private BorderPane browseSetup() {

		Label label_file = new Label ("Dateipfad: ");
		TextField textfield_filepath = new TextField ();
		Button button_browse = new Button ("Durchsuchen");


		BorderPane fileSetup = new BorderPane(textfield_filepath);
		fileSetup.setLeft(label_file);
		fileSetup.setRight(button_browse);
		fileSetup.setPadding(new Insets(10));

		BorderPane.setAlignment(label_file, Pos.TOP_CENTER);
		BorderPane.setAlignment(button_browse, Pos.TOP_CENTER);
		BorderPane.setAlignment(textfield_filepath, Pos.TOP_CENTER);


		FileChooser fileChooser = new FileChooser();
		button_browse.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						File file = fileChooser.showOpenDialog(null);
						if (file != null) {
								textfield_filepath.setText(file.getAbsolutePath());
						}
					}
				});
		
		
		return fileSetup;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void addTab(IGUI data)
	{
		list.add(data);

		Tab tab=new Tab();
		tab.setText(data.getName());
		tab.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
		this.tabPane.getTabs().add(tab);
	}
}