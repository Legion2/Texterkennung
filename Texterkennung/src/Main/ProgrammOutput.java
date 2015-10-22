package Main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GUI.IGUI;
import texterkennung.Erkennung;
import texterkennung.Erkennung_Text;

public class ProgrammOutput implements IGUI
{
	public Erkennung erkennung;

	@Override
	public void gui() {
		// muss geändert werden
		
	}

	@Override
	public String getName()
	{
		return "Standart Ausgabe Fenster";
	}
	
	/**
	 * Wird aufgerufen wenn der Knopf zum start der Texterkennung gedrückt wird.
	 */
	public void Knopf_gedrueckt_Texterkennen()
	{
        if (this.erkennung != null)
        {
        	
        	int par1=Integer.parseInt("");
            int par2=Integer.parseInt("");
            
            this.erkennung.run(par1, par2);
        }
      //+update gui
	}
	
	/**
	 * Wird aufgerufen wenn der Knopf zum laden des Bildes gedrückt wird.
	 */
	public void Knopf_gedrueckt_Bildladen()
	{
        //Pfad zur Bild datei
		String pfad = "";
        if (pfad != null)
        {
            try
            {
            	ArrayList<Color> farbListe = new ArrayList<Color>();
            	farbListe.add(new Color(0, 0, 0));//Farbe Schwarz
            	this.erkennung = new Erkennung_Text(ImageIO.read(new File(pfad)), farbListe);

                //+update gui
            } catch (IOException ex) {
                System.out.println("Fehler aufgetreten beim Lesen der Datei");
            }
        }
	}
}
