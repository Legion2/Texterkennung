package GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;

import debug.Debugger;
import debug.IInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jogl.OpenGLHandler;
import texterkennung.Erkennung;
import texterkennung.Erkennung_Text;
import texterkennung.Erkennung_Vertretungsplan;
import texterkennung.data.Data_Image;


public class GUI extends Application implements IInfo, IConfigurable
{
	public static GUI MainGUI;
	
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
	private ArrayList<String> fonts;
	
	private Data_Image data_Image;
	private Erkennung erkennung;
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
		
		this.list = new HashMap<IGUI, Tab>();
		this.modes = new HashMap<String, Class>();
		
		//Add Erkennungs Modes
		this.modesString = FXCollections.observableArrayList();
		this.addMode("Texterkennung", Erkennung_Text.class);
		this.addMode("Vertretungsplan", Erkennung_Vertretungsplan.class);
		//this.addMode("Stundenplan", Erkennung_Text.class);
		
		//Add Fonts
		this.fonts = new ArrayList<String>();
		this.fonts.add("Arial");
		this.fonts.add("Verdana");
		this.fonts.add("Courier New");
		this.fonts.add("Times New Roman");
	}

	@Override
	public void start(final Stage stage)
	{
		// (1) Komponenten erzeugen

		//Titel Label
		Label label_title = new Label ("Texterkennung");
		label_title.setFont(new Font ("Arial", 24));
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
		this.tabPane.getSelectionModel().selectedItemProperty().addListener((arg0, arg1, arg2) -> {
			Tab tab = (Tab) arg2;
			Debugger.info(this, "Tab Selection changed " + tab.getText());
			
			this.updateTab(tab);
		});
		

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

		this.button_browse.setOnAction(arg0 -> {
			FileChooser fileChooser = new FileChooser();

			fileChooser.setInitialDirectory(new File("./res"));
			File file = fileChooser.showOpenDialog(null);
			if (file != null)
			{
				this.textfield_filepath.setText(file.getAbsolutePath());

				try {
					BufferedImage image = ImageIO.read(file);

					this.data_Image = new Data_Image(image, "Originalbild", true);
				} catch (IOException ex) {
					Debugger.error(this, "Fehler aufgetreten beim Lesen der Datei");
				}
			}
		});

		return fileSetup;
	}
	
	/**
	 * Setup Modus Auswahl
	 * @return BorderPane
	 */
	@SuppressWarnings("unchecked")
	private BorderPane modeSetup()
	{
		Label label_mode = new Label ("Modus: ");

		ComboBox<String> comboBox_mode = new ComboBox<String>(this.modesString);
		comboBox_mode.valueProperty().addListener((arg0, arg1, arg2) -> {
			this.sectedMode = (String) arg2;
			this.updateModeConfig();
			Debugger.info(this, "SelectedMode: " + this.sectedMode);
		});
		comboBox_mode.setPromptText("Texterkennungs Modus");

		this.button_startCalc = new Button("Starte berechnung");
		this.button_startCalc.setOnAction(arg0 -> {
			if (this.data_Image != null) {
				if (this.sectedMode != null) {
					if (((HBox) ((BorderPane) ((VBox) this.borderPane.getCenter()).getChildren().get(4)).getLeft()).getChildren().size() > 0)
					{
						Class erkennung = this.modes.get(this.sectedMode);
						try {
							this.erkennung = (Erkennung) erkennung.asSubclass(Erkennung.class)
									.getConstructor(Data_Image.class, OpenGLHandler.class, String.class)
									.newInstance(this.data_Image, this.openGLHandler, this.getConfig());
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
						CompletableFuture.runAsync(this.erkennung);
						//this.erkennung.start();
					} else {
						Debugger.error(this, "Keine Farbe ausgewählt!");
					}
				} else {
					Debugger.error(this, "Keine Modus ausgewählt!");
				}
			} else {
				Debugger.error(this, "Keine Datei ausgewählt!");
			}
		});


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
		//TODO alle Borderpanes in ein Array packen
		BorderPane pane = new BorderPane();//AA
		BorderPane pane2 = new BorderPane();//GPU
		BorderPane pane3 = new BorderPane();//Schriftart
		BorderPane pane4 = new BorderPane();//Schwellwert
		BorderPane pane5 = new BorderPane();//Farben
		BorderPane pane6 = new BorderPane();//Skalierung
		
		CheckBox checkBox = new CheckBox("Schwarzweiß");
		CheckBox checkBox2 = new CheckBox("GPU");
		checkBox2.setIndeterminate(!this.openGLHandler.supportOpenGL());
		checkBox2.setDisable(!this.openGLHandler.supportOpenGL());
		ObservableList<String> observableList = FXCollections.observableArrayList();
		for (String font : this.fonts)
		{
			observableList.add(font);
		}
		ComboBox<String> comboBox = new ComboBox<String>(observableList);
		comboBox.setPromptText("Schriftart");
		comboBox.setEditable(true);
		comboBox.getEditor().textProperty().addListener((arg0, arg1, arg2) -> {
			GUI.MainGUI.updateFont(arg2);
		});
		Label label = new Label("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(),.;:!?äöüÄÖÜß-");
		TextField textField = new TextField();
		textField.setPromptText("Schwellwert");
		HBox hBox = new HBox();
		ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		hBox.getChildren().add(colorPicker);
		Button removeButton = new Button("Farbe entfernen");
		removeButton.setOnAction(arg0 -> {
			Debugger.info(this, "remove Colorpicker");
			HBox hBoxl = (HBox) ((BorderPane) ((VBox) this.borderPane.getCenter()).getChildren().get(4)).getLeft();
			
			if (hBoxl.getChildren().size() > 0)
			{
				hBoxl.getChildren().remove(hBoxl.getChildren().size() - 1);
			}
			else
			{
				Debugger.error(this, "Kein Colorpicker!");
			}
		});
		Button addButton = new Button("Farbe hinzufügen");
		addButton.setOnAction(arg0 -> {
			Debugger.info(this, "add Colorpicker");
			HBox hBoxl = (HBox) ((BorderPane) ((VBox) this.borderPane.getCenter()).getChildren().get(4)).getLeft();
			ColorPicker colorPickerl = new ColorPicker(Color.BLACK);
			hBoxl.getChildren().add(colorPickerl);
		});
		TextField textField2 = new TextField();
		textField2.setPromptText("Skalierung in %");
		
		pane.setLeft(checkBox);
		pane2.setLeft(checkBox2);
		pane3.setLeft(comboBox); pane3.setRight(label);
		pane4.setLeft(textField);
		pane5.setLeft(hBox); pane5.setCenter(removeButton); pane5.setRight(addButton);
		pane6.setLeft(textField2);
		
		VBox vBox = new VBox(pane, pane2, pane3, pane4, pane5, pane6);
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
				HBox hBox = new HBox();
				String[] c = par[i].split(":");
				for (String color : c)
				{
					ColorPicker colorPicker = new ColorPicker();
					java.awt.Color color2 = new java.awt.Color(Integer.valueOf(color));
					colorPicker.setValue(Color.rgb(color2.getRed(), color2.getGreen(), color2.getBlue()));
					hBox.getChildren().add(colorPicker);
				}
				pane.setLeft(hBox);
				break;
			case 5:
				((TextField) pane.getLeft()).setText(par[i]);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Update the Font of the Font-selection Textfield
	 * @param font Neue Schriftart
	 */
	public void updateFont(String font)
	{
		if (this.borderPane != null && javafx.scene.text.Font.font(font) != null)
		{
			Font newfont = new javafx.scene.text.Font(javafx.scene.text.Font.font(font).getName(), ((ComboBox<String>) ((BorderPane)((VBox) this.borderPane.getCenter()).getChildren().get(2)).getLeft()).getEditor().getFont().getSize());
			Debugger.info(this, "Font name: " + newfont.getName());
			((ComboBox<String>) ((BorderPane)((VBox) this.borderPane.getCenter()).getChildren().get(2)).getLeft()).getEditor().setFont(newfont);
			((Label) ((BorderPane)((VBox) this.borderPane.getCenter()).getChildren().get(2)).getRight()).setFont(newfont);
		}
	}

	/**
	 * Fügt der GUI einen neuen Tab hinzu
	 * 
	 * @param data Daten die im Tab angezeigt werden
	 */
	public void addTab(IGUI data)
	{
		Platform.runLater(() -> {
			Tab tab = new Tab();
			tab.setText(data.getName());

			list.put(data, tab);

			tabPane.getTabs().add(tab);
		});
	}

	/**
	 * Aktuallisiert den Inhalt des Tabs, der zu data gehört
	 * 
	 * @param data Daten die im Tab angezeigt werden
	 */
	public void setTab(IGUI data)
	{
		/*Platform.runLater(new Runnable() {
			@Override
			public void run() {
				BorderPane pane = new BorderPane();
				data.gui(pane);
				list.get(data).setContent(pane);
			}
		});*/
	}
	
	private void updateTab(Tab tab)
	{
		BorderPane pane = new BorderPane();
		
		this.list.entrySet().stream().filter(entry -> (tab.equals(entry.getValue()))).findFirst().ifPresent(entry -> entry.getKey().gui(pane));
		
		tab.setContent(pane);
		Debugger.info(this, "updated tab " + tab.getText());
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
				config += (((CheckBox) pane.getLeft()).isDisabled() || !((CheckBox) pane.getLeft()).isSelected()) ? "false" : "true";
				break;
			case 2:
				config += ((ComboBox<String>) pane.getLeft()).getEditor().getText();
				break;
			case 3:
				config += ((TextField) pane.getLeft()).getText();
				break;
			case 4:
				for (Node colorPicker : ((HBox) pane.getLeft()).getChildren())
				{
					Color color = ((ColorPicker) colorPicker).getValue();
					config += new java.awt.Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue()).getRGB() + ":";
				}
				config = config.substring(0, config.length() - 1);
				break;
			case 5:
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