package GUI;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import advanced.AColor;
import debug.Debugger;
import debug.IInfo;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jogl.OpenGLHandler;
import texterkennung.Erkennung;
import texterkennung.Erkennung_Text;
import texterkennung.data.Data_Image;

//TODO Rename this class
public class GuiElements extends Application implements EventHandler<ActionEvent>, IInfo
{
	public static GuiElements MainGUI;
	
	private HashMap<IGUI, Tab> list;
	
	private ComboBox<String> comboBox_mode;
	private Button button_browse;
	private Button button_startCalc;
	private TextField textfield_filepath;
	private TabPane tabPane;
	
	private Data_Image data_Image;
	public Erkennung erkennung;
	private OpenGLHandler openGLHandler;
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void init()
	{
		MainGUI = this;//TODO kann man das besser lösen
		
		this.openGLHandler = new OpenGLHandler();
		
		list = new HashMap<IGUI, Tab>();
	}

	@Override
	public void start(final Stage stage)
	{
		// (1) Komponenten erzeugen

		//Titel Label
		Label label_title = new Label ("Texterkennung");
		label_title.setFont(GUI.LayoutElements.titleFont);
		label_title.setAlignment(Pos.TOP_LEFT);
		label_title.setPadding(new Insets (5));

		//DateiBrowser-Setup
		BorderPane fileUI = browseSetup();
		
		//Modus Auswahl
		BorderPane modeSelection = modeSetup();
		
		//Modus Auswahl
		BorderPane modeConfig = configSetup();

		//Tab Layout
		this.tabPane = new TabPane();

		// (2) Layout-Klassen erzeugen und Komponenten einsetzen

		VBox UIElements = new VBox (fileUI, modeSelection, modeConfig, tabPane);
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
	}

	@Override
	public void stop()
	{
		Debugger.info(this, "STOP");
		if (this.erkennung != null)
		{
			this.erkennung.close();
		}
		
		if (this.openGLHandler != null)
		{
			this.openGLHandler.stop();
		}
	}

	private BorderPane browseSetup()
	{
		Label label_file = new Label ("Dateipfad: ");
		this.textfield_filepath = new TextField ();
		this.button_browse = new Button ("Durchsuchen");

		BorderPane fileSetup = new BorderPane(textfield_filepath);
		fileSetup.setLeft(label_file);
		fileSetup.setRight(button_browse);
		fileSetup.setPadding(new Insets(10));

		BorderPane.setAlignment(label_file, Pos.TOP_CENTER);
		BorderPane.setAlignment(button_browse, Pos.TOP_CENTER);
		BorderPane.setAlignment(textfield_filepath, Pos.TOP_CENTER);
		
		this.button_browse.setOnAction(this);//EventHandler
		
		return fileSetup;
	}
	
	private BorderPane modeSetup()
	{
		Label label_mode = new Label ("Modus: ");

		this.comboBox_mode = new ComboBox<String>();
		this.comboBox_mode.getItems().addAll("Texterkennung Arial", "Texterkennung Courier", "Stundenplan", "Vertretungsplan");
		this.comboBox_mode.setValue("Texterkennung Arial");

		this.button_startCalc = new Button ("Starte berechnung");
		this.button_startCalc.setOnAction(this);


		BorderPane modeSelection = new BorderPane(this.comboBox_mode);
		modeSelection.setLeft(label_mode);
		modeSelection.setRight(this.button_startCalc);

		modeSelection.setPadding(new Insets (10));

		BorderPane.setAlignment(label_mode, Pos.TOP_CENTER);
		BorderPane.setAlignment(this.comboBox_mode, Pos.TOP_CENTER);
		BorderPane.setAlignment(this.button_startCalc, Pos.TOP_CENTER);
		
		
		return modeSelection;
	}
	
	private BorderPane configSetup()
	{
		BorderPane borderPane = new BorderPane();
		
		return borderPane;
	}

	/**
	 * Fügt der GUI einen neuen Tab hinzu
	 * 
	 * @param data Daten die im Tab angezeigt werden
	 */
	public void addTab(IGUI data)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Tab tab=new Tab();
				tab.setText(data.getName());
				
				list.put(data, tab);
				
				tabPane.getTabs().add(tab);
			}
		});
	}
	
	/**
	 * Aktuallisiert den Inhalt des Tabs, der zu data gehört
	 * 
	 * @param data Daten die im Tab angezeigt werden
	 */
	public void setTab(IGUI data)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Pane pane = new Pane();
				data.gui(pane);
				list.get(data).setContent(pane);
			}
		});
	}

	@Override
	public void handle(ActionEvent arg0)
	{
		if (arg0.getSource() == this.button_browse)
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("./res"));
			File file = fileChooser.showOpenDialog(null);
			if (file != null)
			{
				this.textfield_filepath.setText(file.getAbsolutePath());
				
				try
	            {
					BufferedImage image = ImageIO.read(file);
					
					this.data_Image = new Data_Image(image, "Originalbild", true);
	            } catch (IOException ex) {
	            	Debugger.error(this, "Fehler aufgetreten beim Lesen der Datei");
	            }
			}
		}
		else if (arg0.getSource() == this.button_startCalc)
		{
			if (this.data_Image != null)
			{
				ArrayList<AColor> farbListe = new ArrayList<AColor>();
	        	
	        	switch (this.comboBox_mode.getValue())
	        	{
	        	case "Texterkennung Arial":
	        		farbListe.add(new AColor(0, 0, 0));//Farbe Schwarz
	        		farbListe.add(new AColor(255, 0, 0));//Farbe rot
	        		this.erkennung = new Erkennung_Text(this.data_Image, farbListe, new Font("Arial", Font.PLAIN, 30), true, this.openGLHandler.getGL4());
	        		break;
	        	case "Texterkennung Courier":
	        		farbListe.add(new AColor(0, 0, 0));//Farbe Schwarz
	        		this.erkennung = new Erkennung_Text(this.data_Image, farbListe, new Font("Courier", Font.PLAIN, 30), true, this.openGLHandler.getGL4());
	        		break;
	        	case "Stundenplan":
	        		farbListe.add(new AColor(255, 0, 0));//Farbe rot
	        		this.erkennung = new Erkennung_Text(this.data_Image, farbListe, new Font("Arial", Font.PLAIN, 30), true, this.openGLHandler.getGL4());
	        		break;
	        	case "Vertretungsplan":
	        		//TODO
	        		break;
	        	}
				
	        	this.erkennung.start();
			}
			else
			{
				Debugger.error(this, "Keine Datei ausgewählt!");
			}
		}
	}

	@Override
	public String getName()
	{
		return "GuiElements";
	}
}