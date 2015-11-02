package GUI;

import GUI.LayoutElements;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GuiElements extends Application{

	@Override
	public void start(final Stage stage) {

		// (1) Komponenten erzeugen

		//Titel Label
		Label label_title = new Label ("Texterkennung");
		label_title.setFont(GUI.LayoutElements.titleFont);
		label_title.setAlignment(Pos.TOP_LEFT);
		label_title.setPadding(new Insets (15));

		//DateiBrowser-Setup
		Label label_file = new Label ("Dateipfad: ");
		TextField textfield_filepath = new TextField ();

		Button button_browse = new Button ("Durchsuchen");

		FileChooser fileChooser = new FileChooser();
		button_browse.setOnAction(
				new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent e) {
						File file = fileChooser.showOpenDialog(stage);
						if (file != null) {
							//openFile(file);
						}
					}
				});

		BorderPane fileSetup = new BorderPane(textfield_filepath);
		fileSetup.setLeft(label_file);
		fileSetup.setRight(button_browse);
		fileSetup.setPadding(new Insets(20));

		fileSetup.setAlignment(label_file, Pos.TOP_CENTER);
		fileSetup.setAlignment(button_browse, Pos.TOP_CENTER);
		fileSetup.setAlignment(textfield_filepath, Pos.TOP_CENTER);



		//Modus Auswahl
		Label label_mode = new Label ("Modus: ");

		ComboBox comboBox_mode = new ComboBox();
		comboBox_mode.getItems().addAll("Texterkennung", "Stundenplan", "Vertretungsplan");
		comboBox_mode.setValue("Texterkennung");

		BorderPane modeSelection = new BorderPane(comboBox_mode);
		modeSelection.setLeft(label_file);
		modeSelection.setPadding(new Insets (20));

		modeSelection.setAlignment(label_mode, Pos.TOP_CENTER);
		modeSelection.setAlignment(comboBox_mode, Pos.TOP_CENTER);


		//Berechnung Button
		Button button_startCalc = new Button ("Starte berechnung");

		//Tab Layout
		TabPane tabPane = new TabPane();
		Tab tab = new Tab();
		tab.setText("new tab");
		tab.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
		tabPane.getTabs().add(tab);

		// (2) Layout-Klassen erzeugen und Komponenten einsetzen

		VBox UIElements = new VBox (fileSetup, modeSelection, button_startCalc, tabPane);
		UIElements.setPadding(new Insets(20));
		UIElements.setSpacing(25);
		
		BorderPane pane = new BorderPane();

		pane.setTop(label_title);
		pane.setCenter(UIElements);



		// (3) Szenen erzeugen und ins Fenster setzen

		Scene scene = new Scene(pane, 800, 500);


		// (4) Fenster konfigurieren und anzeigen


		stage.setTitle("Informatik Projekt: Texterkennung");
		stage.setScene(scene);
		stage.show();







	}

	public static void main(String[] args) {
		Application.launch(args);
	}


}