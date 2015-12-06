package GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import debug.Debugger;
import debug.IInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jogl.OpenGLHandler;
import texterkennung.Erkennung;
import texterkennung.Erkennung_Text;
import texterkennung.Erkennung_Vertretungsplan;
import texterkennung.data.Data_Image;

//TODO Rename this class
public class GuiElements extends Application implements EventHandler<ActionEvent>, ChangeListener<String>, IInfo, IConfigurable
{
	public static GuiElements MainGUI;
	
	/**
	 * Liste mit allen Tabs
	 */
	private HashMap<IGUI, Tab> list;
	
	private Button button_browse;
	private Button button_startCalc;
	private TextField textfield_filepath;
	private BorderPane borderPane;
	private TabPane tabPane;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, Class> modes;
	ObservableList<String> modesString;
	private String sectedMode;
	private BorderPane modeConfig;
	
	private Data_Image data_Image;
	public Erkennung erkennung;
	private OpenGLHandler openGLHandler;
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void init()
	{
		MainGUI = this;//TODO kann man das besser lösen
		
		this.openGLHandler = new OpenGLHandler();
		
		list = new HashMap<IGUI, Tab>();
		this.modes = new HashMap<String, Class>();
		
		//Add Erkennungs Modes
		this.modesString = FXCollections.observableArrayList();
		this.addMode("Texterkennung", Erkennung_Text.class);
		this.addMode("Vertretungsplan", Erkennung_Vertretungsplan.class);
		//this.addMode("Stundenplan", Erkennung_Text.class);
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
		
		//Modus Config
		this.modeConfig = configSetup();

		
		//Tab Layout
		this.tabPane = new TabPane();
		

		// (2) Layout-Klassen erzeugen und Komponenten einsetzen
		VBox UIElements = new VBox(fileUI, modeSelection, this.modeConfig, this.tabPane);
		UIElements.setPadding(new Insets(20));
		UIElements.setSpacing(5);

		BorderPane pane = new BorderPane();

		pane.setTop(label_title);
		pane.setCenter(UIElements);

		// (3) Szenen erzeugen und ins Fenster setzen
		Scene scene = new Scene(pane, 800, 800);


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
	
	/**
	 * Adds an Erkennugs Mode to List
	 * @param string showName
	 * @param erkennung Erkennungs class
	 */
	@SuppressWarnings("rawtypes")
	private void addMode(String string, Class erkennung)
	{
		this.modes.put(string, erkennung);
		this.modesString.add(string);
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
	
	/**
	 * Setup Modus Auswahl
	 * @return BorderPane
	 */
	private BorderPane modeSetup()
	{
		Label label_mode = new Label ("Modus: ");

		ComboBox<String> comboBox_mode = new ComboBox<String>(this.modesString);
		comboBox_mode.valueProperty().addListener(this);
		comboBox_mode.setPromptText("Texterkennungs Modus");

		this.button_startCalc = new Button ("Starte berechnung");
		this.button_startCalc.setOnAction(this);


		BorderPane modeSelection = new BorderPane(comboBox_mode);
		modeSelection.setLeft(label_mode);
		modeSelection.setRight(this.button_startCalc);

		modeSelection.setPadding(new Insets (10));

		BorderPane.setAlignment(label_mode, Pos.TOP_CENTER);
		BorderPane.setAlignment(comboBox_mode, Pos.TOP_CENTER);
		BorderPane.setAlignment(this.button_startCalc, Pos.TOP_CENTER);
		
		return modeSelection;
	}
	
	/**
	 * 
	 * @return BorderPane with Options
	 */
	private BorderPane configSetup()
	{
		BorderPane pane = new BorderPane();
		BorderPane pane2 = new BorderPane();
		BorderPane pane3 = new BorderPane();
		BorderPane pane4 = new BorderPane();
		BorderPane pane5 = new BorderPane();
		
		CheckBox checkBox = new CheckBox("Schwarzweiß");
		CheckBox checkBox2 = new CheckBox("GPU");
		ObservableList<String> observableList = FXCollections.observableArrayList();
		observableList.add("Arial");
		observableList.add("Verdana");
		observableList.add("Courier New");
		ComboBox<String> comboBox = new ComboBox<String>(observableList);
		//comboBox.setPromptText("Schriftart");
		comboBox.setEditable(true);
		comboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				GuiElements.MainGUI.updateFont(arg2);
			}
		});
		TextField textField = new TextField();
		textField.setPromptText("Schwellwert");
		TextField textField2 = new TextField();
		textField2.setPromptText("Farben");
		pane.setLeft(checkBox);
		pane2.setLeft(checkBox2);
		pane3.setLeft(comboBox);
		pane4.setLeft(textField);
		pane5.setLeft(textField2);
		
		VBox vBox = new VBox(pane, pane2, pane3, pane4, pane5);
		vBox.setSpacing(5);
		this.borderPane = new BorderPane(vBox);
		return borderPane;
	}
	
	/**
	 * Update the Mode Config Borderpane with the presetconfig
	 */
	@SuppressWarnings("unchecked")
	private void updateModeConfig()
	{
		String s = "";
		try {
			s = (String) this.modes.get(this.sectedMode).getMethod("getConfigPreset").invoke(null);
			Debugger.info(this, "Confug: " + s);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			Debugger.error(this, e.getMessage());
			e.printStackTrace();
		}
		
		VBox vBox = (VBox) this.borderPane.getCenter();
		
		String[] par = s.split(";");
		for (int i = 0; i < par.length && i < vBox.getChildren().size(); i++)
		{
			BorderPane pane = (BorderPane) vBox.getChildren().get(i);
			
			switch (i)
			{
			case 0:
				((CheckBox) pane.getLeft()).setSelected(par[i].equals("true"));
				break;
			case 1:
				((CheckBox) pane.getLeft()).setSelected(par[i].equals("true"));
				break;
			case 2:
				((ComboBox<String>) pane.getLeft()).getEditor().setText(par[i]);
				break;
			case 3:
				((TextField) pane.getLeft()).setText(par[i]);
				break;
			case 4:
				((TextField) pane.getLeft()).setText(par[i]);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Update the Font of the Font-selection Textfield
	 * @param font
	 */
	@SuppressWarnings("unchecked")
	public void updateFont(String font)
	{
		if (this.borderPane != null && javafx.scene.text.Font.font(font) != null)
		{
			Debugger.info(this, "Font name: " + javafx.scene.text.Font.font(font).getName());
			((ComboBox<String>) ((BorderPane)((VBox) this.borderPane.getCenter()).getChildren().get(2)).getLeft()).getEditor().setFont(new javafx.scene.text.Font(javafx.scene.text.Font.font(font).getName(), ((ComboBox<String>) ((BorderPane)((VBox) this.borderPane.getCenter()).getChildren().get(2)).getLeft()).getEditor().getFont().getSize()));
		}
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
				Tab tab = new Tab();
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
				BorderPane pane = new BorderPane();
				data.gui(pane);
				list.get(data).setContent(pane);
			}
		});
	}

	@SuppressWarnings("unchecked")
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
				if (this.sectedMode != null)
				{
					Class erkennung = this.modes.get(this.sectedMode);
					try {
						this.erkennung = (Erkennung) erkennung.asSubclass(Erkennung.class).getConstructor(Data_Image.class, OpenGLHandler.class, String.class).newInstance(this.data_Image, this.openGLHandler, this.getConfig());
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					
		        	this.erkennung.start();
				}
				else
				{
					Debugger.error(this, "Keine Modus ausgewählt!");
				}
			}
			else
			{
				Debugger.error(this, "Keine Datei ausgewählt!");
			}
		}
	}
	
	@Override
	public void changed(ObservableValue<? extends String> arg0, String arg1, String newString)
	{
		this.sectedMode = newString;
		this.updateModeConfig();
		Debugger.info(this, "SelectedMode: " + this.sectedMode);
	}

	@Override
	public String getName()
	{
		return "GuiElements";
	}

	@Override
	public void setConfig(String parameter)
	{
		//TODO
	}

	@Override
	public String getConfig()
	{
		String config = "";
		VBox vBox = (VBox) this.borderPane.getCenter();
		for (int i = 0; i < vBox.getChildren().size(); i++)
		{
			if (i > 0) config += ";";
			BorderPane pane = (BorderPane) vBox.getChildren().get(i);
			
			switch (i)
			{
			case 0:
				config += ((CheckBox) pane.getLeft()).isSelected() ? "true" : "false";
				break;
			case 1:
				config += ((CheckBox) pane.getLeft()).isSelected() ? "true" : "false";
				break;
			case 2:
				config += ((ComboBox<String>) pane.getLeft()).getEditor().getText();
				break;
			case 3:
				config += ((TextField) pane.getLeft()).getText();
				break;
			case 4:
				config += ((TextField) pane.getLeft()).getText();
				break;
			default:
				break;
			}
		}
		
		Debugger.info(this, "Config Parameter: " + config);
		return config;
	}
}