package GUI;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import advanced.AColor;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import texterkennung.Erkennung;
import texterkennung.Erkennung_Text;


public class GuiElements extends Application
{
	public static GuiElements MainGUI;

	private HashMap<IGUI, Pane> list;
	private ProgrammOutput programmOutput;
	private TabPane tabPane;
	
	public Erkennung erkennung;
	

	@Override
	public void init()
	{
		// Init all classes for the program and add them to GUIList
		// TODO 
		MainGUI = this;//TODO kann man das besser lösen
		list = new HashMap<>();
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


		
		button_browse.setOnAction(
				new EventHandler<ActionEvent>() {

					@Override
					public void handle(final ActionEvent e) {
						FileChooser fileChooser = new FileChooser();
						File file = fileChooser.showOpenDialog(null);
						if (file != null) {
								textfield_filepath.setText(file.getAbsolutePath());
								
								
								try
					            {
					            	ArrayList<AColor> farbListe = new ArrayList<AColor>();
					            	farbListe.add(new AColor(0, 0, 0));//Farbe Schwarz
					            	erkennung = new Erkennung_Text(ImageIO.read(file), farbListe, new Font("Arial", Font.PLAIN, 30));

					                // TODO Hier muss die Anzeige der gui aktuallisiert werden
					            } catch (IOException ex) {
					                System.out.println("Fehler aufgetreten beim Lesen der Datei");
					            }
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
		Tab tab=new Tab();
		tab.setText(data.getName());
		
		Pane pane = new Pane();
		tab.setContent(pane);
		
		this.list.put(data, pane);
		
		this.tabPane.getTabs().add(tab);
	}
}